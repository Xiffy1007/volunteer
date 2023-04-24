package com.xiffy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiffy.entity.Activity;
import com.xiffy.entity.Credit;
import com.xiffy.mapper.ActivityMapper;
import com.xiffy.mapper.CreditMapper;
import com.xiffy.service.IActivityService;
import com.xiffy.service.ICreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 阳关信用Service实现类

 */
@Service("creditService")
public class ICreditServiceImpl extends ServiceImpl<CreditMapper, Credit> implements ICreditService {

    @Autowired
    private CreditMapper creditMapper;
}
