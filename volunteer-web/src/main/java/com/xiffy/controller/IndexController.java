package com.xiffy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiffy.common.Result;
import com.xiffy.entity.*;
import com.xiffy.mapper.ActivityMapper;
import com.xiffy.mapper.NewsMapper;
import com.xiffy.mapper.NoticeMapper;
import com.xiffy.mapper.TypeMapper;
import com.xiffy.service.IActivityService;
import com.xiffy.service.INewsService;
import com.xiffy.service.INoticeService;
import com.xiffy.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private IActivityService activityService;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private INoticeService noticeService;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private INewsService iNewsService;

    @Autowired
    private NewsMapper newsMapper;


    @GetMapping("/notice")
    public Result<List<Notice>> list(){
        List<Notice> list=noticeService.list(new QueryWrapper<Notice>().orderByDesc("createTime").last("limit 3"));
        return Result.success(list);
    }

    @GetMapping("/activity")
    public Result<List<Activity>> actList(){
        List<Activity> list=activityService.list(new QueryWrapper<Activity>().orderByDesc("createTime").last("limit 6").eq("state",0));
        return Result.success(list);

    }

    @GetMapping("/news")
    public  Result<List<Integer>> newsList(){
        List<Integer> list=newsMapper.findId();
        return Result.success(list);
    }


}
