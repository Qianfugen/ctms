package com.zl.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import javax.servlet.http.HttpServletRequest;

public class LoginFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 4;
    }

    @Override
    public boolean shouldFilter() {
        /*
         * RequestContext是所有服务共享的一个对象
         */
        RequestContext rc= RequestContext.getCurrentContext();
        HttpServletRequest request=rc.getRequest();
        String path=request.getServletPath();
        //rc.setSendZuulResponse(false);
        String loginAccNo=(String) request.getSession().getAttribute("loginAccNo");
        if(loginAccNo!=null || path.matches("/mybank/user/toLogin*")){
            return false;
        }
        //如果需要拦截那么返回true,否则返回false
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext rc=RequestContext.getCurrentContext();
        System.out.println("请求不通过......................");
        rc.setSendZuulResponse(false);//请求不通过
        rc.setResponseStatusCode(401);//用户没有登录
        return null;
    }
}
