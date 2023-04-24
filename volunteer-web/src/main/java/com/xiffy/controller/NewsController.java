package com.xiffy.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiffy.common.Result;
import com.xiffy.entity.Activity;
import com.xiffy.entity.News;
import com.xiffy.entity.News;
import com.xiffy.entity.Notice;
import com.xiffy.mapper.NewsMapper;
import com.xiffy.mapper.NewsMapper;
import com.xiffy.service.INewsService;
import com.xiffy.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private INewsService iNewsService;

    @Autowired
    private NewsMapper newsMapper;


    //分页查询我发布的新闻
    @GetMapping("/page/{adminId}/{name}")
    public Result<PageInfo<News>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "15") Integer pageSize,
                                       @PathVariable String name, @PathVariable Integer adminId) {
        PageHelper.startPage(pageNum, pageSize);
        if (name.equals("all")) {//查询所有新闻

            List<News> list = iNewsService.list(new QueryWrapper<News>().eq("adminId", adminId).orderByDesc("createTime"));
            return Result.success(PageInfo.of(list));

        } else {
            List<News> list =  newsMapper.selectList(new QueryWrapper<News>().like("name", name).eq("adminId", adminId).orderByDesc("createTime"));
            return Result.success(PageInfo.of(list));
        }
    }

    //分页查询所有的新闻
    @GetMapping("/page/{name}")
    public Result<PageInfo<News>> pageAll(@RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          @PathVariable String name) {
        PageHelper.startPage(pageNum, pageSize);
        if (name.equals("all")) {//查询所有新闻
            QueryWrapper<News> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("createTime");

            List<News> list = iNewsService.list(wrapper);
            return Result.success(PageInfo.of(list));
        } else {
            List<News> list =  newsMapper.selectList(new QueryWrapper<News>().like("name", name));
            return Result.success(PageInfo.of(list));
        }
    }

    //删除新闻
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        newsMapper.delete(new QueryWrapper<News>().eq("id", id));
        return Result.success();
    }

    //修改新闻
    @PutMapping("/modify")
    public Result<News> update(@RequestBody News news) {
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("id", news.getId());
        news.setModifyTime(new Date());
        iNewsService.update(news, wrapper);
        return Result.success();
    }

    //新增新闻
    @PostMapping("/add/{id}")
    public Result<News> add(@RequestBody News news, @PathVariable Integer id) {
        news.setCreateTime(new Date());//设置创建时间
        news.setAdminId(id);//管理员
        news.setLikeNum(0);
        news.setColNum(0);
        newsMapper.insert(news);
        return Result.success(news);
    }

    //按照关键字查询
    @GetMapping("/search/{key}")
    public Result<List<News>> search(@PathVariable String key) {
        List<News> newsList = iNewsService.list(new QueryWrapper<News>().like("name", key));
        return Result.success(newsList);
    }

    //查找新闻详情
    @GetMapping("/detail/{id}")
    public Result<News> detail(@PathVariable Integer id) {
        News news = iNewsService.getOne(new QueryWrapper<News>().eq("id", id));
        return Result.success(news);
    }

    //微信小程序 分类查询新闻
    @RequestMapping("/list")
    public Result<Map<String, Object>> list(Integer typeId, Integer page, Integer pageSize) {//当前页 页数

        List<News> newsList = null;//查询数据
        Map<String, Object> resultMap = new HashMap<>();//返回数据

        Page<News> pageOrder = new Page<>(page, pageSize);

        Page<News> newsResult = iNewsService.page(pageOrder, new QueryWrapper<News>().eq("typeId", typeId).orderByDesc("createTime"));

        newsList = newsResult.getRecords();
        resultMap.put("total", newsResult.getTotal());
        resultMap.put("totalPage", newsResult.getPages());

        resultMap.put("newsList", newsList);
        return Result.success(resultMap);
    }

    //按照关键字查询
    @GetMapping("/key/{order}/{type}")
    public Result<PageInfo<News>> searchdetail(@RequestParam(defaultValue = "1") Integer pageNum,
                                               @RequestParam(defaultValue = "15") Integer pageSize,
                                               @PathVariable Integer order, @RequestParam String start,
                                               @RequestParam String end, @PathVariable Integer type) throws Exception {
        QueryWrapper<News> queryWrapper = new QueryWrapper();


        PageHelper.startPage(pageNum, pageSize);
        if (order == 0) {
            queryWrapper.orderByDesc("likeNum");
        }
        if (order == 1) {
            queryWrapper.orderByDesc("comNum");
        }
        if (order == 2) {
            queryWrapper.orderByDesc("colNum");
        }
        if (type != 0) {
            queryWrapper.eq("typeId", type);
        }
        if (!start.equals("null")&&!start.equals("")) {

            Date Start = DateUtil.parse(start, "yyyy-MM-dd HH:mm:ss");

            queryWrapper.ge("createTime", Start);
        }
        if (!end.equals("null")&&!end.equals("")) {
            Date End = DateUtil.parse(end, "yyyy-MM-dd HH:mm:ss");
            queryWrapper.le("createTime", End);
        }
        queryWrapper.orderByDesc("createTime");
        List<News> NewsList = newsMapper.selectList(queryWrapper);
        return Result.success(PageInfo.of(NewsList));
    }
    //按照关键字查询
    @GetMapping("/key/{Id}/{order}/{type}")
    public Result<PageInfo<News>> searchdetailadmin(@RequestParam(defaultValue = "1") Integer pageNum,
                                               @RequestParam(defaultValue = "15") Integer pageSize,
                                               @PathVariable Integer order, @PathVariable Integer Id,  @RequestParam String start,
                                               @RequestParam String end, @PathVariable Integer type) throws Exception {
        QueryWrapper<News> queryWrapper = new QueryWrapper();
        PageHelper.startPage(pageNum, pageSize);
        if (order == 0) {
            queryWrapper.orderByDesc("likeNum");
        }
        if (order == 1) {
            queryWrapper.orderByDesc("comNum");
        }
        if (order == 2) {
            queryWrapper.orderByDesc("colNum");
        }
        if (type != 0) {
            queryWrapper.eq("typeId", type);
        }
        if (!start.equals("null")&&!start.equals("")) {

            Date Start = DateUtil.parse(start, "yyyy-MM-dd HH:mm:ss");

            queryWrapper.ge("createTime", Start);
        }
        if (!end.equals("null")&&!end.equals("")) {
            Date End = DateUtil.parse(end, "yyyy-MM-dd HH:mm:ss");
            queryWrapper.le("createTime", End);
        }
        queryWrapper.orderByDesc("createTime").eq("adminId",Id);
        List<News> NewsList = newsMapper.selectList(queryWrapper);
        return Result.success(PageInfo.of(NewsList));
    }

}
