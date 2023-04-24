package com.xiffy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiffy.entity.LikeOrCol;
import com.xiffy.mapper.LikeOrColMapper;
import com.xiffy.service.ILikeOrColService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 新闻点赞收藏Service实现类

 */
@Service("likeorcolService")
public class ILikeOrColServiceImpl extends ServiceImpl<LikeOrColMapper, LikeOrCol> implements ILikeOrColService {

    @Autowired
    private LikeOrColMapper likeOrColMapper;
}
