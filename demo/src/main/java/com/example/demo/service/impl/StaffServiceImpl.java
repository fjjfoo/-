package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.StaffEntity;
import com.example.demo.mapper.StaffMapper;
import com.example.demo.service.StaffService;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, StaffEntity> implements StaffService {
}