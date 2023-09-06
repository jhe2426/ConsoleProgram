package com.console.program.dto.responseDto;

import lombok.Data;

@Data
public class ProductPriceRecordSummary implements Comparable<ProductPriceRecordSummary>{
    private int productRecordNumber;
    private int prodcutNumber;
    private String modifyDate;
    

    @Override
    public int compareTo(ProductPriceRecordSummary o) {
        return getProductRecordNumber() - o.getProductRecordNumber();
    }
}
