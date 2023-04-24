package com.xiffy.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiffy.entity.Activity;
import com.xiffy.mapper.ActivityMapper;
import com.xiffy.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.swing.event.ListDataEvent;
import java.util.Date;
import java.util.List;

@Component
public class Job {

    @Autowired
    private IActivityService activityService;

    @Autowired
    private ActivityMapper activityMapper;


/*
    //表示每隔5min 300000
    @Scheduled(fixedRate = 3000)
    public void Doing() {
        UpdateWrapper<Activity> wrapper=new UpdateWrapper<>();
        Date date=new Date();
        wrapper.le("endTime",date);//现在的时间大于等于活动报名截止时间
        wrapper.ge("actTime",date);//现在的时间小于等于活动开始时间
        wrapper.eq("state",0);
        wrapper.set("state",3);
        activityService.update(wrapper);
        System.out.println("fixedRate " + new Date());
    }

 */




}
