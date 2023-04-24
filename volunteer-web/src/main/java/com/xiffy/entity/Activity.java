package com.xiffy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.javafx.beans.IDProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("t_activity")
@Data
public class Activity {

    private Integer id;

    private String name;//活动名称

    private String content;//活动内容

    private Integer number;//活动人数

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; //发布时间

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;//截止时间

    private String leaderName;//负责人

    private String leaderContact;//联系方式

    private Double duration;//志愿时长

    private String address;//活动地址

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date actTime;//活动时间

    private Integer typeId;//活动类型

    @TableField(select = true,exist = false)
    private String typeName;//活动类型名字

    private Integer state;//活动状态

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;//修改时间

    private Integer nowNum;//报名人数

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date actendTime;//活动结束时间

    private Double lat;//维度

    private Double lng;//经度
}
