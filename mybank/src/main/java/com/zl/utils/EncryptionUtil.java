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
     * 加密
     * @param pwd
     * @return 密码盐与加密密码的map
     */
    public Map<String,String> encryption(String pwd){
        //生成密码盐,32位字符串
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        String password = new Md5Hash(pwd,salt,2).toString();
        Map<String,String > map = new HashMap<>();
        map.put("salt",salt);
        map.put("password",password);
        return map;
    }
}
