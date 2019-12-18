package com.zl;

import com.zl.service.TransferService;
import com.zl.service.impl.TransferServiceImpl;

public class Test {
    public static void main(String[] args) {
        TransferService transferService =new TransferServiceImpl();
        Boolean flag=transferService.checkUser("伍佳渊","6222301166861586118");
        System.out.println(flag);
    }
}
