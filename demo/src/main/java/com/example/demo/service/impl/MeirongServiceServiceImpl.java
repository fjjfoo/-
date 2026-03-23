package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.MeirongServiceEntity;
import com.example.demo.mapper.MeirongServiceMapper;
import com.example.demo.service.MeirongServiceService;
import org.springframework.stereotype.Service;

@Service
public class MeirongServiceServiceImpl extends ServiceImpl<MeirongServiceMapper, MeirongServiceEntity> implements MeirongServiceService {
}
