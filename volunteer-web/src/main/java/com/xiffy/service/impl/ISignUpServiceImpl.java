package com.xiffy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiffy.entity.SignUp;
import com.xiffy.mapper.SignUpMapper;
import com.xiffy.service.ISignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 活动报名Service实现类

 */
@Service("signUpService")
public class ISignUpServiceImpl extends ServiceImpl<SignUpMapper, SignUp> implements ISignUpService {

    @Autowired
    private SignUpMapper signUpMapper;
}
