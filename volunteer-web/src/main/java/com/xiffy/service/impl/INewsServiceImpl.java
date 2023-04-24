package com.xiffy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiffy.entity.News;
import com.xiffy.mapper.NewsMapper;
import com.xiffy.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 新闻Service实现类

 */
@Service("newsService")
public class INewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

    @Autowired
    private NewsMapper newsMapper;
}
