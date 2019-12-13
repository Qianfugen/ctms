package com.zl.service.impl;

import com.zl.dao.TransferDao;
import com.zl.pojo.Account;
import com.zl.pojo.Transfer;
import com.zl.pojo.User;
import com.zl.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
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
}
