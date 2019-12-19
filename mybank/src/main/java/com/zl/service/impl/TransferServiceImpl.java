package com.zl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zl.api.JobAPI;
import com.zl.config.RabbitMqConfig;
import com.zl.dao.TransferDao;
import com.zl.pojo.*;
import com.zl.service.TransferService;
import com.zl.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
    RabbitTemplate rabbitTemplate;
    @Autowired
    private JobAPI jobAPI;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 任务分类，定时转账or立即转账
     */
    @Override
    public int executeJob(Transfer transfer) {
        int flag = 0;
        if ("0".equals(transfer.getKind())) {
            //立即转账
            System.out.println("立即转账。。。");
            if (transfer.getTransType() == 0) {
                //同行转账
                transferMoney(transfer);
            } else if (transfer.getTransType() == 1) {
                //跨行转账
                transferMoneyDome(transfer);
            }
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
            job.setFee(transfer.getFee());
            job.setCron(cron);

            //调用定时任务模块
            jobAPI.insertJob(job);
            flag = 1;
        }
        return flag;
    }

    /**
     * 同行转账
     * 只涉及金额的变动
     *
     * @param transfer 交易对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transferMoney(Transfer transfer) {
        //生成流水号
        String dealNo = Long.toHexString(UUID.randomUUID().getMostSignificantBits()) + Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
        transfer.setDealNo(dealNo);
        String banktype = transfer.getAccIn().substring(0, 6);
        if ("622230".equals(banktype)) {
            //同行转账
            transfer.setKind("同行转账");
            transfer.setTransType(0);
        } else {
            //跨行转账
            transfer.setKind("跨行转账");
            transfer.setTransType(1);
        }
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
//        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        def.setName("SomeTxName");
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        //设置回滚点
//        TransactionStatus status = transactionManager.getTransaction(def);
//        try {
        System.out.println(transfer);
        transferDao.subMoney(transfer);
        transferDao.addMoney(transfer);
        //转账成功，设置交易状态为“成功”
        transfer.setTransStatus("成功");
        //写入交易记录
        int flag = writeDeal(transfer);
        System.out.println("执行结果：" + flag);
        System.out.println("转账成功");
//        } catch (Exception e) {
//            //事务回滚
//            transactionManager.rollback(status);
//            e.printStackTrace();
//        }
    }

    /**
     * 跨境转账
     * <p>
     * 银行自动为客户购外汇汇款
     *
     * @param transfer 交易对象
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void transferMoneyOver(Transfer transfer) {
        transfer.setAccInBank("瑞士银行");
        transfer.setAccInName("张三");

        System.out.println("境外转账service开始");
        System.out.println("transfer" + transfer);
        //汇率转换，计算账户需要扣金额
        String host = "https://ali-waihui.showapi.com";
        String path = "/waihui-transform";
        String method = "GET";
        String appcode = "937341d2258345458f648e962bfb0f81";
        String moneyCNY = null;
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("fromCode", transfer.getCurrency());
        querys.put("money", transfer.getTransFund().toString());
        querys.put("toCode", "CNY");

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            JSONObject jb = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));

            moneyCNY = (String) ((JSONObject) jb.get("showapi_res_body")).get("money");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("moneyCNY:" + moneyCNY);
        BigDecimal balance = queryBalance(transfer.getAccOut());
        BigDecimal transMoney = new BigDecimal(moneyCNY);


        //添加事务管理
//        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        def.setName("SomeTxName");
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        //设置回滚点
//        TransactionStatus status = transactionManager.getTransaction(def);
//        try {
            //生成交易流水号,32位随机不重复
            String dealNo = Long.toHexString(UUID.randomUUID().getMostSignificantBits()) + Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
            transfer.setDealNo(dealNo);
            //跨境转账记录状态位处理中
            transfer.setTransStatus("0");
            //设置转账类型为跨境转账，controller已设置
//                transfer.setTransType(2);
            transfer.setKind("跨境转账");

            //收入行信息
//                String accIn = transfer.getAccIn();
//                Map<String, String> mapIn = queryBankAndUserName(accIn);
//                transfer.setAccInName(mapIn.get("userName"));
//                transfer.setAccInBank(mapIn.get("bankName"));
            //转出行信息
            String accOut = transfer.getAccOut();
            Map<String, String> mapOut = queryBankAndUserName(accOut);
            transfer.setAccOutName(mapOut.get("userName"));
            transfer.setAccOutBank(mapOut.get("bankName"));

            /**
             * 把交易记录放到Map中准备发送到消息队列
             */
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
            System.out.println("开始插入transfer" + transfer);
            List<Transfer> overDealing = (List<Transfer>) redisTemplate.opsForList().leftPop("overDealing");
            if (overDealing == null) {
                overDealing = transferDao.queryAllOverDealing();
                if (overDealing == null) {
                    overDealing = new ArrayList<Transfer>();
                }
            }
            overDealing.add(transfer);
            redisTemplate.opsForList().leftPush("overDealing", overDealing);
            int flag = writeDeal(transfer);
            System.out.println("插入完毕。。。" + flag);
            /**
             * 发送消息到队列
             */
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.convertAndSend("directExchange", RabbitMqConfig.ROUTINGKEY_B, map);
            System.out.println("跨境转账处理中，发送消息到境外银行。。。。");
