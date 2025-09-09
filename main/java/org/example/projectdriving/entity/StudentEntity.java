package org.example.projectdriving.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "payments")
public class StudentEntity {
    @Id
    private String id;

    private String fullName;

    private String email;

    private String phone;

    private String nic;

    private String course;
}
