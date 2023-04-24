package com.xiffy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiffy.entity.News;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 新闻Mapper接口

 */
public interface NewsMapper extends BaseMapper<News> {
    @Select("select id from t_news  Order By createTime Desc limit 5")
    List<Integer> findId();

    @Select("SELECT COUNT(*) as `count`, t.typeName as `name` FROM (SELECT n.*,t.name AS typeName FROM t_news n,t_newstype t WHERE n.typeId=t.id) AS t GROUP BY typeId")
    List<Map<String,Object>> getTypeNews();


}
