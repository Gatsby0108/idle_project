package com.idle.controller;

import com.idle.entity.RestBean;
import com.idle.entity.orders.Orders;
import com.idle.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    @Resource
    OrderService orderService;

    @PostMapping("/set-order")
    public RestBean<String> setOrder(Integer id, String create_time, String seller, String buyer, String payment,
                                     BigDecimal price){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);
        boolean orders = orderService.createOrder(id, time, seller, buyer, payment, price);
        if(orders) return RestBean.success("订单创建成功");
        else return RestBean.failure(401,"订单创建失败，请联系管理员");
    }

    @PostMapping("/get-own-order")
    public RestBean<?> getUserOrder(String username){
        List<Orders> orders = orderService.getUserOrder(username);
        if(orders == null || orders.size() == 0) return RestBean.failure(401,"用户还未创建订单");
        else return RestBean.success(orders);
    }
}