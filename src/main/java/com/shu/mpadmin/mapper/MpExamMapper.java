package com.shu.mpadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shu.mpadmin.entity.MpExam;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MpExamMapper extends BaseMapper<MpExam> {

    @Select("select * from mp_exam")
    public List<MpExam> selectAll();
}