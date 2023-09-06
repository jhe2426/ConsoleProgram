package com.console.program.dto.requestDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCouponApplicationOrdersRequestDto {
    List<CouponApplicationOrdersInformation> productOrdersInformationList;
    private Integer couponNumber;
}
