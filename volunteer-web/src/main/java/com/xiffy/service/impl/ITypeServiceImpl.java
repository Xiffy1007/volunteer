package com.xiffy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiffy.entity.Type;
import com.xiffy.mapper.TypeMapper;
import com.xiffy.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 活动类型Service实现类

 */
@Service("typeService")
public class ITypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements ITypeService {

    @Autowired
    private TypeMapper typeMapper;
}
