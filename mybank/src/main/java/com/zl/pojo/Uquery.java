package com.zl.pojo;

import org.bouncycastle.math.ec.custom.sec.SecT409Field;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Uquery implements Serializable {
    //搜索收款人名字
    private String qAccInName;
    //收款人账号
    private String qAccIn;
    //自己賬戶
    private String qMainAcc;

    public String getqAccInName() {
        return qAccInName;
    }

    public void setqAccInName(String qAccInName) {
        this.qAccInName = qAccInName;
    }

    public String getqAccIn() {
        return qAccIn;
    }

    public void setqAccIn(String qAccIn) {
        this.qAccIn = qAccIn;
    }

    public String getqMainAcc() {
        return qMainAcc;
    }

    public void setqMainAcc(String qMainAcc) {
        this.qMainAcc = qMainAcc;
    }

    @Override
    public String toString() {
        return "Uquery{" +
                "qAccInName='" + qAccInName + '\'' +
                ", qAccIn='" + qAccIn + '\'' +
                ", qMainAcc='" + qMainAcc + '\'' +
                '}';
    }
}