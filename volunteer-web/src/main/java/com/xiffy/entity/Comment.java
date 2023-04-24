package com.xiffy.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("t_comment")
@Data
public class Comment {
    private Integer id;

    private Integer userId;

    @TableField(select = false)
    private Integer sex;

    private Integer newsId;

    private String content;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; //发布时间

    @TableField(select = false)
    private String userName;

}
