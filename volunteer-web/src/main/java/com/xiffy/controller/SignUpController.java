package com.xiffy.controller;

import cn.hutool.crypto.asymmetric.Sign;
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
import com.xiffy.service.ISignUpService;
import com.xiffy.service.IWxUserInfoService;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/signup")
public class SignUpController {
    @Autowired
    private SignUpMapper signUpMapper;

    @Autowired
    private ISignUpService signUpService;

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private IWxUserInfoService wxUserInfoService;

    //分页查询报名情况
    @GetMapping("/page")
    public Result<PageInfo<SignUp>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "15") Integer pageSize,
                                         @RequestParam Integer check
    ) {
        if (check == 4) {
            PageHelper.startPage(pageNum, pageSize);
            List<SignUp> list = signUpMapper.find();
            return Result.success(PageInfo.of(list));
        } else {
            PageHelper.startPage(pageNum, pageSize);
            List<SignUp> list = signUpMapper.find1(check);
            return Result.success(PageInfo.of(list));
        }

    }

    //删除报名详情
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        SignUp signUp=signUpMapper.selectById(id);

        Integer num=activityService.getOne(new QueryWrapper<Activity>().eq("id",signUp.getActivityId())).getNowNum();
        if(num>0){
            num--;
            UpdateWrapper wrapper=new UpdateWrapper();
            wrapper.eq("id",signUp.getActivityId());
            wrapper.set("nowNum",num);
            activityService.update(wrapper);

        }
        signUpMapper.delete(new QueryWrapper<SignUp>().eq("id", id));

        return Result.success();
    }

    //查询指定活动的报名情况
    @GetMapping("/detail/{id}")
    public Result<List<User>> detail(@PathVariable Integer id) {
        List<User> list = signUpMapper.finddetial1(id);
        return Result.success(list);
    }

    //查找指定用户的活动详情
    @RequestMapping("/userActivty/{id}")
    public Result<List<SignUp>> user(@PathVariable Integer id){
        List<SignUp> list = signUpMapper.finduser(id);
        return Result.success(list);
    }

    //查看报名详情
    @GetMapping("/{id}")
    public Result<SignUp> detail1(@PathVariable Integer id) {
        SignUp signUp = signUpMapper.findone(id);
        return Result.success(signUp);
    }

    //dur
    @PutMapping("/dur/{duration}")
    public Result durmodify(@RequestParam Integer id, @PathVariable Double duration,@RequestParam Integer userid) {
        UpdateWrapper<SignUp> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("scheck", 3);
        updateWrapper.set("checkTime", new Date());
        updateWrapper.set("dur", duration);
        Boolean res = signUpService.update(updateWrapper);
        //修改用户的资料
        int flag=wxUserInfoMapper.duration(duration,userid);

        if (res&&flag==1) {
            return Result.success();
        } else {
            return Result.error();
        }

    }

    //cre
    @PutMapping("/cre/{credit}")
    public Result cremodify(@RequestParam Integer id, @PathVariable Double credit,@RequestParam Integer userid) {
        UpdateWrapper<SignUp> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("scheck", 3);
        updateWrapper.set("checkTime", new Date());
        updateWrapper.set("credit", credit);
        Boolean res = signUpService.update(updateWrapper);
        //修改用户的资料
        int flag=wxUserInfoMapper.credit(credit,userid);

        if (res&&flag==1) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    //查询用户的阳光信用记录
    @RequestMapping("/credit/list")
    public Result<Map<String,Object>> creditlist(Integer page, Integer pageSize, Integer userId){//当前页 页数

        List<SignUp> creditList=null;//查询数据
        Map<String,Object> resultMap=new HashMap<>();//返回数据

        Page<SignUp> pageOrder=new Page<>(page,pageSize);

        Page<SignUp> creResult = signUpService.page(pageOrder, new QueryWrapper<SignUp>().orderByDesc("createTime").eq("userId",userId).in("scheck",0,1,3));

        creditList=creResult.getRecords();
        resultMap.put("total",creResult.getTotal());
        resultMap.put("totalPage",creResult.getPages());

        resultMap.put("creditsList",creditList);

        //查询总阳光信用
        WxUserInfo wxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("id",userId));
        resultMap.put("totalcredit",wxUserInfo.getCredit());

        return Result.success(resultMap);
    }


    //查询用户的志愿时长记录
    @RequestMapping("/duration/list")
    public Result<Map<String,Object>> durationlist(Integer page, Integer pageSize, Integer userId){//当前页 页数

        List<SignUp> durationtList=null;//查询数据
        Map<String,Object> resultMap=new HashMap<>();//返回数据

        Page<SignUp> pageOrder=new Page<>(page,pageSize);

        Page<SignUp> creResult = signUpService.page(pageOrder, new QueryWrapper<SignUp>().orderByDesc("createTime").eq("userId",userId).in("scheck",0,1,3));

        durationtList=creResult.getRecords();
        resultMap.put("total",creResult.getTotal());
        resultMap.put("totalPage",creResult.getPages());

        resultMap.put("durationtList",durationtList);

        //查询总志愿时长
        WxUserInfo wxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("id",userId));
        resultMap.put("totalduration",wxUserInfo.getDuration());

        return Result.success(resultMap);
    }
}
