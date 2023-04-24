package com.xiffy.controller;

import cn.hutool.json.JSONObject;
import com.xiffy.common.Result;
import com.xiffy.mapper.*;

import com.xiffy.service.*;

import com.xiffy.vo.EchartsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Autowired
    private IActivityService activityService;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private INewsService NewsService;

    @Autowired
    private NewsMapper newsMapper;


    @Autowired
    private IWxUserInfoService wxUserInfoService;

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;

    @Autowired
    private ITypeService typeService;

    @Autowired
    private TypeMapper typeMapper;

    @GetMapping("getTotal")
    public Result<Map<String, Object>> getTotal() {
        Map<String, Object> map = new HashMap<>();
        //获取用户总数
        map.put("totalUser", wxUserInfoService.count());
        //获取评论总数
        map.put("totalComment", commentService.count());
        //获取新闻总数
        map.put("totalNews", NewsService.count());
        //获取活动总数
        map.put("totalacts", activityService.count());
        //获取活动种类数
        map.put("totalActClass",typeService.count());
        return Result.success(map);
    }

    @GetMapping("/get/activity")
    public Result<List<EchartsData>> getactEchartsData() {
        List<EchartsData> list = new ArrayList<>();
        List<Map<String, Object>> typeActivity = activityMapper.getTypeActivity();
        Map<String, Long> typeMap = new HashMap<>();
        for(Map<String,Object>map:typeActivity) {
            typeMap.put((String)map.get("name"),(Long)map.get("count"));
        }
        getPieData("分类活动总数",list,typeMap);
        getBarData("分类活动总数",list,typeMap);

        return Result.success(list);

    }

    @GetMapping("/get/news")
    public Result<List<EchartsData>> getnewsEchartsData() {
        List<EchartsData> list = new ArrayList<>();
        List<Map<String, Object>> typeNews = newsMapper.getTypeNews();
        Map<String, Long> typeMap = new HashMap<>();
        for(Map<String,Object>map:typeNews) {
            typeMap.put((String)map.get("name"),(Long)map.get("count"));
        }
        getPieData("分类新闻总数",list,typeMap);
        getBarData("分类新闻总数",list,typeMap);

        return Result.success(list);

    }

    @GetMapping("/get/classinfo")
    public Result<List<EchartsData>> getclassinfoEchartsData() {
        List<EchartsData> list = new ArrayList<>();
        List<Map<String, Object>> typeclassinfo = wxUserInfoMapper.getTypeclassinfo();
        Map<String, Long> typeMap = new HashMap<>();
        for(Map<String,Object>map:typeclassinfo) {
            typeMap.put((String)map.get("name"),(Long)map.get("count"));
        }
        getPieData("班级人数",list,typeMap);
        getBarData("班级人数",list,typeMap);

        return Result.success(list);

    }
    @GetMapping("/get/college")
    public Result<List<EchartsData>> getcollegeEchartsData() {
        List<EchartsData> list = new ArrayList<>();
        List<Map<String, Object>> typecollege = wxUserInfoMapper.getTypecollege();
        Map<String, Long> typeMap = new HashMap<>();
        for(Map<String,Object>map:typecollege) {
            typeMap.put((String)map.get("name"),(Long)map.get("count"));
        }
        getPieData("学院人数",list,typeMap);
        getBarData("学院人数",list,typeMap);

        return Result.success(list);

    }
    @GetMapping("/get/major")
    public Result<List<EchartsData>> getmajorEchartsData() {
        List<EchartsData> list = new ArrayList<>();
        List<Map<String, Object>> typemajor = wxUserInfoMapper.getTypemajor();
        Map<String, Long> typeMap = new HashMap<>();
        for(Map<String,Object>map:typemajor) {
            typeMap.put((String)map.get("name"),(Long)map.get("count"));
        }
        getPieData("专业人数",list,typeMap);
        getBarData("专业人数",list,typeMap);

        return Result.success(list);

    }
    @GetMapping("/get/comment")
    public Result<List<EchartsData>> getcommentEchartsData() {
        List<EchartsData> list = new ArrayList<>();
        List<Map<String, Object>> typecomment = commentMapper.getTypecomment();
        Map<String, Long> typeMap = new HashMap<>();
        for(Map<String,Object>map:typecomment) {
            typeMap.put((String)map.get("name"),(Long)map.get("count"));
        }
        getPieData("分类评论数",list,typeMap);
        getBarData("分类评论数",list,typeMap);

        return Result.success(list);

    }


    //封装饼状图
    private void getPieData(String name, List pielist ,Map<String,Long> dataMap) {
        EchartsData pieData = new EchartsData();
        Map<String, String> titleMap = new HashMap<>();
        titleMap.put("text", name);
        pieData.setTitle(titleMap);

        Map<String, Object> tooltipMap = new HashMap<>();
        tooltipMap.put("show", true);
        pieData.setTooltip(tooltipMap);

        Map<String, String> legendMap = new HashMap<>();
        legendMap.put("orient", "vertical");
        pieData.setLegend(legendMap);

        EchartsData.Series series = new EchartsData.Series();
        series.setName(name);
        series.setType("pie");
        series.setRadius("50%");

        List<Object> objects = new ArrayList<>();
        for (String key : dataMap.keySet()) {
            objects.add(new JSONObject().putOpt("name", key).putOpt("value", dataMap.get(key)));
        }
        series.setData(objects);
        pieData.setSeries(Collections.singletonList(series));
        pielist.add(pieData);
    }

    //封装柱状图
    private void getBarData(String name, List pielist ,Map<String,Long> dataMap) {
        EchartsData barData = new EchartsData();
        Map<String, String> titleMap = new HashMap<>();
        titleMap.put("text", name);
        barData.setTitle(titleMap);

        Map<String, Object> tooltipMap = new HashMap<>();
        tooltipMap.put("show", true);
        barData.setTooltip(tooltipMap);


        EchartsData.Series series = new EchartsData.Series();
        series.setName(name);
        series.setType("bar");
        List<Object> objects = new ArrayList<>();
        List<Object> xAxisObjs = new ArrayList<>();

        for (String key : dataMap.keySet()) {
            objects.add(dataMap.get(key));
            xAxisObjs.add(key);
        }
        series.setData(objects);

        Map<String,Object> xAxisMap = new HashMap<>();
        xAxisMap.put("data",xAxisObjs);
        barData.setxAxis(xAxisMap);
        barData. setyAxis(new HashMap());
        barData.setSeries(Collections.singletonList(series));
        pielist.add(barData);
    }


}
