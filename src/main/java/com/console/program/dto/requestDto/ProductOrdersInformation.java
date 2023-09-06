package com.console.program.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductOrdersInformation {
    private Integer productNumber;
    private Integer productQuantity;
}
