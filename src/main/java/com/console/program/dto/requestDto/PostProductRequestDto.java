package com.console.program.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostProductRequestDto {

    private String name;

    private Integer price;

    private int deliveryCharge;
}
