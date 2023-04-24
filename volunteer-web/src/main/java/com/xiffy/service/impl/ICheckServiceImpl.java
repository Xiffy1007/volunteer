package com.xiffy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiffy.entity.Check;
import com.xiffy.mapper.CheckMapper;
import com.xiffy.service.ICheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 审核Service实现类

 */
@Service("checkService")
public class ICheckServiceImpl extends ServiceImpl<CheckMapper, Check> implements ICheckService {

    @Autowired
    private CheckMapper checkMapper;
}