//                TransactionStatus status2 = transactionManager.getTransaction(def);
//                transactionManager.commit(status2);
//        } catch (Exception e) {
//            //事务回滚
//            transactionManager.rollback(status);
//            e.printStackTrace();
//        }


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void transferMoneyDome(Transfer transfer) {
        //调用api
        transfer.setAccInBank("瑞士银行");
        transfer.setAccInName("张三");

        System.out.println("跨行转账service开始");
        System.out.println("transfer" + transfer);

        //判断余额是否充足
        if (queryBalance(transfer.getAccOut()).compareTo(transfer.getTransFund()) > 0) {
//            //添加事务管理
//            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//            def.setName("SomeTxName");
//            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//            //设置回滚点
//            TransactionStatus status = transactionManager.getTransaction(def);
//            try {
            //生成交易流水号,32位随机不重复
            String dealNo = Long.toHexString(UUID.randomUUID().getMostSignificantBits()) + Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
            transfer.setDealNo(dealNo);
            //跨境转账记录状态位处理中
            transfer.setTransStatus("0");
            //设置转账类型为跨境转账，controller已设置
//                transfer.setTransType(2);
            transfer.setKind("跨行转账");

            //收入行信息
//                String accIn = transfer.getAccIn();
//                Map<String, String> mapIn = queryBankAndUserName(accIn);
//                transfer.setAccInName(mapIn.get("userName"));
//                transfer.setAccInBank(mapIn.get("bankName"));
            //转出行信息
            String accOut = transfer.getAccOut();
            Map<String, String> mapOut = queryBankAndUserName(accOut);
            transfer.setAccOutName(mapOut.get("userName"));
            transfer.setAccOutBank(mapOut.get("bankName"));
            System.out.println("跨行转账transfer:" + transfer);

            /**
             * 把交易记录放到Map中准备发送到消息队列
             */
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
             * 减钱
             */
            transferDao.subMoney(transfer);

            /**
             * 写入交易记录
             */
            System.out.println("开始插入transfer" + transfer);
            List<Transfer> domeDealing = (List<Transfer>) redisTemplate.opsForList().leftPop("domeDealing");
            if (domeDealing == null) {
                domeDealing = transferDao.queryAllDomeDealing();
                if (domeDealing == null) {
                    domeDealing = new ArrayList<Transfer>();
                }
            }
            domeDealing.add(transfer);
            redisTemplate.opsForList().leftPush("domeDealing", domeDealing);
            int flag = writeDeal(transfer);
            System.out.println("插入完毕。。。" + flag);
            /**
             * 发送消息到队列
             */
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.convertAndSend("directExchange", RabbitMqConfig.ROUTINGKEY_A, map);
            System.out.println("跨行转账处理中，发送消息到国内其他银行。。。。");
//                TransactionStatus status2 = transactionManager.getTransaction(def);
//                transactionManager.commit(status2);
//            } catch (Exception e) {
//                //事务回滚
//                transactionManager.rollback(status);
//                e.printStackTrace();
//            }
        } else {
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
     * 查询所有境外转账未完成记录
     *
     * @return
     */
    @Override
    public List<Transfer> queryAllOverDealing() {
        return transferDao.queryAllOverDealing();
    }

    /**
     * 查询所有跨行转账未完成记录
     *
     * @return
     */
    @Override
    public List<Transfer> queryAllDomeDealing() {
        return transferDao.queryAllDomeDealing();
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
        /**
         * 自动发送跨境转账未完成记录消息
         */
        List<Transfer> overDealing = (List<Transfer>) redisTemplate.opsForList().leftPop("overDealing");
        if (overDealing == null) {
            overDealing = transferDao.queryAllOverDealing();
            redisTemplate.opsForList().leftPush("overDealing", overDealing);
        }
        if (overDealing != null && overDealing.size() > 0) {
            for (Transfer transfer : overDealing) {
                System.out.println(transfer);
                /**
                 * 把交易记录放到Map中准备发送到消息队列
                 */
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
                //发送消息到消息队列
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.convertAndSend("directExchange", RabbitMqConfig.ROUTINGKEY_B, map);
            }
        }

        /**
         * 自动发送跨行转账未完成记录消息
         */
        List<Transfer> domeDealing = (List<Transfer>) redisTemplate.opsForList().leftPop("domeDealing");
        if (domeDealing == null) {
            domeDealing = transferDao.queryAllDomeDealing();
            redisTemplate.opsForList().leftPush("domeDealing", domeDealing);
        }
        if (domeDealing != null && domeDealing.size() > 0) {
            for (Transfer transfer : domeDealing) {
                System.out.println(transfer);
                /**
                 * 把交易记录放到Map中准备发送到消息队列
                 */
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
                //发送消息到消息队列
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.convertAndSend("directExchange", RabbitMqConfig.ROUTINGKEY_A, map);
            }
        }
    }

    /**
     * 查询最大上限
     *
     * @param accNo
     * @return
     */
    @Override
    public BigDecimal queryAccLimit(String accNo) {
        return transferDao.queryAccLimit(accNo);
    }

    /**
     * 查询启用状态
     *
     * @param accNo
     * @return
     */
    @Override
    public int queryAccStatus(String accNo) {
        return transferDao.queryAccStatus(accNo);
    }

    /**
     * 转账前的验证
     *
     * @param transfer
     * @param bank     status  0 冻结，1 定时任务取消成功，2 受理中， 100 超出上限，200 转账成，400 余额不足
     *                 手续费
     *                 同行   免费
     *                 跨行   <5000：免费 ；5000-10000：5元/笔；10000-50000：7.5元/笔
     *                 跨境   汇款金额的0.08%,最低40元/笔,最高208元/笔
     * @return
     */
    @Override
    public Map<String, Integer> verifyTransfer(Transfer transfer, String bank) {
        Map<String, Integer> map = new HashMap<>();
        //手续费
        BigDecimal fee = BigDecimal.valueOf(0.00);
        //判断是否冻结
        if (queryAccStatus(transfer.getAccOut()) != 0) {
            //没冻结
            if ("0".equals(bank)) {
                //境内转账
                String banktype = transfer.getAccIn().substring(0, 6);
                System.out.println("银行前六位：" + banktype);
                if ("622230".equals(banktype)) {
                    //同行转账
                    //判断上限
                    BigDecimal limit = queryAccLimit(transfer.getAccOut());
                    //判断余额
                    BigDecimal balance = queryBalance(transfer.getAccOut());
                    System.out.println("balance: " + balance + " accOut: " + transfer.getAccOut());
                    if (limit.compareTo(transfer.getTransFund()) < 0) {
                        //超过上限
                        map.put("status", 100);
                    } else if (balance.compareTo(transfer.getTransFund()) >= 0) {
                        //设置手续费
                        transfer.setFee(fee);
                        System.out.println("同行转账,手续费：" + fee + "元");
                        transfer.setTransType(0);
                        transfer.setCurrency("CNY");
                        int flag = executeJob(transfer);
                        if (flag > 0) {
                            //定时转账
                            map.put("status", 2);
                        } else {
                            map.put("status", 200);
                        }
                    } else {
                        //余额不足
                        map.put("status", 400);
                    }
                } else {
                    //跨行转账
                    //判断上限
                    BigDecimal limit = queryAccLimit(transfer.getAccOut());
                    //查询余额
                    BigDecimal balance = queryBalance(transfer.getAccOut());
                    //计算手续费
                    if (transfer.getTransFund().compareTo(BigDecimal.valueOf(5000.00)) <= 0) {
                        fee = BigDecimal.valueOf(0.00);
                    } else if (transfer.getTransFund().compareTo(BigDecimal.valueOf(10000.00)) <= 0) {
                        fee = BigDecimal.valueOf(5.00);
                    } else {
                        fee = BigDecimal.valueOf(7.50);
                    }
                    //设置手续费
                    transfer.setFee(fee);
                    if (limit.compareTo(transfer.getTransFund()) < 0) {
                        //超过上限
                        map.put("status", 100);
                    } else if (balance.compareTo(transfer.getTransFund().add(fee)) >= 0) {
                        System.out.println("跨行转账,收取手续费：" + fee + "元");
                        //executeJob(transfer);
                        //跨行转账方法
                        transfer.setTransType(1);
                        transfer.setCurrency("CNY");
                        int flag = executeJob(transfer);
                        if (flag > 0) {
                            //定时转账
                            map.put("status", 2);
                        } else {
                            map.put("status", 200);
                        }
                    } else {
                        //余额不足
                        map.put("status", 400);
                    }
                }
            } else {
                //跨境转账
                //判断上限
                BigDecimal limit = queryAccLimit(transfer.getAccOut());
                //查询余额
                BigDecimal balance = queryBalance(transfer.getAccOut());
                System.out.println("余额" + balance);
                //计算手续费
                fee = transfer.getTransFund().multiply(BigDecimal.valueOf(0.0008));
                if (fee.compareTo(BigDecimal.valueOf(40.00)) < 0) {
                    fee = BigDecimal.valueOf(40.00);
                } else if (fee.compareTo(BigDecimal.valueOf(208.00)) > 0) {
                    fee = BigDecimal.valueOf(208.00);
                }
                //设置手续费
                transfer.setFee(fee);
                if (limit.compareTo(transfer.getTransFund()) < 0) {
                    //超过上限
                    map.put("status", 100);
                } else if (balance.compareTo(transfer.getTransFund().add(fee)) >= 0) {
                    System.out.println("跨境转账,收取手续费：" + fee + "元");
                    //executeJob(transfer);
                    transfer.setTransType(2);
                    transferMoneyOver(transfer);
                    map.put("status", 2);
                } else {
                    //余额不足
                    map.put("status", 400);
                }
            }
        } else {
            map.put("status", 0);
        }
        return map;
    }

    @Override
    public List<UsualColl> queryCusUsual(String accNo) {
        return transferDao.queryCusUsual(accNo);
    }
}
