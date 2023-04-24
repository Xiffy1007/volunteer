package com.xiffy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiffy.common.Result;
import com.xiffy.entity.Notice;
import com.xiffy.mapper.NoticeMapper;
import com.xiffy.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private INoticeService noticeService;

    @Autowired
    private NoticeMapper noticeMapper;

    //分页查询公告
    @GetMapping("/page/{name}")
    public Result<PageInfo<Notice>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @PathVariable String name) {
        PageHelper.startPage(pageNum, pageSize);
        if(name.equals("all")){//查询所有公告
            List<Notice> list = noticeService.list(new QueryWrapper<Notice>().orderByDesc("createTime"));
            return Result.success(PageInfo.of(list));

        }else {//查询单个公告
            List<Notice> list= noticeService.list(new QueryWrapper<Notice>().like("name", name).orderByDesc("createTime"));


            return Result.success(PageInfo.of(list));
        }
    }

    //删除公告
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id)
    {
        noticeMapper.delete(new QueryWrapper<Notice>().eq("id",id));
        return Result.success();
    }

    //修改公告
    @PutMapping("/modify")
    public Result<Notice> update(@RequestBody Notice notice){
        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.eq("id",notice.getId());
        notice.setModifyTime(new Date());
        noticeService.update(notice,wrapper);
        return Result.success();
    }

    //新增公告
    @PostMapping("/add")
    public Result<Notice> add(@RequestBody Notice notice) {
        notice.setCreateTime(new Date());//设置创建时间
        noticeMapper.insert(notice);
        return Result.success(notice);
    }

    //微信小程序 查询所有公告
    @RequestMapping("/list")
    public Result<Map<String,Object>> list(Integer page, Integer pageSize){//当前页 页数

        List<Notice> noticeList=null;//查询数据
        Map<String,Object> resultMap=new HashMap<>();//返回数据

        Page<Notice> pageOrder=new Page<>(page,pageSize);

        Page<Notice> orderResult = noticeService.page(pageOrder, new QueryWrapper<Notice>().orderByDesc("createTime"));

        noticeList=orderResult.getRecords();
        resultMap.put("total",orderResult.getTotal());
        resultMap.put("totalPage",orderResult.getPages());

        resultMap.put("noticeList",noticeList);
        return Result.success(resultMap);
    }

    //查询公告详情
    @GetMapping("/detail/{id}")
    public Result<Notice> detail(@PathVariable Integer id){
        Notice notice=noticeService.getOne(new QueryWrapper<Notice>().eq("id",id));
        return Result.success(notice);
    }



}
