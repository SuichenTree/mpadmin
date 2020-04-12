package com.shu.mpadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shu.mpadmin.entity.MpUserOption;
import com.shu.mpadmin.mapper.MpUserOptionMapper;
import com.shu.mpadmin.service.MpUserOptionService;
import org.springframework.stereotype.Service;

@Service
public class MpUserOptionServiceImpl extends ServiceImpl<MpUserOptionMapper, MpUserOption> implements MpUserOptionService {
}
