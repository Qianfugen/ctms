package com.zl.service.impl;

import com.zl.dao.CashSweepDao;
import com.zl.dao.TransferDao;
import com.zl.pojo.Account;
import com.zl.pojo.Coll;
import com.zl.pojo.Transfer;
import com.zl.service.CashSweepService;
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
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author fm
 * @version 1.0
 * @date 2019/12/10 13:28
 */
@Service
public class CashSweepServiceImpl implements CashSweepService {
    @Autowired
    private CashSweepDao cashSweepDao;
    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    private TransferService transferService;

    /**
     * 签约时，查询需要签约作为主账号的账户的签约状态，为“已签约”则不可作为主账户签约
     * @param mainAcc 传入需要作为主账户的账号
     * @return 返回该账户的签约状态
     */
    @Override
    public String queryCollStatus(String mainAcc) {
        return cashSweepDao.queryCollStatus(mainAcc);
    }

    /**
     * 资金归集功能签约
     * @param viceAccount  需要签约为副卡的账号
     * @param coll 签约信息
     * @param collStatus 主账号的签约状态
     * @return 返回签约结果 0失败 1成功
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int signColl(Account viceAccount,Coll coll,String collStatus,String signFund) {
        //定义签约状态的字符串
        String collStatus2="已签约";
        String collStatus3="主账号";

        //添加事务管理
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("signColl");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //设置回滚点
        TransactionStatus status = transactionManager.getTransaction(def);

        int flag=1;
        try {
            //如果需要签约为主卡的账号的签约状态不是“已签约”
            if (!collStatus2.equals(collStatus)) {
                //调用处理签约信息对象的方法，获得签约对象
                coll = getColl(coll,viceAccount,signFund);
                //调用添加归集信息对象的方法
                int flag1 = cashSweepDao.addColl(coll);
                //如果添加失败，签约方法直接返回失败
                if(flag1==0){
                    throw new Exception("签约归集信息添加失败！");
                }

                //归集信息添加成功，修改子账号和主账号的签约状态
                //修改子账号的签约状态
                viceAccount.setCollStatus(collStatus2);
                int flag2 = cashSweepDao.updateCollStatus(viceAccount);
                //如果子账号修改签约状态失败，签约方法直接返回失败
                if(flag2==0){
                    throw new Exception("修改签约状态失败！");
                }

                //如果主账号的签约状态不是“主账号”
                if(!collStatus3.equals(collStatus)){
                    //获取主账号
                    String mainAcc=coll.getMainAcc();
                    //修改主账号的签约状态为“主账号”
                    Account mainAccount = getAccount(mainAcc);
                    mainAccount.setCollStatus(collStatus3);
                    int flag3 = cashSweepDao.updateCollStatus(mainAccount);
                    //如果主账号的签约状态修改失败，签约方法直接返回失败
                    if(flag3==0){
                        throw new Exception("主账号的签约状态修改失败！");
                    }
                }
            }

            //如果主账号已签约，签约失败
            if (collStatus2.equals(collStatus)){
                flag=0;
            }
        }catch (Exception e){
            flag=0;
            transactionManager.rollback(status);
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 处理归集信息对象的方法，用于添加归集信息时的信息处理
     * @return 返回归集信息对象
     */
    private Coll getColl(Coll coll,Account viceAccount,String signFund) {
        if(coll.getCollId()==null){

            //测试代码
            String id= String.valueOf(new Random().nextInt(9999));
            coll.setCollId(id);
        }
        coll.setFollowAcc(viceAccount.getAccNo());
        coll.setSignDate(new Date());
        coll.setSignFund(new BigDecimal(signFund));
        return coll;
    }

    private Account getAccount(String accNo){
        Account account=new Account();
        account.setAccNo(accNo);
        return account;
    }

