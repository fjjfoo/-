package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.MeirongOrderEntity;
import com.example.demo.entity.MeirongServiceEntity;
import com.example.demo.entity.StaffEntity;
import com.example.demo.service.MeirongOrderService;
import com.example.demo.service.MeirongServiceService;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/meirongOrder")
@CrossOrigin
public class MeirongOrderController {

    @Autowired
    private MeirongOrderService meirongOrderService;

    @Autowired
    private MeirongServiceService meirongServiceService;

    @Autowired
    private StaffService staffService;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer limit,
                                    @RequestParam(required = false) Integer yonghuId,
                                    @RequestParam(required = false) Integer orderStatus,
                                    @RequestParam(required = false) String orderUuidNumber) {
        Page<MeirongOrderEntity> pageParam = new Page<>(page, limit);
        QueryWrapper<MeirongOrderEntity> queryWrapper = new QueryWrapper<>();
        
        if (yonghuId != null) {
            queryWrapper.eq("yonghu_id", yonghuId);
        }
        if (orderStatus != null) {
            queryWrapper.eq("order_status", orderStatus);
        }
        if (orderUuidNumber != null && !orderUuidNumber.isEmpty()) {
            queryWrapper.like("order_uuid_number", orderUuidNumber);
        }
        queryWrapper.orderByDesc("id");
        
        Page<MeirongOrderEntity> resultPage = meirongOrderService.page(pageParam, queryWrapper);
        
        // 处理订单数据，添加服务师名称
        List<Map<String, Object>> orderList = new ArrayList<>();
        for (MeirongOrderEntity order : resultPage.getRecords()) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("orderUuidNumber", order.getOrderUuidNumber());
            orderMap.put("serviceId", order.getServiceId());
            orderMap.put("staffId", order.getStaffId());
            orderMap.put("yonghuId", order.getYonghuId());
            orderMap.put("orderTruePrice", order.getOrderTruePrice());
            orderMap.put("orderStatus", order.getOrderStatus());
            orderMap.put("shijianduan", order.getShijianduan());
            orderMap.put("buyTime", order.getBuyTime());
            orderMap.put("insertTime", order.getInsertTime());
            orderMap.put("createTime", order.getCreateTime());
            orderMap.put("payStatus", order.getPayStatus());
            orderMap.put("payTime", order.getPayTime());
            orderMap.put("orderComment", order.getOrderComment());
            orderMap.put("orderRating", order.getOrderRating());
            
            // 查询服务师名称
            if (order.getStaffId() != null && order.getStaffId() > 0) {
                StaffEntity staff = staffService.getById(order.getStaffId());
                if (staff != null) {
                    orderMap.put("staffName", staff.getStaffName());
                }
            }
            
            orderList.add(orderMap);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查询成功");
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("list", orderList);
        pageData.put("total", resultPage.getTotal());
        result.put("page", pageData);
        return result;
    }

    @GetMapping("/info/{id}")
    public Map<String, Object> info(@PathVariable Integer id) {
        MeirongOrderEntity meirongOrder = meirongOrderService.getById(id);
        
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("id", meirongOrder.getId());
        orderMap.put("orderUuidNumber", meirongOrder.getOrderUuidNumber());
        orderMap.put("serviceId", meirongOrder.getServiceId());
        orderMap.put("staffId", meirongOrder.getStaffId());
        orderMap.put("yonghuId", meirongOrder.getYonghuId());
        orderMap.put("orderTruePrice", meirongOrder.getOrderTruePrice());
        orderMap.put("orderStatus", meirongOrder.getOrderStatus());
        orderMap.put("shijianduan", meirongOrder.getShijianduan());
        orderMap.put("buyTime", meirongOrder.getBuyTime());
        orderMap.put("insertTime", meirongOrder.getInsertTime());
        orderMap.put("createTime", meirongOrder.getCreateTime());
        orderMap.put("payStatus", meirongOrder.getPayStatus());
        orderMap.put("payTime", meirongOrder.getPayTime());
        orderMap.put("orderComment", meirongOrder.getOrderComment());
        orderMap.put("orderRating", meirongOrder.getOrderRating());
        
        // 查询服务师名称
        if (meirongOrder.getStaffId() != null && meirongOrder.getStaffId() > 0) {
            StaffEntity staff = staffService.getById(meirongOrder.getStaffId());
            if (staff != null) {
                orderMap.put("staffName", staff.getStaffName());
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查询成功");
        result.put("meirongOrder", orderMap);
        return result;
    }

    @PostMapping("/save")
    public Map<String, Object> save(@RequestBody MeirongOrderEntity meirongOrder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderNumber = "MR" + sdf.format(new Date()) + UUID.randomUUID().toString().substring(0, 4);
        meirongOrder.setOrderUuidNumber(orderNumber);
        meirongOrder.setInsertTime(new Date());
        meirongOrder.setCreateTime(new Date());
        
        meirongOrderService.save(meirongOrder);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "预约成功");
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody MeirongOrderEntity meirongOrder) {
        meirongOrderService.updateById(meirongOrder);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "更新成功");
        return result;
    }

    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestBody List<Integer> ids) {
        meirongOrderService.removeByIds(ids);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "删除成功");
        return result;
    }
    
    @GetMapping("/count")
    public Map<String, Object> count() {
        long count = meirongOrderService.count();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查询成功");
        result.put("data", count);
        return result;
    }
    
    @GetMapping("/totalRevenue")
    public Map<String, Object> totalRevenue() {
        // 计算已支付订单的总收入
        QueryWrapper<MeirongOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pay_status", 1); // 假设1表示已支付
        queryWrapper.select("COALESCE(SUM(order_true_price), 0) as total_revenue");
        Map<String, Object> resultMap = meirongOrderService.getMap(queryWrapper);
        
        double total = 0.0;
        if (resultMap != null && resultMap.get("total_revenue") != null) {
            total = Double.parseDouble(resultMap.get("total_revenue").toString());
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查询成功");
        result.put("data", total);
        return result;
    }
    
    @PostMapping("/checkStaffAvailability")
    public Map<String, Object> checkStaffAvailability(@RequestBody Map<String, Object> params) {
        Integer staffId = (Integer) params.get("staffId");
        String buyTime = (String) params.get("buyTime");
        String shijianduan = (String) params.get("shijianduan");
        
        QueryWrapper<MeirongOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("staff_id", staffId);
        queryWrapper.eq("buy_time", buyTime);
        queryWrapper.eq("shijianduan", shijianduan);
        queryWrapper.in("order_status", 1, 2, 3); // 待确认、已确认、服务中状态的订单
        
        long count = meirongOrderService.count(queryWrapper);
        boolean available = count == 0;
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "查询成功");
        result.put("available", available);
        return result;
    }
}
