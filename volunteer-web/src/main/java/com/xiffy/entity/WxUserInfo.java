package com.xiffy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("t_wxuserinfo")
@Data
public class WxUserInfo implements Serializable {

    private Integer id; // 用户编号

    private String openid; // 用户唯一标识

    private String sno;//学号

    private String userName; // 用户姓名

    private double duration=0;//志愿时长 默认为0

    private String phone;//电话

    private String email;//邮箱

    private String college;//学院

    private String major;//专业

    private String classinfo;//班级

    private int sex;//性别

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date registerDate; // 注册日期

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastLoginDate; // 最后登录日期

    @TableField(select = false,exist = false)
    private String code; // 微信用户code 前端传来的

    private double credit;//阳光信用


}
