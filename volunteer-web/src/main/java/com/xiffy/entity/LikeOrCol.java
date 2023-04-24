package com.xiffy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("t_news_likeorcol")
@Data
public class LikeOrCol {
    private Integer id;

    private Integer newsId;

    private Integer userId;

    private Integer type;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; //创建时间



}
