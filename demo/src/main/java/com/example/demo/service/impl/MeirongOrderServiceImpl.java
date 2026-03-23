package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.MeirongOrderEntity;
import com.example.demo.mapper.MeirongOrderMapper;
import com.example.demo.service.MeirongOrderService;
import org.springframework.stereotype.Service;

@Service
public class MeirongOrderServiceImpl extends ServiceImpl<MeirongOrderMapper, MeirongOrderEntity> implements MeirongOrderService {
}
