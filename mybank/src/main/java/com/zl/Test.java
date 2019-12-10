package com.zl;

import com.zl.pojo.Transfer;
import com.zl.service.impl.TransferServiceImpl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Test {
    public static void main(String[] args) {
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        Transfer transfer=new Transfer();
//        transfer.setDealNo("4D5DECCEE3DD46C396BBC2728BB53987");
//        try {
//            transfer.setDealDate(sdf.parse("1972-12-03 00:00:00"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        transfer.setTransType(0);
//        transfer.setTransStatus("成功");
//        transfer.setAccOutName("杨树安");
//        transfer.setCurrency("人民币");
//
//        transfer.setAccIn("6222305134942173154");
//        transfer.setAccOut("6222306645598761176");
//        transfer.setTransFund(BigDecimal.valueOf(1));
//        TransferServiceImpl ts=new TransferServiceImpl();
//        int flag=ts.addMoney(transfer);
//        System.out.println(flag);

        TransferServiceImpl ts=new TransferServiceImpl();
        Transfer transfer=ts.queryTransferByDealNo("4D5DECCEE3DD46C396BBC2728BB53987");
        System.out.println(transfer);
    }
}
