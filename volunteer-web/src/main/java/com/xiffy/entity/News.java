package com.xiffy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("t_news")
@Data
public class News {
    private String name;//标题

    private Integer id;

    private String content;//内容

    private Integer adminId;//作者

    private Integer typeId;

    private Integer likeNum;//点赞数

    private Integer colNum;//收藏数

    private Integer comNum;//评论数

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; // 创建日期

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime; // 修改日期

    private String fileName;//封面图

    private String originalName;
}
