package com.zl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zl.config.RabbitMqConfig;
import com.zl.dao.TransferDao;
import com.zl.pojo.Account;
import com.zl.pojo.Transfer;
import com.zl.pojo.User;
import com.zl.service.TransferService;
import com.zl.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.*;

/**
 * 转账service层实现类
 *
 * @author root
 */
@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    private TransferDao transferDao;
    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 定时任务
     *
     * @param transfer
     */
    @Override
    public void executeJob(Transfer transfer) {

    }

    /**
     * 同行转账
     * 只涉及金额的变动
     *
     * @param transfer 交易对象
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void transferMoney(Transfer transfer) {
        //生成流水号
        String dealNo = Long.toHexString(UUID.randomUUID().getMostSignificantBits()) + Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
        transfer.setDealNo(dealNo);
        //设置类型kind
        transfer.setKind("转账");
        //补充交易对象信息
        transfer.setCurrency("CNY");
        //收入行信息
        String accIn = transfer.getAccIn();
        Map<String, String> mapIn = queryBankAndUserName(accIn);
        transfer.setAccInName(mapIn.get("userName"));
        transfer.setAccInBank(mapIn.get("bankName"));
        //转出行信息
        String accOut = transfer.getAccOut();
        Map<String, String> mapOut = queryBankAndUserName(accOut);
        transfer.setAccOutName(mapOut.get("userName"));
        transfer.setAccOutBank(mapOut.get("bankName"));
        //添加事务管理
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //设置回滚点
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            transferDao.subMoney(transfer);
            transferDao.addMoney(transfer);
            //转账成功，设置交易状态为“成功”
            transfer.setTransStatus("成功");
            //如果转账类型为空，则设为同行转账，代号0
            if (transfer.getTransType() == null) {
                transfer.setTransType(0);
            }
            //写入交易记录
            writeDeal(transfer);
        } catch (Exception e) {
            //事务回滚
            transactionManager.rollback(status);
            e.printStackTrace();
        }
    }

    /**
     * 跨境转账
     *
     * 银行自动为客户购外汇汇款
     *
     * @param transfer 交易对象
     * @return
     */
    @Override
    public void transferMoneyOver(Transfer transfer) {
        //汇率转换，计算账户需要扣金额
        String host = "https://ali-waihui.showapi.com";
        String path = "/waihui-transform";
        String method = "GET";
        String appcode = "937341d2258345458f648e962bfb0f81";
        String moneyCNY=null;
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("fromCode", transfer.getCurrency());
        querys.put("money", transfer.getTransFund().toString());
        querys.put("toCode", "CNY");

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            JSONObject jb=JSONObject.parseObject(EntityUtils.toString(response.getEntity()));

            moneyCNY= (String) ((JSONObject)jb.get("showapi_res_body")).get("money");
        } catch (Exception e) {
            e.printStackTrace();
        }

        BigDecimal balance=queryBalance(transfer.getAccOut());
        BigDecimal transMoney=new BigDecimal(moneyCNY);

        //判断余额是否充足
        if(balance.compareTo(transMoney)>0){
            //添加事务管理
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setName("SomeTxName");
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            //设置回滚点
            TransactionStatus status = transactionManager.getTransaction(def);
            try {
                //生成交易流水号,32位随机不重复
                String dealNo = Long.toHexString(UUID.randomUUID().getMostSignificantBits()) + Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
                transfer.setDealNo(dealNo);
                //跨境转账记录状态位处理中
                transfer.setTransStatus("0");
                //设置转账类型为跨境转账
                transfer.setTransType(2);
                transfer.setKind("跨境转账");

                //收入行信息
                String accIn = transfer.getAccIn();
                Map<String, String> mapIn = queryBankAndUserName(accIn);
                transfer.setAccInName(mapIn.get("userName"));
                transfer.setAccInBank(mapIn.get("bankName"));
                //转出行信息
                String accOut = transfer.getAccOut();
                Map<String, String> mapOut = queryBankAndUserName(accOut);
                transfer.setAccOutName(mapOut.get("userName"));
                transfer.setAccOutBank(mapOut.get("bankName"));

                /**
                 * 把交易记录放到Map中准备发送到消息队列
                 */
                Map map=new HashMap();
                map.put("dealNo",transfer.getDealNo());
                map.put("transType",transfer.getTransType());
                map.put("transStatus",transfer.getTransStatus());
                map.put("accOut",transfer.getAccOut());
                map.put("accOutName",transfer.getAccOutName());
                map.put("accOutBank",transfer.getAccOutBank());
                map.put("accIn",transfer.getAccIn());
                map.put("accInName",transfer.getAccInName());
                map.put("accInBank",transfer.getAccInBank());
                map.put("currency",transfer.getCurrency());
                map.put("transFund",transfer.getTransFund());
                map.put("kind",transfer.getKind());

                transfer.setCurrency("CNY");
                transfer.setTransFund(transMoney);
                /**
                 * 减钱
                 */
                transferDao.subMoney(transfer);

                transfer.setCurrency(map.get("currency").toString());
                transfer.setTransFund(new BigDecimal(map.get("transFund").toString()));
                /**
                 * 写入交易记录
                 */
                writeDeal(transfer);
                /**
                 * 发送消息到队列
                 */
                rabbitTemplate.convertAndSend("directExchange", RabbitMqConfig.ROUTINGKEY_B,map);
                System.out.println("跨境转账处理中，发送消息到境外银行。。。。");
            } catch (Exception e) {
                //事务回滚
                transactionManager.rollback(status);
                e.printStackTrace();
            }
        }else {
            //余额不足。。。。
        }


    }

    /**
     * 写入交易记录
     * 转账、资金归集、主动收款都要写入交易记录
     *
     * @param transfer
     * @return
     */
    @Override
    public int writeDeal(Transfer transfer) {
        //设置交易时间
        transfer.setDealDate(new Date());
        return transferDao.writeDeal(transfer);
    }

    /**
     * 根据卡号查询余额
     *
     * @param accNo 卡号
     * @return 余额
     */
    @Override
    public BigDecimal queryBalance(String accNo) {
        return transferDao.queryBalance(accNo);
    }

    /**
     * 根据卡号查询用户名和银行
     *
     * @param accNo 卡号
     * @return
     */
    @Override
    public Map<String, String> queryBankAndUserName(String accNo) {
        Map<String, String> map = new HashMap<>();
        String bankName = transferDao.queryBankName(accNo);
        String userName = transferDao.queryUserName(accNo);
        map.put("bankName", bankName);
        map.put("userName", userName);
        return map;
    }

    /**
     * 根据卡号和用户名验证用户是否存在
     *
     * @param userName 用户名
     * @param accNo    卡号
     * @return
     */
    @Override
    public Boolean checkUser(String userName, String accNo) {
        User user = new User();
        user.setUserName(userName);
        Account account=new Account();
        account.setAccNo(accNo);
        user.setAccount(account);
        user = transferDao.checkUser(user);
        //用户存在
        if (user != null) {
            return true;
        }
        return false;
    }

    /**
     * 根据流水号查询交易记录
     *
     * @param dealNo
     * @return 流水号
     */
    @Override
    public Transfer queryTransferByDealNo(String dealNo) {
        return transferDao.queryTransferByDealNo(dealNo);
    }

    /**
     * 根据流水号查询未完成的记录
     *
     * @param dealNo
     * @return
     */
    @Override
    public Transfer queryTransferDealing(String dealNo) {
        return transferDao.queryTransferDealing(dealNo);
    }

    /**
     * 流水记录处理成功
     *
     * @param dealNo
     * @return
     */
    @Override
    public int transferConfirm(String dealNo) {
        return transferDao.transferConfirm(dealNo);
    }

    /**
     * 自动把未完成的记录发送到消息队列
     */
    @Override
    public void autoSend() {
        List<Transfer> transfers=transferDao.queryAllDealing();
        if(transfers!=null&&transfers.size()>0){
            for(Transfer transfer:transfers){
                /**
                 * 把交易记录放到Map中准备发送到消息队列
                 */
                Map map=new HashMap();
                map.put("dealNo",transfer.getDealNo());
                map.put("transType",transfer.getTransType());
                map.put("transStatus",transfer.getTransStatus());
                map.put("accOut",transfer.getAccOut());
                map.put("accOutName",transfer.getAccOutName());
                map.put("accOutBank",transfer.getAccOutBank());
                map.put("accIn",transfer.getAccIn());
                map.put("accInName",transfer.getAccInName());
                map.put("accInBank",transfer.getAccInBank());
                map.put("currency",transfer.getCurrency());
                map.put("transFund",transfer.getTransFund());
                map.put("kind",transfer.getKind());
                //发送消息到消息队列
                rabbitTemplate.convertAndSend("directExchange", RabbitMqConfig.ROUTINGKEY_B,map);
            }
        }
    }
}
