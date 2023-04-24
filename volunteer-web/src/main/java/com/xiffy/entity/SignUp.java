package com.xiffy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("t_signup")
@Data
public class SignUp {
    private Integer id;

    private Integer userId;

    private Integer activityId;

    @TableField(select = true,exist = false)
    private String content;//活动内容

    @TableField(select = true,exist = false)
    private String address;//活动地址

    @TableField(select = true,exist = false)
    private String leaderName;//负责人


    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; // 报名日期

    @TableField(select = true,exist = false)
    private String username;

    //活动名称
    @TableField(select = true,exist = false)
    private String name;

    @TableField(select = true,exist = false)
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date actTime;//活动时间

    @TableField(select = true,exist = false)
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;//截止时间

    @TableField(select = true,exist = false)
    private Double duration;//志愿时长

    private Double dur;//志愿时长

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date checkTime;//审核时间

    private Double credit;//阳光信用

    private Integer scheck;//0-未通过审核 1-通过审核 2-初始态 3-人工审核


}
