package com.xiffy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiffy.common.Result;
import com.xiffy.entity.Activity;
import com.xiffy.entity.Credit;
import com.xiffy.entity.WxUserInfo;
import com.xiffy.mapper.CreditMapper;
import com.xiffy.mapper.WxUserInfoMapper;
import com.xiffy.service.ICreditService;
import com.xiffy.service.IWxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/credit")
public class CreditController {
    @Autowired
    private CreditMapper creditMapper;

    @Autowired
    private ICreditService iCreditService;

    @Autowired
    private IWxUserInfoService wxUserInfoService;

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;

    //微信小程序
    @RequestMapping("/list")
    public Result<Map<String,Object>> list(Integer page, Integer pageSize, Integer userId){//当前页 页数

        List<Credit> creditsList=null;//查询数据
        Map<String,Object> resultMap=new HashMap<>();//返回数据

        Page<Credit> pageOrder=new Page<>(page,pageSize);

        Page<Credit> creResult = iCreditService.page(pageOrder, new QueryWrapper<Credit>().orderByDesc("createTime").eq("userId",userId));

        creditsList=creResult.getRecords();
        resultMap.put("total",creResult.getTotal());
        resultMap.put("totalPage",creResult.getPages());

        resultMap.put("creditsList",creditsList);

        WxUserInfo wxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("id",userId));
        resultMap.put("totalcredit",wxUserInfo.getCredit());
        return Result.success(resultMap);
    }

    //查询用户的阳光信用
    @GetMapping("/mycredit")
    public Result<Boolean> findCredit(Integer userId){
        Double res=wxUserInfoMapper.selectOne(new QueryWrapper<WxUserInfo>().eq("id",userId)).getCredit();
        if(res<5){
            return Result.success(false);
        }else return Result.success(true);

    }
}
