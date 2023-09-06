package com.console.program.dto.responseDto;

import lombok.Data;

@Data
public class CouponSummary {
    private int couponNumber;
    private String couponName;
    private int applicationPrice;
    private int availableProductNumber;
}
