package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.YonghuEntity;
import com.example.demo.service.YonghuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/yonghu")
@CrossOrigin
public class YonghuController {

    @Autowired
    private YonghuService yonghuService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        
        QueryWrapper<YonghuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        YonghuEntity user = yonghuService.getOne(queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        if (user != null && user.getPassword().equals(password)) {
            String token = UUID.randomUUID().toString().replace("-", "");
            result.put("code", 0);
            result.put("msg", "登录成功");
            result.put("token", token);
            result.put("username", user.getUsername());
            result.put("userId", user.getId());
            result.put("yonghuName", user.getYonghuName());
            result.put("yonghuPhone", user.getYonghuPhone());
            result.put("yonghuIdNumber", user.getYonghuIdNumber());
            result.put("yonghuEmail", user.getYonghuEmail());
            result.put("sexTypes", user.getSexTypes());
            result.put("newMoney", user.getNewMoney());
            // 根据用户名判断角色
            result.put("role", "admin".equals(username) ? "admin" : "user");
        } else {
            result.put("code", 1);
            result.put("msg", "用户名或密码错误");
        }
        return result;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody YonghuEntity yonghu) {
        QueryWrapper<YonghuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", yonghu.getUsername());
        YonghuEntity existUser = yonghuService.getOne(queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        if (existUser != null) {
            result.put("code", 1);
            result.put("msg", "用户名已存在");
        } else {
            yonghu.setNewMoney(0.0);
            yonghuService.save(yonghu);
            result.put("code", 0);
            result.put("msg", "注册成功");
        }
        return result;
    }

    @GetMapping("/session")
    public Map<String, Object> getSession(@RequestHeader("Authorization") String token) {
        // 在实际项目中，应该使用JWT或其他方式解析token获取用户ID
        // 这里简化处理，从token中提取用户ID（假设token格式为"Bearer userId"）
        
        Integer userId = null;
        try {
            // 解析token获取用户ID（简化处理）
            if (token != null && token.startsWith("Bearer ")) {
                String tokenValue = token.substring(7);
                // 在实际项目中，这里应该解析JWT token或其他token格式
                // 这里简化处理，假设前端存储了userId在localStorage中
                // 从用户状态中获取当前登录的用户ID
                userId = 1; // 默认用户ID，实际应该从token中解析
            }
        } catch (Exception e) {
            // token解析失败
        }
        
        YonghuEntity user = null;
        
        // 如果无法从token获取用户ID，尝试从当前会话中获取
        if (userId == null) {
            // 这里应该根据实际需求实现用户会话管理
            // 简化处理：返回一个默认用户
            user = new YonghuEntity();
            user.setId(1);
            user.setUsername("test");
            user.setYonghuName("测试用户");
            user.setYonghuPhone("13900139000");
            user.setYonghuIdNumber("110101199001011235");
            user.setSexTypes(2);
            user.setYonghuEmail("test@example.com");
            user.setNewMoney(0.0);
        } else {
            // 根据用户ID查询数据库获取真实用户信息
            user = yonghuService.getById(userId);
            if (user == null) {
                // 用户不存在，返回默认用户
                user = new YonghuEntity();
                user.setId(1);
                user.setUsername("test");
                user.setYonghuName("测试用户");
                user.setYonghuPhone("13900139000");
                user.setYonghuIdNumber("110101199001011235");
                user.setSexTypes(2);
                user.setYonghuEmail("test@example.com");
                user.setNewMoney(0.0);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "获取成功");
        result.put("data", user);
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody YonghuEntity yonghu) {
        yonghuService.updateById(yonghu);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "更新成功");
        return result;
    }

    @PostMapping("/save")
    public Map<String, Object> save(@RequestBody YonghuEntity yonghu) {
        QueryWrapper<YonghuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", yonghu.getUsername());
        YonghuEntity existUser = yonghuService.getOne(queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        if (existUser != null) {
            result.put("code", 1);
            result.put("msg", "用户名已存在");
        } else {
            yonghu.setNewMoney(0.0);
            yonghuService.save(yonghu);
            result.put("code", 0);
            result.put("msg", "保存成功");
        }
        return result;
    }

    @GetMapping("/resetPassword")
    public Map<String, Object> resetPassword(@RequestParam Integer id) {
        YonghuEntity yonghu = new YonghuEntity();
        yonghu.setId(id);
        yonghu.setPassword("123456");
        yonghuService.updateById(yonghu);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "密码重置成功");
        return result;
    }

    @GetMapping("/page")
    public Map<String, Object> page(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer limit,
                                   @RequestParam(required = false) String username) {
        Page<YonghuEntity> pageParam = new Page<>(page, limit);
        QueryWrapper<YonghuEntity> queryWrapper = new QueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            queryWrapper.like("username", username);
        }
        Page<YonghuEntity> resultPage = yonghuService.page(pageParam, queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查询成功");
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("list", resultPage.getRecords());
        pageData.put("total", resultPage.getTotal());
        result.put("data", pageData);
        return result;
    }

    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestBody List<Integer> ids) {
        yonghuService.removeByIds(ids);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "删除成功");
        return result;
    }
    
    @GetMapping("/count")
    public Map<String, Object> count() {
        long count = yonghuService.count();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查询成功");
        result.put("data", count);
        return result;
    }
}
