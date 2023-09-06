package com.console.program.dto.responseDto;

import lombok.Data;

@Data
public class Product {
    private int productNumber;
    private String productName;
    private int productPrice;
    private int productDeliveryCharge;
    private String createDate;
}
