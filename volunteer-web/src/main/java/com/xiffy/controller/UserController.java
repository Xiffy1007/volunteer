package com.xiffy.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiffy.common.Result;

import com.xiffy.entity.Activity;
import com.xiffy.entity.Notice;
import com.xiffy.entity.SignUp;
import com.xiffy.entity.WxUserInfo;
import com.xiffy.mapper.WxUserInfoMapper;
import com.xiffy.properties.WeixinProperties;
import com.xiffy.service.IWxUserInfoService;
import com.xiffy.util.HttpClientUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 微信用户Controller
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private WeixinProperties weixinProperties;

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Autowired
    private IWxUserInfoService wxUserInfoService;

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;

    /**
     * 小程序微信用户登录
     *
     * @return
     */
    @RequestMapping("/wxlogin")
    public Result<WxUserInfo> wxLogin(@RequestBody String code) {
        code = code.replace("\"", "");
        String jscode2sessionUrl = weixinProperties.getJscode2sessionUrl() + "?appid=" + weixinProperties.getAppid() + "&secret=" + weixinProperties.getSecret() + "&js_code=" + code + "&grant_type=authorization_code";

        String result = httpClientUtil.sendHttpGet(jscode2sessionUrl);

        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.get("openid").toString();//得到openid

        WxUserInfo resultWxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("openid", openid));
        if (resultWxUserInfo == null) {//用户不存在 则插入用户到数据库
            WxUserInfo wxUserInfo = new WxUserInfo();
            wxUserInfo.setOpenid(openid);
            wxUserInfo.setCredit(15);
            wxUserInfo.setUserName("微信用户");//昵称
            wxUserInfo.setRegisterDate(new Date());//注册日期
            wxUserInfo.setLastLoginDate(new Date());//最后登录日期
            wxUserInfoService.save(wxUserInfo);//插入数据库
            return Result.success(wxUserInfo);
        } else {//如果用户存在则返回用户信息
            UpdateWrapper<WxUserInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("openid", openid);
            resultWxUserInfo.setLastLoginDate(new Date());
            updateWrapper.set("lastLoginDate", new Date());
            wxUserInfoService.update(updateWrapper);
            return Result.success(resultWxUserInfo);
        }
    }


    //小程序微信端更新用户信息
    @PutMapping("/update")
    public Result<WxUserInfo> update(@RequestBody WxUserInfo wxUserInfo) {
        System.out.println(wxUserInfo.getOpenid());

        UpdateWrapper<WxUserInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("openid", wxUserInfo.getOpenid());
        updateWrapper.set("userName", wxUserInfo.getUserName()).set("sex", wxUserInfo.getSex()).set("phone", wxUserInfo.getPhone())
                .set("sno", wxUserInfo.getSno()).set("college", wxUserInfo.getCollege()).set("major", wxUserInfo.getMajor())
                .set("classinfo", wxUserInfo.getClassinfo()).set("email", wxUserInfo.getEmail());
        Boolean flag = wxUserInfoService.update(updateWrapper);
        if (flag) {
            WxUserInfo resultWxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("openid", wxUserInfo.getOpenid()));
            return Result.success(resultWxUserInfo);
        } else {
            return Result.error();
        }
    }

    //管理员批量查询用户资料 分页查询用户列表
    @GetMapping("/page/{name}/{key}")
    public Result<PageInfo<WxUserInfo>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                             @RequestParam(defaultValue = "15") Integer pageSize,
                                             @PathVariable String name, @PathVariable Integer key) {
        PageHelper.startPage(pageNum, pageSize);


        if (name.equals("all") == false && (key == 0 || key == 1)) {
            List<WxUserInfo> list = wxUserInfoService.list(new QueryWrapper<WxUserInfo>().eq("userName", name));
            return Result.success(PageInfo.of(list));
        } else if (name.equals("all") == false && key == 2) {
            WxUserInfo wxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("sno", name));
            List<WxUserInfo> list = new ArrayList<>();
            list.add(wxUserInfo);
            return Result.success(PageInfo.of(list));
        } else if (name.equals("all") == false && key == 3) {
            List<WxUserInfo> list = wxUserInfoService.list(new QueryWrapper<WxUserInfo>().eq("college", name));
            return Result.success(PageInfo.of(list));
        } else if (name.equals("all") == false && key == 4) {
            List<WxUserInfo> list = wxUserInfoService.list(new QueryWrapper<WxUserInfo>().eq("major", name));
            return Result.success(PageInfo.of(list));
        } else if (name.equals("all") == false && key == 5) {
            List<WxUserInfo> list = wxUserInfoService.list(new QueryWrapper<WxUserInfo>().eq("classinfo", name));
            return Result.success(PageInfo.of(list));
        } else {
            List<WxUserInfo> list = wxUserInfoService.list();
            return Result.success(PageInfo.of(list));


        }

    }


    //管理员更新用户资料
    @PutMapping("/modify")
    public Result<WxUserInfo> updateuserinfo(@RequestBody WxUserInfo wxUserInfo) {
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("openid", wxUserInfo.getOpenid());
        wxUserInfoService.update(wxUserInfo, wrapper);
        return Result.success();
    }

    //管理员删除用户 根据id删除用户
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        wxUserInfoMapper.delete(new QueryWrapper<WxUserInfo>().eq("id", id));
        return Result.success();
    }

    //管理员获取用户信息
    @GetMapping("/get/{id}")
    public Result<WxUserInfo> detail(@PathVariable Long id) {
        WxUserInfo wxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("id", id));
        return Result.success(wxUserInfo);
    }

    //微信小程序查询志愿时长排名
    @RequestMapping("/ranklist")
    public Result<Map<String, Object>> list(Integer page, Integer pageSize, Integer id) {//当前页 页数

        List<WxUserInfo> rankList = null;//查询排名数据
        Map<String, Object> resultMap = new HashMap<>();//返回数据

        Page<WxUserInfo> pageOrder = new Page<>(page, pageSize);

        QueryWrapper<WxUserInfo> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("duration");

        Page<WxUserInfo> orderResult = wxUserInfoService.page(pageOrder, wrapper);

        rankList = orderResult.getRecords();

        if (id == -1) {
            resultMap.put("myrank", -1);
        } else {
            for (WxUserInfo user : rankList) {
                if (id.equals(user.getId())) {
                    resultMap.put("myrank", rankList.indexOf(user) + 1);

                }
            }
        }

        resultMap.put("total", orderResult.getTotal());
        resultMap.put("totalPage", orderResult.getPages());

        resultMap.put("ranklist", rankList);
        return Result.success(resultMap);
    }

    //按照关键字查询
    @GetMapping("/key/{filter}/{order}/{gender}")
    public Result<PageInfo<WxUserInfo>> searchdetail(@RequestParam(defaultValue = "1") Integer pageNum,
                                                     @RequestParam(defaultValue = "15") Integer pageSize,
                                                     @PathVariable Integer filter, @RequestParam String start,
                                                     @RequestParam String end, @PathVariable Integer order, @PathVariable Integer gender) throws Exception {
        QueryWrapper<WxUserInfo> queryWrapper = new QueryWrapper();
        PageHelper.startPage(pageNum, pageSize);
       if(filter==0){
           if(!start.equals("null")&&!start.equals("")){
               Double s=Double.valueOf(start);
               queryWrapper.ge("duration",s);

           }
           System.out.println(end);
           if(!end.equals("null")&&!end.equals("")){
               Double e=Double.valueOf(end);
               queryWrapper.le("duration",e);
           }
       }
       if (filter==1){
           if(!start.equals("null")&&!start.equals("")){
               Double s=Double.valueOf(start);
               queryWrapper.ge("credit",s);

           }
           if(!end.equals("null")&&!end.equals("")){
               Double e=Double.valueOf(end);
               queryWrapper.le("credit",e);
           }
       }


        if (gender != 3) {
            queryWrapper.eq("sex", gender);
        }
        if (order == 1&&filter==0) {
            queryWrapper.orderByDesc("duration");
        }
        if (order == 0&&filter==0) {
            queryWrapper.orderByAsc("duration");
        }
        if (order == 1&&filter==1) {
            queryWrapper.orderByDesc("credit");
        }
        if (order == 0&&filter==1) {
            queryWrapper.orderByAsc("credit");
        }

        List<WxUserInfo> wxUserInfoList = wxUserInfoMapper.selectList(queryWrapper);
        return Result.success(PageInfo.of(wxUserInfoList));
    }


    @GetMapping("/detail/{id}")
    public Result<WxUserInfo> detail(@PathVariable Integer id) {
        WxUserInfo wxUserInfo = wxUserInfoMapper.selectById(id);
        return Result.success(wxUserInfo);
    }


    @GetMapping("/page/rank")
    public Result<PageInfo<WxUserInfo>> rank(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "15") Integer pageSize,
                                         @RequestParam Integer order,@RequestParam Integer way
    ) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<WxUserInfo> queryWrapper=new QueryWrapper<>();
        List<WxUserInfo> list = new ArrayList<>();

        if (way==1 && order==0) {//阳 升
            queryWrapper.orderByAsc("credit");
        }
        if(way == 1 && order==1){//阳 降
            queryWrapper.orderByDesc("credit");
        }
        if(way == 0 && order==0){//志 升
            queryWrapper.orderByAsc("duration");
        }
        if(way == 0 && order==1){//志 降
            queryWrapper.orderByDesc("duration");
        }
        list= wxUserInfoService.list(queryWrapper);
        return Result.success(PageInfo.of(list));


    }

}
