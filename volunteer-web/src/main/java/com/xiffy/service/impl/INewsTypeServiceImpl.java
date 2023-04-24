package com.xiffy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiffy.entity.NewsType;
import com.xiffy.entity.Type;
import com.xiffy.mapper.NewsTypeMapper;
import com.xiffy.mapper.TypeMapper;
import com.xiffy.service.INewsTypeService;
import com.xiffy.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 新闻类型Service实现类

 */
@Service("newstypeService")
public class INewsTypeServiceImpl extends ServiceImpl<NewsTypeMapper, NewsType> implements INewsTypeService {

    @Autowired
    private NewsTypeMapper newsTypeMapper;
}
