package com.shu.mpadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shu.mpadmin.entity.MpUser;
import com.shu.mpadmin.mapper.MpUserMapper;
import com.shu.mpadmin.service.MpUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//该service层实现类已继承实现了mybatis-plus通用的service接口

@Service
public class MpUserServieImpl extends ServiceImpl<MpUserMapper, MpUser> implements MpUserService {

    @Autowired
    private MpUserMapper mapper;

    @Override
    public MpUser findByopenId(String openId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("wx_openid",openId);
        MpUser one = mapper.selectOne(queryWrapper);
        return one;
    }
}
