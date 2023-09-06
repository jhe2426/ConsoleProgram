package com.console.program.dto.responseDto;

import java.util.List;

import lombok.Data;

@Data
public class GetOrdersResponseDto {
    private int ordersNumber;
    private int userNumber;
    private int totalOrderCount;
    private int orderPrice;
    private int couponPkNumber;
    private int deliveryCharge;
    private List<OrdersProduct> ordersProdcutList;
}
