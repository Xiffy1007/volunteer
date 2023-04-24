package com.xiffy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@TableName("t_admin")
@Data
public class Admin {
    private Integer id;

    private String adminName;//昵称

    private String password;//密码

    private String sex;//性别

    private String phone;//手机

    private String email;//邮箱

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastLoginDate; // 最后登录日期

    @TableField(select = true,exist = false)
    private String newPassword ;
}
