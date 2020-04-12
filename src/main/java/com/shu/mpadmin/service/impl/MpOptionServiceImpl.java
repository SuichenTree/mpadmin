package com.shu.mpadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shu.mpadmin.entity.MpOption;
import com.shu.mpadmin.mapper.MpOptionMapper;
import com.shu.mpadmin.service.MpOptionService;
import org.springframework.stereotype.Service;

@Service
public class MpOptionServiceImpl extends ServiceImpl<MpOptionMapper, MpOption> implements MpOptionService {
}
