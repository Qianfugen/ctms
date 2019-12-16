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

/**
 * 转账service层实现类
 *
 * @author root
 */
@Service
public class TransferServiceImpl implements TransferService {
    @Override
    public int transferMoney(Transfer transfer) {
        System.out.println("调用dao层。。。");
        return transferDao.addMoney(transfer);
    }

    @Autowired
    private TransferDao transferDao;
    @Autowired
    private DataSourceTransactionManager transactionManager;


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
     * 处理消息
     *
     * @param transfer
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int processMessage(Transfer transfer) {
        System.out.println("开始处理消息");
        int flag=-1;
        //添加事务管理
//        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        def.setName("SomeTxName");
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        //设置回滚点
//        TransactionStatus status = transactionManager.getTransaction(def);
        System.out.println(transfer);
        int addOK=transferMoney(transfer);
        System.out.println(addOK);
        try {
            System.out.println("addOK:"+addOK);
            if(addOK>0){
                System.out.println("加钱成功！");
                transfer.setTransStatus("1");
                writeDeal(transfer);
                System.out.println("写入记录成功！");
                flag=1;
            }
        }catch (Exception e){
            e.printStackTrace();
//            transactionManager.rollback(status);
        }
        return flag;
    }
}
