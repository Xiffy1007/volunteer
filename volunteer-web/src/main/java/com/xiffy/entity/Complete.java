package com.xiffy.entity;

import lombok.Data;

@Data
public class Complete {
    Integer id;//种类id
    String name;//活动种类
    Integer cyNum;//参与人数
    Integer comNum;//完成人数
    Double percent;//完成度
    String classinfo;//班级
}
