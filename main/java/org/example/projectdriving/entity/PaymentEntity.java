package org.example.projectdriving.entity;

import jakarta.persistence.*;
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

    private String course;

    private String enrollment;
    private String reference;

    @ManyToOne
    //@JoinColumn(name = "payment_id", nullable = false)
    private StudentEntity student;
}
