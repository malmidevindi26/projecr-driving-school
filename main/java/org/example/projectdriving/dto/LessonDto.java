package org.example.projectdriving.dto;

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
    private String studentId;
    private String courseId;
    private String instructorId;
    private String status;
}
