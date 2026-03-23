package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.MeirongServiceEntity;
import com.example.demo.service.MeirongServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meirong")
@CrossOrigin
public class MeirongController {

    @Autowired
    private MeirongServiceService meirongServiceService;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer limit,
                                    @RequestParam(required = false) String serviceName,
                                    @RequestParam(required = false) Integer serviceCategory,
                                    @RequestParam(required = false) Integer shangxiaTypes,
                                    @RequestParam(required = false) String orderBy,
                                    @RequestParam(required = false) String sort) {
        Page<MeirongServiceEntity> pageParam = new Page<>(page, limit);
        QueryWrapper<MeirongServiceEntity> queryWrapper = new QueryWrapper<>();
        
        if (serviceName != null && !serviceName.isEmpty()) {
            queryWrapper.like("service_name", serviceName);
        }
        if (serviceCategory != null) {
            queryWrapper.eq("service_category", serviceCategory);
        }
        if (shangxiaTypes != null) {
            queryWrapper.eq("shangxia_types", shangxiaTypes);
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            // 将驼峰命名转换为下划线命名，以匹配数据库列名
            String dbColumnName = orderBy.replaceAll("([A-Z])", "_$1").toLowerCase();
            if ("desc".equalsIgnoreCase(sort)) {
                queryWrapper.orderByDesc(dbColumnName);
            } else {
                queryWrapper.orderByAsc(dbColumnName);
            }
        } else {
            queryWrapper.orderByDesc("id");
        }
        
        Page<MeirongServiceEntity> resultPage = meirongServiceService.page(pageParam, queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查询成功");
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("list", resultPage.getRecords());
        pageData.put("total", resultPage.getTotal());
        result.put("page", pageData);
        return result;
    }

    @GetMapping("/info/{id}")
    public Map<String, Object> info(@PathVariable Integer id) {
        MeirongServiceEntity meirongService = meirongServiceService.getById(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查询成功");
        result.put("meirongService", meirongService);
        return result;
    }

    @PostMapping("/save")
    public Map<String, Object> save(@RequestBody MeirongServiceEntity meirongService) {
        meirongServiceService.save(meirongService);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "保存成功");
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody MeirongServiceEntity meirongService) {
        meirongServiceService.updateById(meirongService);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "更新成功");
        return result;
    }

    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestBody List<Integer> ids) {
        meirongServiceService.removeByIds(ids);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "删除成功");
        return result;
    }
    
    @GetMapping("/count")
    public Map<String, Object> count() {
        long count = meirongServiceService.count();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查询成功");
        result.put("data", count);
        return result;
    }
}
