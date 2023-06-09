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
    public RestBean<String> setOrder(Long id, String product_name, String seller, String buyer,
                                     String payment, BigDecimal price, String shipping_address){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);
        boolean orders = orderService.createOrder(id, product_name, time, seller, buyer, payment, price, shipping_address);
        if(orders) return RestBean.success("订单创建成功");
        else return RestBean.failure(401,"订单创建失败，请联系管理员");
    }

    @PostMapping("/get-own-order")
    public RestBean<?> getUserOrder(String nickname){
        List<Orders> orders = orderService.getUserOrder(nickname);
        if(orders == null || orders.size() == 0) return RestBean.failure(401,"用户还未创建订单");
        else return RestBean.success(orders);
    }

    @PostMapping("/get-id-order")
    public RestBean<?> getOrderById(Long id, String nickname){
        List<Orders> orders = orderService.getOrderById(id,nickname);
        if(orders == null || orders.size() == 0) return RestBean.failure(401,"未查询到指定订单");
        else return RestBean.success(orders);
    }

    @PostMapping("/check-order")
    public RestBean<String> checkOrder(Long id) {
        boolean orders = orderService.checkOrder(id);
        if(orders) return RestBean.success("订单确认成功！交易完成！");
        else return RestBean.failure(500, "内部错误，请联系管理员");
    }
    @PostMapping("/delete-order")
    public RestBean<String> deleteOrder(Long id){
        boolean status = orderService.deleteOrder(id);
        if(status) return RestBean.success("订单取消成功");
        else return RestBean.failure(500,"内部错误，请联系管理员");
    }
}
