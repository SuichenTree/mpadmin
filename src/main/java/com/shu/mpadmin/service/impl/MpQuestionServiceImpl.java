package com.shu.mpadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shu.mpadmin.entity.MpQuestion;
import com.shu.mpadmin.mapper.MpQuestionMapper;
import com.shu.mpadmin.service.MpQuestionService;
import org.springframework.stereotype.Service;

@Service
public class MpQuestionServiceImpl extends ServiceImpl<MpQuestionMapper, MpQuestion> implements MpQuestionService {

}
