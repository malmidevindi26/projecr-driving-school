package org.example.projectdriving.dto;

import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class LessonDto {

    private String id;

    private java.time.LocalDateTime startTime;
    private java.time.LocalDateTime endTime;

    private String status;
}
