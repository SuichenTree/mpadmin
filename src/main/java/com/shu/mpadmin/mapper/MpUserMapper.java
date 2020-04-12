package com.shu.mpadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shu.mpadmin.entity.MpUser;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface MpUserMapper extends BaseMapper<MpUser> {
    //编写自定义方法
    @Select("select * from mp_user where id = #{id}")
    public MpUser findByid(Integer id);
}
