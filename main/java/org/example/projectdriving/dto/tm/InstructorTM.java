package org.example.projectdriving.dto.tm;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class InstructorTM {

    private String id;


    private String fullName;


    private String email;


    private String availabilityNote;

}
