package com.xiffy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiffy.entity.Activity;
import com.xiffy.entity.LikeOrCol;
import com.xiffy.entity.News;
import com.xiffy.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 新闻点赞收藏Mapper接口

 */
public interface LikeOrColMapper extends BaseMapper<LikeOrCol> {
    @Select("select w.id,w.userName,w.major,w.classinfo,w.phone,n.createTime,n.id as signupId from t_wxuserinfo w,t_news_likeorcol n where n.id in (select id from t_news_likeorcol where newsId=#{newsId} and type=#{type})  AND n.userId=w.id order by n.createTime desc")
    List<User> finddetial(Integer newsId,Integer type);

    @Select("SELECT t_news.* FROM t_news WHERE id IN (SELECT newsId FROM t_news_likeorcol WHERE userId=#{userId} and type=1)")
    Page<News> findcol(Page<News> page, Integer userId);
    @Select("SELECT t_news.* FROM t_news WHERE id IN (SELECT newsId FROM t_news_likeorcol WHERE userId=#{userId} and type=0)")
    Page<News> findlikes(Page<News> page, Integer userId);
}
