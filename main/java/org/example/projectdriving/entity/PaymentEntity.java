package org.example.projectdriving.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    private String id;

    private BigDecimal amount;

    private String method;

    private String status;

    private String reference;
}
