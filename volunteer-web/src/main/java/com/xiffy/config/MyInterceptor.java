package com.xiffy.config;

import com.xiffy.common.Common;
import com.xiffy.entity.Admin;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Admin admin=(Admin) request.getSession().getAttribute(Common.ADMIN_INFO);
        if(admin==null){
            response.sendRedirect("/end/page/login.html");
            return false;
        }
        return true;
    }
}
