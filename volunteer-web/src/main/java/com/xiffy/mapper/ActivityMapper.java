package com.xiffy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiffy.entity.Activity;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * 志愿活动Mapper接口

 */
public interface ActivityMapper extends BaseMapper<Activity> {
    @Select("select a.* from t_activity a,t_signup b where a.id=b.activityId and a.state=#{state} and b.userId=#{userId}")
    Page<Activity> findActs(Page<Activity> page, Integer userId, Integer state);

    @Select("select a.* from t_activity a,t_signup b where a.id=b.activityId and b.userId=#{userId}")
    Page<Activity> findAllActs(Page<Activity> page,Integer userId);

    @Select("select a.*,t.name as typeName from t_activity a,t_type t where a.id=#{id} and a.typeId=t.id")
    Activity findDetail(Integer id);

    @Select("SELECT COUNT(*) as `count`,  t.typeName as `name`  FROM (SELECT a.*,t.name AS typeName FROM t_activity a,t_type t WHERE a.typeId=t.id) AS t GROUP BY typeId ")
    List<Map<String,Object>> getTypeActivity();

    @Select("select a.* from t_activity a,t_signup b where a.id=b.activityId and b.userId=#{userId} and b.scheck=1")
    Page<Activity> findlist(Page<Activity> page,Integer userId);
}
