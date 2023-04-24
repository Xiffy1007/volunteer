package com.xiffy.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiffy.common.Result;
import com.xiffy.entity.*;
import com.xiffy.mapper.ActivityMapper;
import com.xiffy.mapper.SignUpMapper;
import com.xiffy.mapper.WxUserInfoMapper;
import com.xiffy.service.IActivityService;
import com.xiffy.service.IWxUserInfoService;
import com.xiffy.service.impl.ISignUpServiceImpl;
import com.xiffy.util.DistanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private IActivityService activityService;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private SignUpMapper signUpMapper;

    @Autowired
    private ISignUpServiceImpl signUpService;

    @Autowired
    private IWxUserInfoService wxUserInfoService;

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;

    //分页查询
    @GetMapping("/page/{name}")
    public Result<PageInfo<Activity>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @PathVariable String name) {
        PageHelper.startPage(pageNum, pageSize);
        if (name.equals("all")) {//查询所有
            QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("createTime");
            List<Activity> list = activityService.list(queryWrapper);
            return Result.success(PageInfo.of(list));

        } else {
            List<Activity> list = activityService.list(new QueryWrapper<Activity>().like("name", name).or().like("address", name).or().like("leaderName", name));
            return Result.success(PageInfo.of(list));
        }
    }

    //删除活动
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        activityMapper.delete(new QueryWrapper<Activity>().eq("id", id));
        return Result.success();
    }

    //修改活动
    @PutMapping("/modify")
    public Result<Type> update(@RequestBody Activity activity) {
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("id", activity.getId());
        DistanceUtils distanceUtils = new DistanceUtils();
        Map<String, BigDecimal> map=distanceUtils.getLatAndLngByAddress(activity.getAddress());
        activity.setLng(map.get("lng").doubleValue());
        activity.setLat(map.get("lat").doubleValue());
        activity.setModifyTime(new Date());
        activityService.update(activity, wrapper);
        return Result.success();
    }

    //新增活动
    @PostMapping("/add")
    public Result<Activity> add(@RequestBody Activity activity) {
        DistanceUtils distanceUtils = new DistanceUtils();
        Map<String, BigDecimal> map=distanceUtils.getLatAndLngByAddress(activity.getAddress());
        activity.setLng(map.get("lng").doubleValue());
        activity.setLat(map.get("lat").doubleValue());
        activity.setCreateTime(new Date());//设置创建时间
        activity.setState(0);//报名中
        activityMapper.insert(activity);
        return Result.success(activity);
    }

    //按照关键字查询
    @GetMapping("/search/{key}")
    public Result<List<Activity>> search(@PathVariable String key) {
        List<Activity> activityList = activityService.list(new QueryWrapper<Activity>().like("name", key));
        return Result.success(activityList);
    }


    //微信小程序 分类查询活动
    @RequestMapping("/list")
    public Result<Map<String, Object>> list(Integer type, Integer typeId, Integer page, Integer pageSize) {//当前页 页数
        System.out.println("type=" + type);

        List<Activity> activityList = null;//查询数据
        Map<String, Object> resultMap = new HashMap<>();//返回数据

        Page<Activity> pageOrder = new Page<>(page, pageSize);

        Page<Activity> orderResult = activityService.page(pageOrder, new QueryWrapper<Activity>().eq("state", type).eq("typeId", typeId).orderByDesc("createTime"));
        System.out.println("总记录数" + orderResult.getTotal());
        System.out.println("总页数" + orderResult.getPages());
        System.out.println("当前页数据" + orderResult.getRecords());
        activityList = orderResult.getRecords();
        resultMap.put("total", orderResult.getTotal());
        resultMap.put("totalPage", orderResult.getPages());

        resultMap.put("activityList", activityList);
        return Result.success(resultMap);
    }


    //查找活动详情
    @GetMapping("/detail/{id}")
    public Result<Activity> detail(@PathVariable Integer id) {
        Activity activity = activityMapper.findDetail(id);
        return Result.success(activity);
    }
    //查询信用分
    //查找活动详情
    @GetMapping("/credit/{id}")
    public Result<Double> credit(@PathVariable Integer id) {
        WxUserInfo wxUserInfo=wxUserInfoMapper.selectById(id);
        return Result.success(wxUserInfo.getCredit());
    }


    //报名
    @PostMapping("/signup")
    public Result sign(@RequestBody SignUp signUp) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id", signUp.getActivityId());
        Activity activity = activityService.getOne(wrapper);
        Date date = new Date();
        if (activity.getState() == 0 && date.after(activity.getCreateTime()) && date.before(activity.getEndTime()) && activity.getNowNum() < activity.getNumber()) {
            SignUp sign1 = new SignUp();
            sign1.setActivityId(signUp.getActivityId());
            sign1.setUserId(signUp.getUserId());
            sign1.setCreateTime(new Date());
            sign1.setScheck(2);//初始态
            activity.setNowNum(activity.getNowNum() + 1);//报名人数加1
            UpdateWrapper updatewrapper = new UpdateWrapper();
            updatewrapper.eq("id", signUp.getActivityId());
            signUpMapper.insert(sign1);
            activityService.update(activity, updatewrapper);
            return Result.success();
        } else {
            return Result.error("3001", "报名失败！");
        }

    }

    //查看活动是否报名
    @GetMapping("checkSign")
    public Boolean issignip(SignUp signUp) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("userId", signUp.getUserId());
        wrapper.eq("activityId", signUp.getActivityId());
        SignUp sign = signUpService.getOne(wrapper);
        if (sign == null) {
            return false;
        } else {
            return true;
        }
    }


    //根据状态查询微信用户参与的活动
    @RequestMapping("/myacts")
    public Result<Map<String, Object>> acts(Integer userId, Integer state, Integer page, Integer pageSize) {

        List<Activity> list = null;
        Map<String, Object> resultMap = new HashMap<>();//返回数据
        Page<Activity> pageOrder = new Page<>(page, pageSize);
        if (state == 3) {
            Page<Activity> Results = activityMapper.findAllActs(pageOrder, userId);
            list = Results.getRecords();
            resultMap.put("total", Results.getTotal());
            resultMap.put("totalPage", Results.getPages());
        } else {
            Page<Activity> Results = activityMapper.findActs(pageOrder, userId, state);
            list = Results.getRecords();
            resultMap.put("total", Results.getTotal());
            resultMap.put("totalPage", Results.getPages());
        }

        resultMap.put("list", list);
        return Result.success(resultMap);
    }


    //微信签到
    @PostMapping("/signin/{lat}/{lng}/{id}/{userId}")
    public Result signin(@PathVariable Double lat, @PathVariable Double lng, @PathVariable Integer id, @PathVariable Integer userId) {
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>().eq("id", id));
        DistanceUtils distanceUtils = new DistanceUtils();
        Double distance = distanceUtils.getDistance(lng, lat, activity.getLng(), activity.getLat());
        distance = distance * 1000;
        UpdateWrapper<SignUp> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("activityId", id);
        updateWrapper.eq("userId", userId);

        if (distance <= 500) {//距离小于500米 签到成功
            updateWrapper.set("scheck", 1);//通过审核
            updateWrapper.set("dur", activity.getDuration());
            updateWrapper.set("credit", 1);
            Boolean res = signUpService.update(updateWrapper);

            int flag = wxUserInfoMapper.credit(1.0, userId);//信用加1
            int flag1 = wxUserInfoMapper.duration(activity.getDuration(), userId);//志愿时长增加
            //增加志愿时长和信用
            return Result.success("签到成功！");
        } else {//签到失败
            int flag = wxUserInfoMapper.credit(-1.0, userId);//信用减1

            updateWrapper.set("scheck", 0);//未通过审核
            updateWrapper.set("dur", 0);
            updateWrapper.set("credit", -1);
            Boolean res = signUpService.update(updateWrapper);
            //不增加志愿时长和减少信用
            return Result.error();
        }
    }

    //按照关键字查询
    @GetMapping("/key/{state}/{type}")
    public Result<PageInfo<Activity>> searchdetail(@RequestParam(defaultValue = "1") Integer pageNum,
                                                   @RequestParam(defaultValue = "5") Integer pageSize,
                                                   @PathVariable Integer state, @RequestParam String start,
                                                   @RequestParam String end, @PathVariable Integer type) throws Exception {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper();


        PageHelper.startPage(pageNum, pageSize);
        if (state != 3) {
            queryWrapper.eq("state", state);
        }
        if (type != 0) {
            queryWrapper.eq("typeId", type);
        }
      if (!start.equals("null")&&!start.equals("")) {

          Date Start = DateUtil.parse(start,"yyyy-MM-dd HH:mm:ss");

          queryWrapper.ge("actTime", Start);
        }
        if (!end.equals("null")&&!end.equals("")) {
            Date End = DateUtil.parse(end,"yyyy-MM-dd HH:mm:ss");
            queryWrapper.le("actendTime", End);
        }
        queryWrapper.orderByDesc("createTime");
        List<Activity> activityList = activityMapper.selectList(queryWrapper);
        return Result.success(PageInfo.of(activityList));
    }

    @RequestMapping("/user/list3/{id}")
    public Result<List<Activity>> actlist3(@PathVariable Integer id) {

        List<Activity> list=signUpMapper.find3(id);
        return Result.success(list);
    }




    @RequestMapping("/acts")
    public Result<Map<String, Object>> acts(Integer userId, Integer page, Integer pageSize) {

        List<Activity> list = null;
        Map<String, Object> resultMap = new HashMap<>();//返回数据
        Page<Activity> pageOrder = new Page<>(page, pageSize);

            Page<Activity> Results = activityMapper.findlist(pageOrder, userId);
            list = Results.getRecords();
            resultMap.put("total", Results.getTotal());
            resultMap.put("totalPage", Results.getPages());


        resultMap.put("list", list);
        return Result.success(resultMap);
    }


}
