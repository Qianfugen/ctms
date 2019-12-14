package com.zl.config;

import com.zl.pojo.User;
import com.zl.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录认证类
 * @author 徐浩杰
 * @version 1.0 2019-12-12
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService us;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 登录认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户输入的账号
        String loginAccNo = (String) token.getPrincipal();
        System.out.println("token:"+token);
        User user = us.queryUserByAccNo(loginAccNo);
        if(user == null){
            throw new UnknownAccountException();
        }
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("loginAccNO", loginAccNo);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                loginAccNo,
                user.getUserPwd(),
                ByteSource.Util.bytes(loginAccNo),//盐值
                getName()//realm的name
        );
        return authenticationInfo;
    }
}
