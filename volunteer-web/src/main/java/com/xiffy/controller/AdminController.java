package com.xiffy.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiffy.common.Common;
import com.xiffy.common.Result;
import com.xiffy.common.ResultCode;
import com.xiffy.entity.Admin;
import com.xiffy.entity.News;
import com.xiffy.exception.CustomException;
import com.xiffy.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IAdminService iAdminService;

    //管理员登录
    @PostMapping("/login")
    public Result<Admin> login(@RequestBody Admin admin, HttpServletRequest request){
        if (StrUtil.isBlank(admin.getAdminName())||StrUtil.isBlank(admin.getPassword())) {
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
        //从数据库查询账号密码是否正确，放到session

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("adminName", admin.getAdminName());
        Admin login= iAdminService.getOne(wrapper);
        if(login==null){//如果未查到 则该用户不存在
            throw new CustomException(ResultCode.USER_NOT_EXIST_ERROR);
        }else{
            wrapper.eq("password", SecureUtil.md5(admin.getPassword()));
           // wrapper.eq("password", admin.getPassword());
            login= iAdminService.getOne(wrapper);
            if(login==null){
                throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
            }

        }
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("id", login.getId());
        updateWrapper.set("lastLoginDate",new Date());
        iAdminService.update(updateWrapper);
        HttpSession session = request.getSession();
        session.setAttribute(Common.ADMIN_INFO, login);
        session.setMaxInactiveInterval(60*60*24);//过期时间
        return Result.success(login);
    }

    //管理员退出
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().setAttribute(Common.ADMIN_INFO, null);
        return Result.success();
    }

    /**
     *管理员修改密码
     */

    @PutMapping("/updatePasword")
    public Result updatePasword( @RequestBody Admin admin,HttpServletRequest request) {
        Object admin1 = request.getSession().getAttribute(Common.ADMIN_INFO);
        if (admin1 == null) {//如果内存中没有信息则未登录
            return Result.error("401", "未登录");
        }
        Admin admininfo = (Admin) admin1;
        String oldPassword = SecureUtil.md5(admin.getPassword());//获取旧密码
        if (!oldPassword.equals(admininfo.getPassword())) {//如果输入的旧密码和数据库中的密码不匹配
            return Result.error(ResultCode.USER_ACCOUNT_ERROR.code, ResultCode.USER_ACCOUNT_ERROR.msg);
        }
        //如果旧密码输入正确
        admininfo.setPassword(SecureUtil.md5(admin.getNewPassword()));
        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.eq("id",admininfo.getId());
        iAdminService.update(admininfo,wrapper);
        //清空session，让用户重新登录
        request.getSession().setAttribute(Common.ADMIN_INFO, null);
        return Result.success();
    }


    @PutMapping("/modify")
    public Result<Admin> update(@RequestBody Admin admin,HttpServletRequest request){
        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.eq("id",admin.getId());
        iAdminService.update(admin,wrapper);
        Admin login= iAdminService.getOne(new QueryWrapper<Admin>().eq("id", admin.getId()));
        HttpSession session = request.getSession();
        session.setAttribute(Common.ADMIN_INFO, login);
        return Result.success(login);
    }
}
