package org.example.projectdriving.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class StudentDto {

    private String id;

    private String fullName;

    private String email;

    private String phone;

    private String nic;

    //private String course;
    private List<String> courseIds;
}
