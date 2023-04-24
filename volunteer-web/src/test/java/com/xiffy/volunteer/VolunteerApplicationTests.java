package com.xiffy.volunteer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiffy.entity.Activity;
import com.xiffy.entity.Type;
import com.xiffy.entity.WxUserInfo;
import com.xiffy.mapper.ActivityMapper;
import com.xiffy.mapper.TypeMapper;
import com.xiffy.mapper.WxUserInfoMapper;
import com.xiffy.service.IActivityService;
import com.xiffy.service.ITypeService;
import com.xiffy.service.IWxUserInfoService;
import com.xiffy.util.DistanceUtils;
import com.xiffy.vo.EchartsData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;


@SpringBootTest
class VolunteerApplicationTests {

    @Resource
    WxUserInfoMapper wxUserInfoMapper;
    @Resource
    IWxUserInfoService wxUserInfoService;

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ITypeService typeService;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private IActivityService activityService;



    @Test
    void add() {
        WxUserInfo wxUserInfo = new WxUserInfo();
        wxUserInfo.setUserName("某幻");
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("id", 13);
        wxUserInfoService.update(wxUserInfo, wrapper);
    }


    @Test
    void location() {
        String ids = ",1,2,3,4,5";
        ids = ids.substring(1, ids.length());
        System.out.println(ids);
        List<Integer> idList = Arrays.stream(ids.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        System.out.println(idList);
    }

    @Test
    void test() {
        String time = "2022-09-22 22:43:48";
        Date date = DateUtil.parse(time, "yyyy-MM-dd HH:mm:ss");

        System.out.println(date);
    }

    @Test
    void t() {
        QueryWrapper<Activity> queryWrapper=new QueryWrapper<>();
        queryWrapper.groupBy("typeId");
        List<Map<String,Object>> list= activityService.listMaps(queryWrapper);
        System.out.println(list);

    }

    @Test
    void tet(){
        DistanceUtils distanceUtils = new DistanceUtils();
        Double distance = distanceUtils.getDistance(114.0291901, 30.5820300, 114.0291900, 30.5820300);
        distance = distance * 1000;
        System.out.println(distance);
    }


}
