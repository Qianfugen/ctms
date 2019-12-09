package com.zl.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author junqi
 * 常用收款人
 */
public class UsualColl implements Serializable {
    /**
     * 自己账号
     */
    private String mainAcc;
    /**
     * 收款人账号
     */
    private String accIn;
    /**
     * 收款人用户名
     */
    private String accInName;
    /**
     * 收款金额
     */
    private BigDecimal transFund;

}
