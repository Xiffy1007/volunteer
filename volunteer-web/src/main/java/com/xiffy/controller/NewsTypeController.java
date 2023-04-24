package com.xiffy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiffy.common.Result;
import com.xiffy.entity.News;
import com.xiffy.entity.NewsType;
import com.xiffy.mapper.NewsMapper;
import com.xiffy.mapper.NewsTypeMapper;
import com.xiffy.service.IActivityService;
import com.xiffy.service.INewsService;
import com.xiffy.service.INewsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/newstype")
public class NewsTypeController {

    @Autowired
    private INewsTypeService newsTypeService;

    @Autowired
    private NewsTypeMapper newsTypeMapper;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private INewsService iNewsService;

    @Autowired
    private NewsMapper newsMapper;
    //分页查询
    @GetMapping("/page/{name}")
    public Result<PageInfo<NewsType>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @PathVariable String name) {
        PageHelper.startPage(pageNum, pageSize);
        if(name.equals("all")){//查询所有
            List<NewsType> list = newsTypeService.list(new QueryWrapper<NewsType>().orderByDesc("createTime"));
            return Result.success(PageInfo.of(list));

        }else {//查询单个
            List<NewsType> list=newsTypeMapper.selectList(new QueryWrapper<NewsType>().like("name",name).orderByDesc("createTime"));

            return Result.success(PageInfo.of(list));
        }
    }

    //删除类别
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        int typeId=0;
        NewsType Type =newsTypeMapper.selectOne(new QueryWrapper<NewsType>().eq("name","默认"));
        if(Type==null){//如果不存在默认种类则添加
            NewsType newsType=new NewsType();
            newsType.setName("默认");
            newsType.setCreateTime(new Date());//设置创建时间
            int res1=newsTypeMapper.insert(newsType);//插入默认种类

            typeId=newsTypeMapper.selectOne(new QueryWrapper<NewsType>().eq("name","默认")).getId();
        }else{

            typeId=Type.getId();
            List<News> newsList=iNewsService.list(new QueryWrapper<News>().eq("typeId",id));
            if(newsList.size()==0){
                int res=newsTypeMapper.delete(new QueryWrapper<NewsType>().eq("id",id));
                return Result.success();

            }
        }

        int res=newsTypeMapper.delete(new QueryWrapper<NewsType>().eq("id",id));
        List<News> newsList=iNewsService.list(new QueryWrapper<News>().eq("typeId",id));
        for(News news:newsList){
            UpdateWrapper wrapper=new UpdateWrapper();
            wrapper.eq("typeId",id);
            news.setTypeId(typeId);
            Boolean res2=iNewsService.update(news,wrapper);

        }
        if(res==1){
            return Result.success();
        }
        else return Result.error();

    }

    @GetMapping("/exist")
    public Result<Boolean> exist(){
        NewsType Type =newsTypeMapper.selectOne(new QueryWrapper<NewsType>().eq("name","默认"));
        if(Type!=null){
            List<News> newsList=iNewsService.list(new QueryWrapper<News>().eq("typeId",Type.getId()));
            if(newsList.size()==0){
                return Result.success(true);//可以删除
            }else return Result.success(false);//不能删除
        }
        return Result.success(true);//可以删除

    }

    //修改类别
    @PutMapping("/modify")
    public Result update(@RequestBody NewsType newsType){
        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.eq("id",newsType.getId());
        newsType.setModifyTime(new Date());
        newsTypeService.update(newsType,wrapper);
        return Result.success();
    }

    //新增类别
    @PostMapping("/add")
    public Result<NewsType> add(@RequestBody NewsType newsType) {
        newsType.setCreateTime(new Date());//设置创建时间
        newsTypeMapper.insert(newsType);
        return Result.success(newsType);
    }



    @GetMapping("/findnewsTypes")
    public Result<List<NewsType>> findnewsTypes(){
        List<NewsType> newsTypeList=newsTypeService.list();//获取活动的所有类型
        return Result.success(newsTypeList);
    }

}
