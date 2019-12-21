package com.zl.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收款
 *
 * @author root
 */
public class Payee implements Serializable {
    /**
     * 贷方账号
     */
    private String creditorAcc;
    /**
     * 借方账号
     */
    private String debtor;
    /**
     * 借贷金额
     */
    private BigDecimal fund;

    public String getCreditorAcc() {
        return creditorAcc;
    }

    public void setCreditorAcc(String creditorAcc) {
        this.creditorAcc = creditorAcc;
    }

    public String getDebtor() {
        return debtor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }

    public BigDecimal getFund() {
        return fund;
    }

    public void setFund(BigDecimal fund) {
        this.fund = fund;
    }

    @Override
    public String toString() {
        return "Payee{" +
                "creditorAcc='" + creditorAcc + '\'' +
                ", debtor='" + debtor + '\'' +
                ", fund=" + fund +
                '}';
    }
}
