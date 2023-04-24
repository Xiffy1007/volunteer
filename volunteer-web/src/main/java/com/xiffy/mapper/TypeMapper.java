package com.xiffy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiffy.entity.Complete;
import com.xiffy.entity.Type;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * 活动类型Mapper接口

 */
public interface TypeMapper extends BaseMapper<Type> {

    @Select("SELECT COUNT(*) FROM t_signup WHERE  credit>0 AND activityId IN (SELECT id FROM t_activity WHERE typeId=#{typeId})")
    Integer findcom(Integer typeId);
    @Select("SELECT COUNT(*) FROM t_signup WHERE activityId IN (SELECT id FROM t_activity WHERE typeId=#{typeId})")
    Integer findcy(Integer typeId);
}
