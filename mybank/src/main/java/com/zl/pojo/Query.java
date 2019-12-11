package com.zl.pojo;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 收款人分页查询的条件
 * @author 徐浩杰
 * @version 1.0 2019-12-10
 */
@Component
public class Query implements Serializable {
    /**
     * 贷方账号
     */
    private String creditorAcc;
    /**
     * 借方账号
     */
    private String debtor;
    /**
     *借方用户名
     */
    private String debtorName;

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

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    @Override
    public String toString() {
        return "Query{" +
                "creditorAcc='" + creditorAcc + '\'' +
                ", debtor='" + debtor + '\'' +
                ", debtorName='" + debtorName + '\'' +
                '}';
    }
}
