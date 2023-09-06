package com.console.program.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetMartTokenResponseDto{
    private String token;
    private int expirationDate;
}
