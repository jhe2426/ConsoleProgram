package com.console.program.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetParticularDateProductPriceRequestDto {
    private String date;
    private int productNumber;
}
