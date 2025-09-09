package org.example.projectdriving.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class PaymentDto {
    private String id;

    private BigDecimal amount;

    private String method;

    private String status;

    private String reference;
}
