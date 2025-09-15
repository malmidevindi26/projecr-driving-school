package org.example.projectdriving.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String userName;
    private String password;
    private  String role;
}
