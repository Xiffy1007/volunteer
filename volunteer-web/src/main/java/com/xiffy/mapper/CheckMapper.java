package com.xiffy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.xiffy.entity.Check;
import com.xiffy.entity.SignUp;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 审核Mapper接口

 */
public interface CheckMapper extends BaseMapper<Check> {
    @Select("select * from t_check where userId=#{userId} order by createTime desc ")
    Page<Check> findAll(Page<Check> page, Integer userId);

    @Select("select * from t_check where state=#{state} and userId=#{userId} order by createTime desc" )
    Page<Check> findcheck(Page<Check> page, Integer userId, Integer state);

    @Select("select c.*,w.username,w.major,w.classinfo,w.phone from t_check c,t_wxuserinfo w where c.userId=w.id and c.state=#{check} order by c.createTime desc")
    List<Check> find(Integer check);
}
