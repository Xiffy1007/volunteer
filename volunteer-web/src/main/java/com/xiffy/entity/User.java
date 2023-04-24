package com.xiffy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer signupId;
    private Integer id;
    private String userName; // 用户姓名
    private String major;//专业
    private String classinfo;//班级
    private String phone;//电话
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; // 报名日期

}
