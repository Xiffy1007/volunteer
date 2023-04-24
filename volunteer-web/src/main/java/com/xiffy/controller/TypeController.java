package com.xiffy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiffy.common.Result;
import com.xiffy.entity.*;
import com.xiffy.mapper.SignUpMapper;
import com.xiffy.mapper.TypeMapper;
import com.xiffy.mapper.WxUserInfoMapper;
import com.xiffy.service.IActivityService;
import com.xiffy.service.ISignUpService;
import com.xiffy.service.ITypeService;
import com.xiffy.service.IWxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private ITypeService typeService;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private SignUpMapper signUpMapper;

    @Autowired
    private ISignUpService signUpService;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private IWxUserInfoService wxUserInfoService;

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;

    //分页查询
    @GetMapping("/page/{name}")
    public Result<PageInfo<Type>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @PathVariable String name) {
        PageHelper.startPage(pageNum, pageSize);
        if(name.equals("all")){//查询所有
            List<Type> list = typeService.list(new QueryWrapper<Type>().orderByDesc("createTime"));
            return Result.success(PageInfo.of(list));

        }else {
            List<Type> list=typeService.list(new QueryWrapper<Type>().like("name",name).or().like("description",name).orderByDesc("createTime"));
            return Result.success(PageInfo.of(list));
        }
    }

    //删除类别
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        int typeId=0;
        Type Detype =typeMapper.selectOne(new QueryWrapper<Type>().eq("name","默认"));
        if(Detype==null){//如果不存在默认种类则添加
            Type type=new Type();
            type.setName("默认");
            type.setCreateTime(new Date());//设置创建时间
            int res1=typeMapper.insert(type);//插入默认种类

            typeId=typeMapper.selectOne(new QueryWrapper<Type>().eq("name","默认")).getId();
        }else{

            typeId=Detype.getId();
            List<Activity> acList=activityService.list(new QueryWrapper<Activity>().eq("typeId",id));
            if(acList.size()==0){
                int res=typeMapper.delete(new QueryWrapper<Type>().eq("id",id));
                return Result.success();

            }
        }

        int res=typeMapper.delete(new QueryWrapper<Type>().eq("id",id));
        List<Activity> acList=activityService.list(new QueryWrapper<Activity>().eq("typeId",id));
        for(Activity activity:acList){
            UpdateWrapper wrapper=new UpdateWrapper();
            wrapper.eq("typeId",id);
            activity.setTypeId(typeId);
            Boolean res2=activityService.update(activity,wrapper);

        }
        if(res==1){
            return Result.success();
        }
        else return Result.error();
    }

    @GetMapping("/exist")
    public Result<Boolean> exist(){
        Type Type =typeMapper.selectOne(new QueryWrapper<Type>().eq("name","默认"));
        if(Type!=null){
            List<Activity> acList=activityService.list(new QueryWrapper<Activity>().eq("typeId",Type.getId()));
            if(acList.size()==0){
                return Result.success(true);//可以删除
            }else return Result.success(false);//不能删除
        }
        return Result.success(true);//可以删除

    }
    //修改类别
    @PutMapping("/modify")
    public Result<Type> update(@RequestBody Type type){
        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.eq("id",type.getId());
        type.setModifyTime(new Date());
        typeService.update(type,wrapper);
        return Result.success();
    }

    //新增类别
    @PostMapping("/add")
    public Result<Type> add(@RequestBody Type type) {
        type.setCreateTime(new Date());//设置创建时间
        typeMapper.insert(type);
        return Result.success(type);
    }



    @GetMapping("/findTypes")
    public Result<List<Type>> findtypes(){
        List<Type> typeList=typeService.list();//获取活动的所有类型
        return Result.success(typeList);
    }

    @GetMapping("/findcom")
    public Result<List<Complete>> find(){
        List<Type> typeList=typeService.list();//获取活动的所有类型
        List<Complete> res=new ArrayList<>();
        for (Type type:
             typeList) {
            Complete temp=new Complete();
            temp.setName(type.getName());
            temp.setId(type.getId());
            int cy=typeMapper.findcy(type.getId());
            int com=typeMapper.findcom(type.getId());
            double per=0;
            if(cy==0||com==0){
                per=0;
            }else {
                per=(double)com/(double)cy*100;
            }

            temp.setCyNum(cy);
            temp.setComNum(com);
            temp.setPercent(per);
            res.add(temp);
        }
        return Result.success(res);


    }
    @GetMapping("/findclasscom")
    public Result<List<Complete>> findclass(){
        List<String> typeList=wxUserInfoMapper.getclass();//获取活动的所有类型
        List<Complete> res=new ArrayList<>();
        for (String str:
                typeList) {
            Complete temp=new Complete();
            temp.setClassinfo(str);

            int cy= wxUserInfoMapper.findcy(str);
            int com=wxUserInfoMapper.findcom(str);
            double per=0;
            if(cy==0||com==0){
                per=0;
            }else {
                per=(double)com/(double)cy*100;
            }

            temp.setCyNum(cy);
            temp.setComNum(com);
            temp.setPercent(per);
            res.add(temp);
        }
        return Result.success(res);


    }

}
