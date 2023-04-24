package com.xiffy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiffy.entity.Activity;
import com.xiffy.entity.SignUp;
import com.xiffy.entity.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;


/**
 * 活动报名情况Mapper接口

 */
public interface SignUpMapper extends BaseMapper<SignUp> {
    @Select("select s.*,w.username,a.name,a.actTime,a.endTime from t_activity a,t_signup s,t_wxuserinfo w where a.id=s.activityId and s.userId=w.id order by s.createTime desc")
    List<SignUp> find();

    @Select("select s.*,w.username,a.name,a.actTime,a.endTime from t_activity a,t_signup s,t_wxuserinfo w where a.id=s.activityId and s.userId=w.id and s.scheck=#{check} order by s.createTime desc")
    List<SignUp> find1(Integer check);

    @Select("select w.id,w.userName,w.major,w.classinfo,w.phone,s.createTime,s.id as signupId from t_wxuserinfo w,t_signup s where s.id in (select id from t_signup where activityId=#{id})  AND s.userId=w.id order by s.createTime desc")
    List<User> finddetial1(Integer id);

    @Select("select s.*,w.username,a.name,a.actTime,a.endTime,a.content,a.address,a.leaderName,a.duration from t_activity a,t_signup s,t_wxuserinfo w where a.id=s.activityId and s.userId=w.id and s.id=#{id}")
    SignUp findone(Integer id);

    @Select("select a.* from t_activity a,t_signup s where s.userId=#{id} and s.activityId=a.id and  s.scheck=1 order by s.createTime desc limit 3;")
    List<Activity> find3(Integer id);

    @Select("select a.id as activityId,a.name as name,a.actTime as actTime,s.dur,s.credit from t_activity a,t_signup s where s.userId=#{id} and s.activityId=a.id order by s.createTime desc ;")
    List<SignUp> finduser(Integer id);




}
