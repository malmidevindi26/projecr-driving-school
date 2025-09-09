package org.example.projectdriving.dto.tm;

import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class LessonTM {

    private String id;

    private java.time.LocalDateTime startTime;
    private java.time.LocalDateTime endTime;

    private String status;
}
