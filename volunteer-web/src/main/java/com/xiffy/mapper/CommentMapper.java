package com.xiffy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiffy.entity.Comment;
import com.xiffy.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * 评论Mapper接口

 */
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("select c.* ,w.userName,w.sex from t_wxuserinfo w,t_comment c where c.id in (select id from t_comment where newsId=#{id})  AND c.userId=w.id order by c.createTime desc")
    List<Comment> findlist(Integer id);

    @Select("SELECT COUNT(*) AS `count`,t.name AS `name` FROM (SELECT m.typeId,n.name FROM (SELECT n.typeId FROM t_comment c ,t_news n WHERE c.newsId=n.id) m,t_newstype n WHERE n.id=m.typeId) t GROUP BY t.typeId")
    List<Map<String,Object>> getTypecomment();
}
