package com.zl.pojo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收款
 * @author root
 */
@Component
public class Payee implements Serializable {
    /**
     * 贷方账号
     */
    private String creditorAcc;
    /**
     * 贷方姓名
     */
    private String creditorName;
    /**
     * 借方账号
     */
    private String debtor;
    /**
     * 借方姓名
     */
    private String debtorName;
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

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getDebtor() {
        return debtor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
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
                ", creditorName='" + creditorName + '\'' +
                ", debtor='" + debtor + '\'' +
                ", debtorName='" + debtorName + '\'' +
                ", fund=" + fund +
                '}';
    }
}
