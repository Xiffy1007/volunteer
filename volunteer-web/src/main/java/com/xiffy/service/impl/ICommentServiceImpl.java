package com.xiffy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiffy.entity.Comment;
import com.xiffy.mapper.CommentMapper;
import com.xiffy.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 评论Service实现类

 */
@Service("acommentService")
public class ICommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;
}
