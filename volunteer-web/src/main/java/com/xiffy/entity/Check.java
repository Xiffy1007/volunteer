package com.xiffy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("t_check")
@Data
public class Check {
    private Integer id;

    private Integer userId;

    @TableField(select = true,exist = false)
    private String userName; // 用户姓名
    @TableField(select = true,exist = false)
    private String major;//专业
    @TableField(select = true,exist = false)
    private String classinfo;//班级
    @TableField(select = true,exist = false)
    private String phone;//电话

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; //创建时间

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date checkTime; //审核时间

    private Integer state;//审核状态

    private double duration;//志愿时长

    private String images;//图片

    private String content;

    private String question;
}
