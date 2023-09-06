package com.console.program.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetTokenResponseDto{
    private String token;
    private int expirationDate;
}
