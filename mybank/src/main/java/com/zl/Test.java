package com.zl;

import com.zl.pojo.Transfer;
import com.zl.service.impl.TransferServiceImpl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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

//        TransferServiceImpl ts=new TransferServiceImpl();
//        Transfer transfer=ts.queryTransferByDealNo("4D5DECCEE3DD46C396BBC2728BB53987");
//        System.out.println(transfer);

//        String[] uuids = null;
//        StringBuffer uuid = new StringBuffer();
//        long date1 = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            uuid.setLength(0);
//            uuids = UUID.randomUUID().toString().split("-");
//            for (String str : uuids) {
//                uuid.append(str);
//            }
//        }
//        long date2 = System.currentTimeMillis();
//        System.out.println("使用时间" + (date2 - date1) + "ms");
//
//        System.out.println("******************************************");
//
//        char[] dest = new char[32];
//        long date3 = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//
//            char[] src = UUID.randomUUID().toString().toCharArray();
//            System.arraycopy(src, 0, dest, 0, 8);
//            System.arraycopy(src, 9, dest, 8, 4);
//            System.arraycopy(src, 14, dest, 12, 4);
//            System.arraycopy(src, 19, dest, 16, 4);
//            System.arraycopy(src, 24, dest, 20, 12);
//        }
//        long date4 = System.currentTimeMillis();
//        System.out.println("使用时间" + (date4 - date3) + "ms");
//
//        System.out.println("******************************************");
//
//
//        String name = null;
//        long date5 = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            name = Long.toHexString(UUID.randomUUID().getMostSignificantBits()) + Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
//        }
//        long date6 = System.currentTimeMillis();
//        System.out.println("使用时间" + (date6 - date5) + "ms");

    }
}
