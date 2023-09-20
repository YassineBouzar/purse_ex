package com.purse.ex.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Document
@Builder
@Data
public class Transaction {
    @Id
    private String id;
    private BigDecimal total;
    private Type type;
    private Status status;
    List<OrderLine> orderLineList= new ArrayList<>();
}
