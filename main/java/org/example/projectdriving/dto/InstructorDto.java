package org.example.projectdriving.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class InstructorDto {


    private String id;


    private String fullName;


    private String email;


    private String availabilityNote;

}
