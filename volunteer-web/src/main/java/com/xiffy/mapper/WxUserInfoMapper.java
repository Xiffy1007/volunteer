package com.xiffy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiffy.entity.SignUp;
import com.xiffy.entity.WxUserInfo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 微信用户Mapper接口
 * @author java1234_小锋
 * @site www.java1234.com
 * @company 南通小锋网络科技有限公司
 * @create 2022-02-23 22:00
 */
public interface WxUserInfoMapper extends BaseMapper<WxUserInfo> {

    public WxUserInfo findByOpenId(String openId);

    //修改志愿时长
    @Update("update t_wxuserinfo set duration=#{dur}+duration where id=#{id}")
    int duration(Double dur,Integer id);

    //修改阳光信用
    @Update("update t_wxuserinfo set credit=#{cre}+credit where id=#{id}")
    int credit(Double cre,Integer id);

    @Select("SELECT COUNT(*) AS `count`,college AS  `name` FROM t_wxuserinfo GROUP BY college")
    List<Map<String,Object>> getTypecollege();

    @Select("SELECT COUNT(*) AS `count`,major AS  `name` FROM t_wxuserinfo GROUP BY major")
    List<Map<String,Object>> getTypemajor();

    @Select("SELECT COUNT(*) AS `count`,classinfo AS `name` FROM t_wxuserinfo GROUP BY classinfo")
    List<Map<String,Object>> getTypeclassinfo();

    @Select("SELECT classinfo FROM t_wxuserinfo GROUP BY classinfo")
    List<String> getclass();

    @Select("SELECT COUNT(*) FROM t_signup WHERE  credit>0 AND userId IN (SELECT id FROM t_wxuserinfo WHERE classinfo=#{str})")
    Integer findcom(String str);
    @Select("SELECT COUNT(*) FROM t_signup WHERE userId IN (SELECT id FROM t_wxuserinfo WHERE classinfo=#{str})")
    Integer findcy(String str);

}
