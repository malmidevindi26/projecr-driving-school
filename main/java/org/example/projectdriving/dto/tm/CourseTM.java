package org.example.projectdriving.dto.tm;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class CourseTM {
    private String id;


    private String name;


    private String duration;


    private BigDecimal fee;


    private String description;
}
