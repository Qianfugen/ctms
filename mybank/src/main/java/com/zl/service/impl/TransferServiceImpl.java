package com.zl.service.impl;

import com.zl.api.JobAPI;
import com.zl.config.RabbitMqConfig;
import com.zl.dao.TransferDao;
import com.zl.pojo.Account;
import com.zl.pojo.Job;
import com.zl.pojo.Transfer;
import com.zl.pojo.User;
import com.zl.service.TransferService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    RabbitTemplate rabbitTemplate;
    @Autowired
    private JobAPI jobAPI;

    /**
     * 任务分类，定时转账or立即转账
     */
    @Override
    public void executeJob(Transfer transfer) {
        if ("0".equals(transfer.getKind())) {
            //立即转账
            System.out.println("立即转账。。。");
            transferMoney(transfer);
        } else {
            //1分钟后转账
            System.out.println("1分钟后转账");
            long currentTime = System.currentTimeMillis();
            System.out.print("      当前时间:");
            System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(currentTime));
            //2小时后的时间
//            currentTime += 120 * 60 * 1000;
            //1分钟后转账
            currentTime += 60 * 1000;
            Date date = new Date(currentTime);
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyyMMddHHmmss");
            System.out.print("1分钟后时间:");
            System.out.println(dateFormat.format(date));
            String time = dateFormat.format(date);
            String month = time.substring(4, 6);
            String day = time.substring(6, 8);
            String hour = time.substring(8, 10);
            String minute = time.substring(10, 12);
            String second = time.substring(12, 14);
            //编写cron表达式
            String cron = second + " " + minute + " " + hour + " " + day + " " + month + " ?";
            System.out.println(cron);
            //设置任务
            Job job = new Job();
            //暂且把转出账户作为任务ID,这样一个用户有且只有一个在等待的定时任务，任务执行后删除
            job.setId(Long.parseLong(transfer.getAccOut()));
            job.setAccIn(transfer.getAccIn());
            job.setAccOut(transfer.getAccOut());
            //设置为CNY交易，境外转账从页面获取，这里仅做测试
            job.setCurrency("CNY");
            job.setTransFund(transfer.getTransFund());
            job.setCron(cron);

            //调用定时任务模块
            jobAPI.insertJob(job);
        }
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
            System.out.println(transfer);
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
     * @param transfer 交易对象
     * @return
     */
    @Override
    public void transferMoneyOver(Transfer transfer) {
        String host = "https://ali-waihui.showapi.com";
        String path = "/waihui-transform";
        String method = "GET";
        String appcode = "937341d2258345458f648e962bfb0f81";
        String moneyCNY = null;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("fromCode", transfer.getCurrency());
        querys.put("money", transfer.getTransFund().toString());
        querys.put("toCode", "CNY");

//        try {
//            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
//            JSONObject jb=JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
//
//            //moneyCNY= (String) JSONObject.parseObject(jb.get("showapi_res_body").toString()).get("money");
//            moneyCNY= (String) ((JSONObject)jb.get("showapi_res_body")).get("money");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //设置转账类型为跨境转账
        transfer.setTransType(2);


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
            String dealNo = UUID.randomUUID().toString();
            transfer.setDealNo(dealNo);
            //写入交易记录
            writeDeal(transfer);

            transfer.setCurrency("CNY");
            //transfer.setTransFund(new BigDecimal(moneyCNY));

            transferDao.subMoney(transfer);
            //transferDao.addMoney(transfer); //加钱在境外银行加，不能自己调用
            //转账成功，设置交易状态为“成功”
            transfer.setTransStatus("处理中");

            Map map = new HashMap();
            map.put("dealNo", transfer.getDealNo());
            map.put("transType", transfer.getTransType());
            map.put("transStatus", transfer.getTransStatus());
            map.put("accOut", transfer.getAccOut());
            map.put("accOutName", transfer.getAccOutName());
            map.put("accOutBank", transfer.getAccOutBank());
            map.put("accIn", transfer.getAccIn());
            map.put("accInName", transfer.getAccInName());
            map.put("accInBank", transfer.getAccInBank());
            map.put("currency", transfer.getCurrency());
            map.put("transFund", transfer.getTransFund());
            map.put("kind", transfer.getKind());

            /**
             * 发送消息到队列
             */
            rabbitTemplate.convertAndSend("directExchange", RabbitMqConfig.ROUTINGKEY_B, map);

        } catch (Exception e) {
            //事务回滚
            e.printStackTrace();
            transactionManager.rollback(status);
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
        Account account = new Account();
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
}
