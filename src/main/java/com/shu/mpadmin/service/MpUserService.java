package com.shu.mpadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shu.mpadmin.entity.MpUser;
import org.apache.ibatis.annotations.Select;

public interface MpUserService extends IService<MpUser> {

    public MpUser findByopenId(String openId);

}
