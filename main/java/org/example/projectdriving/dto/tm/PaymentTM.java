package org.example.projectdriving.dto.tm;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class PaymentTM {
    private String id;

    private BigDecimal amount;

    private String method;

    private String status;

    private String reference;
}
