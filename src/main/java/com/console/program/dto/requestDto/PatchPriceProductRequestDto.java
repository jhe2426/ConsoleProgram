package com.console.program.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatchPriceProductRequestDto {
    private int productNumber;
    private int price;
}
