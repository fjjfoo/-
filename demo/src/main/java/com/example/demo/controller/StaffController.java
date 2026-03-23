package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.StaffEntity;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staff")
@CrossOrigin
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer limit,
                                   @RequestParam(required = false) String staffName,
                                   @RequestParam(required = false) Integer staffStatus) {
        Page<StaffEntity> pageParam = new Page<>(page, limit);
        QueryWrapper<StaffEntity> queryWrapper = new QueryWrapper<>();
        
        if (staffName != null && !staffName.isEmpty()) {
            queryWrapper.like("staff_name", staffName);
        }
        if (staffStatus != null) {
            queryWrapper.eq("staff_status", staffStatus);
        }
        queryWrapper.orderByDesc("id");
        
        Page<StaffEntity> resultPage = staffService.page(pageParam, queryWrapper);
        
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
        StaffEntity staff = staffService.getById(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查询成功");
        result.put("staff", staff);
        return result;
    }

    @PostMapping("/save")
    public Map<String, Object> save(@RequestBody StaffEntity staff) {
        staffService.save(staff);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "保存成功");
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody StaffEntity staff) {
        staffService.updateById(staff);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "更新成功");
        return result;
    }

    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestBody List<Integer> ids) {
        staffService.removeByIds(ids);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "删除成功");
        return result;
    }
    
    @PostMapping("/updateStatus")
    public Map<String, Object> updateStatus(@RequestBody Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        Integer status = (Integer) params.get("status");
        
        StaffEntity staff = staffService.getById(id);
        staff.setStaffStatus(status);
        staffService.updateById(staff);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "状态更新成功");
        return result;
    }
    
    @GetMapping("/count")
    public Map<String, Object> count() {
        long count = staffService.count();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查询成功");
        result.put("data", count);
        return result;
    }
}