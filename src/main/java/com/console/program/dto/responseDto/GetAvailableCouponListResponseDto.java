package com.console.program.dto.responseDto;

import java.util.List;

import lombok.Data;

@Data
public class GetAvailableCouponListResponseDto extends ResponseDto {
   private List<CouponSummary> couponList;
}
