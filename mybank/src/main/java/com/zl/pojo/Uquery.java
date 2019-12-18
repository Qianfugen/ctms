package com.zl.pojo;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Uquery implements Serializable {
    //搜索收款人名字
    private String qAccInName;
    //收款人账号
    private String qAccIn;

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
}