package com.purse.ex.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class OrderLine {
    private String name;
    private String ref;
    private Integer quantity;
    private BigDecimal price;
}
