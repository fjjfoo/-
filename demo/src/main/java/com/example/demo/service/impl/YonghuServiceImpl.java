package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.YonghuEntity;
import com.example.demo.mapper.YonghuMapper;
import com.example.demo.service.YonghuService;
import org.springframework.stereotype.Service;

@Service
public class YonghuServiceImpl extends ServiceImpl<YonghuMapper, YonghuEntity> implements YonghuService {
}
