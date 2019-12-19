package com.zl.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-12
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "shirFilter")
    public ShiroFilterFactoryBean factoryBean(SecurityManager securityManager) {
        System.out.println("进入shiroConfig");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //设置登录时提交url
        shiroFilterFactoryBean.setLoginUrl("/user/login");
        //登录成功后跳转的url
        shiroFilterFactoryBean.setSuccessUrl("/user/toIndex");
        //未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/user/notLogin");
        //拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //anon:所有url可以匿名访问
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/user/toLogin","anon");
        filterChainDefinitionMap.put("/user/toRegister","anon");
        filterChainDefinitionMap.put("/user/regName","anon");
        filterChainDefinitionMap.put("/transfer/transferMoney","anon");
        //authc:所有url都必须认证通过才可以访问
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 匹配MD5加密后的密码
     * 密码校验给予shiro的SimpleAuthenticationInfo处理
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列次数2次，相当于md5(md5(""))
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    @Bean
    public MyRealm myRealm() {
        MyRealm myRealm = new MyRealm();
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myRealm;
    }

    /**
     * shiro接口
     */
    @Bean(name = "securityManager")
    public SecurityManager securityManager() {
        //SecurityManager安全管理器
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm());
        return securityManager;
    }

}
