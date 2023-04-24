package com.xiffy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiffy.common.Result;
import com.xiffy.entity.*;
import com.xiffy.mapper.LikeOrColMapper;
import com.xiffy.service.ILikeOrColService;
import com.xiffy.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/likeorcol")
public class LikeOrColController {

    @Autowired
    private LikeOrColMapper likeOrColMapper;

    @Autowired
    private ILikeOrColService likeOrColService;

    @Autowired
    private INewsService iNewsService;

    @PutMapping("/like")
    private Result<Boolean> like(@RequestBody LikeOrCol likeOrCol){
        Integer num=iNewsService.getOne(new QueryWrapper<News>().eq("id",likeOrCol.getNewsId())).getLikeNum();
        num++;
        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.eq("id",likeOrCol.getNewsId());
        wrapper.set("likeNum",num);
        iNewsService.update(wrapper);

        LikeOrCol likeOrCol1=new LikeOrCol();
        likeOrCol1.setCreateTime(new Date());
        likeOrCol1.setNewsId(likeOrCol.getNewsId());
        likeOrCol1.setType(0);
        likeOrCol1.setUserId(likeOrCol.getUserId());
        likeOrColMapper.insert(likeOrCol1);//插入点赞记录
        return Result.success(true);

    }

    @PutMapping("/col")
    private Result<Boolean> col(@RequestBody LikeOrCol likeOrCol){
        Integer num=iNewsService.getOne(new QueryWrapper<News>().eq("id",likeOrCol.getNewsId())).getColNum();
        num++;
        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.eq("id",likeOrCol.getNewsId());
        wrapper.set("colNum",num);
        iNewsService.update(wrapper);

        LikeOrCol likeOrCol1=new LikeOrCol();
        likeOrCol1.setCreateTime(new Date());
        likeOrCol1.setNewsId(likeOrCol.getNewsId());
        likeOrCol1.setType(1);
        likeOrCol1.setUserId(likeOrCol.getUserId());
        likeOrColMapper.insert(likeOrCol1);//插入点赞记录
        return Result.success(true);

    }

    @DeleteMapping("/discol")
    private Result<Boolean> discol(@RequestBody LikeOrCol likeOrCol){
        likeOrColMapper.delete(new QueryWrapper<LikeOrCol>().eq("newsId",likeOrCol.getNewsId()).eq("userId",likeOrCol.getUserId()).eq("type",1));
        Integer num=iNewsService.getOne(new QueryWrapper<News>().eq("id",likeOrCol.getNewsId())).getColNum();
        if(num>0){
            num--;
            UpdateWrapper wrapper=new UpdateWrapper();
            wrapper.eq("id",likeOrCol.getNewsId());
            wrapper.set("colNum",num);
            iNewsService.update(wrapper);
        }
        return Result.success(false);

    }

    @DeleteMapping("/dislike")
    private Result<Boolean> dislike(@RequestBody LikeOrCol likeOrCol){
        likeOrColMapper.delete(new QueryWrapper<LikeOrCol>().eq("newsId",likeOrCol.getNewsId()).eq("userId",likeOrCol.getUserId()).eq("type",0));//删除点赞记录
        Integer num=iNewsService.getOne(new QueryWrapper<News>().eq("id",likeOrCol.getNewsId())).getLikeNum();
        if(num>0){
            num--;
            UpdateWrapper wrapper=new UpdateWrapper();
            wrapper.eq("id",likeOrCol.getNewsId());
            wrapper.set("likeNum",num);
            iNewsService.update(wrapper);

        }
        return Result.success(false);

    }

    @GetMapping("/islike")
    private boolean islike(LikeOrCol likeOrCol){
        LikeOrCol like=likeOrColService.getOne(new QueryWrapper<LikeOrCol>().eq("newsId",likeOrCol.getNewsId()).eq("userId",likeOrCol.getUserId()).eq("type",0));
        if(like!=null){//如果已点赞
            return true;
        }else {//如果没有点赞
            return false;
        }

    }

    @GetMapping("/iscol")
    private boolean Col(LikeOrCol likeOrCol){
        LikeOrCol like=likeOrColService.getOne(new QueryWrapper<LikeOrCol>().eq("newsId",likeOrCol.getNewsId()).eq("userId",likeOrCol.getUserId()).eq("type",1));
        if(like!=null){//如果已收藏
            return true;
        }else {//如果没有收藏
            return false;
        }

    }

    //查询指定新闻的点赞或评论情况 type-0:点赞 1-收藏
    @GetMapping("/detail/{newsId}/{type}")
    public Result<List<User>> detail(@PathVariable Integer newsId,@PathVariable Integer type) {
        List<User> list = likeOrColMapper.finddetial(newsId,type);
        return Result.success(list);
    }


    //查询我的收藏
    @RequestMapping("/mycol")
    public Result<Map<String, Object>> acts(Integer userId,Integer page, Integer pageSize) {

        List<News> list = null;
        Map<String, Object> resultMap = new HashMap<>();//返回数据
        Page<News> pageOrder = new Page<>(page, pageSize);

            Page<News> Results = likeOrColMapper.findcol(pageOrder, userId);
            list = Results.getRecords();
            resultMap.put("total", Results.getTotal());
            resultMap.put("totalPage", Results.getPages());

        resultMap.put("newsList", list);
        return Result.success(resultMap);
    }
    //查询我的点赞
    @RequestMapping("/mylike")
    public Result<Map<String, Object>> like(Integer userId,Integer page, Integer pageSize) {

        List<News> list = null;
        Map<String, Object> resultMap = new HashMap<>();//返回数据
        Page<News> pageOrder = new Page<>(page, pageSize);

        Page<News> Results = likeOrColMapper.findlikes(pageOrder, userId);
        list = Results.getRecords();
        resultMap.put("total", Results.getTotal());
        resultMap.put("totalPage", Results.getPages());

        resultMap.put("newsList", list);
        return Result.success(resultMap);
    }



}
