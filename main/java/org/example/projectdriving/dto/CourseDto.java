package org.example.projectdriving.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class CourseDto {


    private String id;


    private String name;


    private String duration;


    private BigDecimal fee;


    private String description;
}
