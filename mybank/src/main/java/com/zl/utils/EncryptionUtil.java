package com.zl.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.HashMap;
import java.util.Map;

/**
 * 加密工具类
 * @author 徐浩杰
 * @version 1.0 2019-12-12
 */
public class EncryptionUtil {

    /**
     * 加密和密码盐
     * @param accNo 将卡号作为密码盐
     * @param pwd 未加密密码
     * @return
     */
    public Map<String,String> encryption(String accNo,String pwd){
        //卡号作为密码盐,32位字符串
        String password = new Md5Hash(pwd,accNo,2).toString();
        Map<String,String > map = new HashMap<>();
        map.put("accNo",accNo);
        map.put("password",password);
        return map;
    }
}
