package com.idle.entity.orders;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Orders {
    Long id;
    String product_name;
    String create_time;
    String seller;
    String buyer;
    String payment;
    Integer status;
    BigDecimal price;
    String shipping_address;
}
