package com.xiffy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("t_credit")
@Data
public class Credit {
    private Integer id;

    private Integer userId;

    private Integer activityId;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; //创建时间

    private double data;

}
