package org.example.projectdriving.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class StudentTM {
    private String id;

    private String fullName;

    private String email;

    private String phone;

    private String nic;

    private String course;
}