    /**
     * 资金归集功能解约
     * @param viceAccount 需要解约的副卡账号
     * @param mainAccount 主账号
     * @return 返回解约结果 0失败 1 成功
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int cancelColl(Account viceAccount,Account mainAccount) {

        //添加事务管理
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("cancelColl");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //设置回滚点
        TransactionStatus status = transactionManager.getTransaction(def);

        int flag=1;
        try {
            //调用方法删除副卡的归集信息
            int flag1 = cashSweepDao.deleteColl(viceAccount);
            //如果删除归集信息失败，解约方法直接返回失败
            if(flag1==0){
                throw new Exception("删除归集信息失败!");
            }

            //归集信息删除成功，调用方法更改副卡签约状态
            viceAccount.setCollStatus("未签约");
            int flag2 = cashSweepDao.updateCollStatus(viceAccount);
            //如果更改副卡签约状态失败，解约功能直接返回失败
            if(flag2==0){
                throw new Exception("更改副卡签约状态失败!");
            }

            //判断主账号是否还有子账号，需不需要更改主卡的签约状态
            //调用方法查询主卡的子卡信息
            List<Coll> colls = cashSweepDao.queryMainColl(mainAccount);
            //如果没有子卡信息
            if(colls==null||colls.size()==0){
                mainAccount.setCollStatus("未签约");
                int flag3 = cashSweepDao.updateCollStatus(mainAccount);
                //如果更改主卡的签约状态失败，解约方法直接返回失败
                if(flag3==0){
                    throw new Exception("更改主卡的签约状态失败!");
                }
            }
        }catch (Exception e){
            flag=0;
            transactionManager.rollback(status);
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改副卡的归集信息
     * @param reviseColl 传入修改后的归集信息
     * @param viceAccount 子账号
     * @param signFund 签约金额的字符串
     * @param collStatus 修改后的主账户的签约状态
     * @return 返回归集信息修改结果，大于0成功
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int updateColl(Coll reviseColl,Account viceAccount,String signFund,String collStatus) {
        //添加事务管理
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("updateColl");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //设置回滚点
        TransactionStatus status = transactionManager.getTransaction(def);

        //查询要修改的归集信息
        Coll oldColl = cashSweepDao.queryColl(viceAccount);

        //获取修改前后的主账号
        String newMainAcc=reviseColl.getMainAcc();
        String oldMainAcc=oldColl.getMainAcc();

        int flag=1;
        //如果归集信息中的主账号发生了改变
        if(!newMainAcc.equals(oldMainAcc)){
            try {
                //调用解约方法，将当前子账号与之前主账号解约
                Account oldMainAccount=getAccount(oldMainAcc);
                int flag1 = cancelColl(viceAccount, oldMainAccount);
                //如果解约失败，修改方法直接失败
                if(flag1==0){
                    throw new Exception("解约失败!");
                }

                //解约成功，调用签约方法将子账户与新的主账号签约
                int flag2=signColl(viceAccount,reviseColl,collStatus,signFund);
                //如果签约失败，修改方法直接失败
                if(flag2==0){
                    throw new Exception("签约失败!");
                }
            }catch (Exception e){
                flag=0;
                transactionManager.rollback(status);
                e.printStackTrace();
            }
            return flag;
        }

        //如果如果归集信息中的主账号没有改变
        //处理归集信息对象
        reviseColl=getColl(reviseColl,viceAccount,signFund);
        return cashSweepDao.updateColl(reviseColl);
    }

    /**
     * 查询副卡的归集信息
     * @param account 传入账号信息（副卡）
     * @return 返回当前副卡的归集信息
     */
    @Override
    public Coll queryColl(Account account) {
        return cashSweepDao.queryColl(account);
    }

    /**
     * 查询主卡的归集信息（子卡信息）
     * @param account 传入账号信息（主卡）
     * @return 返回主卡下的所有子卡的归集信息
     */
    @Override
    public List<Coll> queryMainColl(Account account) {
        return cashSweepDao.queryMainColl(account);
    }

    /**
     * 查询库中所有的归集信息
     * @return 返回所有的归集信息
     */
    @Override
    public List<Coll> queryAllCollInTable() {
        return cashSweepDao.queryAllCollInTable();
    }

    /**
     * 查询归集记录
     * @param followAcc 传入副卡的信息，以账号和交易类型作为查询条件
     * @return 返回归集转账交易记录
     */
    @Override
    public List<Transfer> queryTransfers(String followAcc) {
        return cashSweepDao.queryTransfers(followAcc);
    }

    /**
     * 资金归集
     * @param colls 归集信息
     * @return 返回归集结果
     */
    @Override
    public void sweepCash(List<Coll> colls) {
        for(Coll coll:colls) {
            //查询子账户余额，判断余额是否超出签约的归集资金
            BigDecimal balance = transferService.queryBalance(coll.getFollowAcc());
            int result = balance.compareTo(coll.getSignFund());

            //如果余额小于等于签约资金，跳过
            if (result <= 0) {
                continue;
            }

            //获取归集信息,并设置到转账交易对象中
            Transfer transfer = new Transfer();
            //转入主账号
            transfer.setAccIn(coll.getMainAcc());
            //从子账号转出
            transfer.setAccOut(coll.getFollowAcc());
            //转出金额
            transfer.setTransFund(balance.subtract(coll.getSignFund()));
            //交易类型
            transfer.setKind("归集");

            //调用转账服务
            transferService.transferMoney(transfer);
        }
    }
}
