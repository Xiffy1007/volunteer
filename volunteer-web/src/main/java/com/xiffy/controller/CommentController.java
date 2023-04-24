package com.xiffy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiffy.common.Result;
import com.xiffy.entity.*;
import com.xiffy.mapper.CommentMapper;
import com.xiffy.mapper.LikeOrColMapper;
import com.xiffy.mapper.NewsMapper;
import com.xiffy.service.ICommentService;
import com.xiffy.service.ILikeOrColService;
import com.xiffy.service.INewsService;
import com.xiffy.service.IWxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IWxUserInfoService wxUserInfoService;

    @Autowired
    private INewsService iNewsService;
    @Autowired
    private LikeOrColMapper likeOrColMapper;

    @Autowired
    private ILikeOrColService likeOrColService;

    @Autowired
    private NewsMapper newsMapper;


    //分页查询
    @GetMapping("/page/{name}")
    public Result<PageInfo<Comment>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "15") Integer pageSize,
                                          @PathVariable String name) {
        PageHelper.startPage(pageNum, pageSize);
        if (name.equals("all")) {//查询所有
            List<Comment> list = commentService.list(new QueryWrapper<Comment>().orderByDesc("createTime"));
            return Result.success(PageInfo.of(list));

        } else {
            List<Comment> list = commentService.list(new QueryWrapper<Comment>().like("content", name).orderByDesc("createTime"));
            return Result.success(PageInfo.of(list));
        }
    }

    //删除评论
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {

        Comment comment=commentMapper.selectById(id);
        Integer num=iNewsService.getOne(new QueryWrapper<News>().eq("id",comment.getNewsId())).getComNum();
        if(num>0){
            num--;
            UpdateWrapper wrapper=new UpdateWrapper();
            wrapper.eq("id",comment.getNewsId());
            wrapper.set("comNum",num);
            iNewsService.update(wrapper);

        }
        commentMapper.delete(new QueryWrapper<Comment>().eq("id", id));
        return Result.success();
    }

    //添加评论
    @PostMapping("/add")
    public Result add(@RequestBody Comment comment) {
        comment.setCreateTime(new Date());
        int res = commentMapper.insert(comment);

        //获取评论数
        Integer num = iNewsService.getOne(new QueryWrapper<News>().eq("id", comment.getNewsId())).getComNum();
        num++;
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("id", comment.getNewsId());
        wrapper.set("comNum", num);
        Boolean flag = iNewsService.update(wrapper);//修改评论数

        if (flag && res == 1) {
            return Result.success();
        } else
            return Result.error();

    }

    //查询指定新闻的评论
    @GetMapping("/list")
    public Result<Map<String, Object>> list(Integer page, Integer pageSize, Integer newsId) {//当前页 页数
        List<Comment> comments = null;
        Map<String, Object> resultMap = new HashMap<>();//返回数据
        Page<Comment> pageOrder = new Page<>(page, pageSize);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();

        wrapper.orderByDesc("createTime");


        wrapper.eq("newsId", newsId);

        Page<Comment> commentResult = commentService.page(pageOrder, wrapper);

        comments = commentResult.getRecords();
        for (Comment comment : comments) {
            WxUserInfo wxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("id", comment.getUserId()));
            comment.setUserName(wxUserInfo.getUserName());
            comment.setSex(wxUserInfo.getSex());
        }
        resultMap.put("total", commentResult.getTotal());
        resultMap.put("totalPage", commentResult.getPages());
        resultMap.put("commentList", comments);
        return Result.success(resultMap);
    }


    @GetMapping("/commentList/{id}")
    public Result<List<Comment>> detail(@PathVariable Integer id) {
        List<Comment> list = commentMapper.findlist(id);
        return Result.success(list);
    }

    //查询我的评论
    @RequestMapping("/myComList")
    public Result<Map<String, Object>> mycomlist(Integer page, Integer pageSize, Integer userId) {//当前页 页数

        List<Comment> CommentsList = null;//查询数据
        Map<String, Object> resultMap = new HashMap<>();//返回数据

        Page<Comment> pageOrder = new Page<>(page, pageSize);

        Page<Comment> comResult = commentService.page(pageOrder, new QueryWrapper<Comment>().orderByDesc("createTime").eq("userId", userId));

        CommentsList = comResult.getRecords();
        resultMap.put("total", comResult.getTotal());
        resultMap.put("totalPage", comResult.getPages());
        resultMap.put("CommentsList", CommentsList);
        resultMap.put("totalCom", comResult.getTotal());
        return Result.success(resultMap);
    }

    @RequestMapping("/user/list3/{id}")
    public Result<List<Comment>> comlist3(@PathVariable Integer id) {
        QueryWrapper<Comment> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userId",id);
        queryWrapper.orderByDesc("createTime");
        queryWrapper.last("limit 3");
        List<Comment> list=commentMapper.selectList(queryWrapper);
        return Result.success(list);
    }

    //查询我的评论
    @RequestMapping("/ComList")
    public Result<Map<String, Object>> comlist(Integer page, Integer pageSize, Integer userId) {//当前页 页数

        List<Comment> CommentsList = null;//查询数据
        Map<String, Object> resultMap = new HashMap<>();//返回数据

        Page<Comment> pageOrder = new Page<>(page, pageSize);

        Page<Comment> comResult = commentService.page(pageOrder, new QueryWrapper<Comment>().orderByDesc("createTime").eq("userId", userId));

        CommentsList = comResult.getRecords();
        resultMap.put("total", comResult.getTotal());
        resultMap.put("totalPage", comResult.getPages());
        resultMap.put("CommentsList", CommentsList);
        return Result.success(resultMap);
    }
}
