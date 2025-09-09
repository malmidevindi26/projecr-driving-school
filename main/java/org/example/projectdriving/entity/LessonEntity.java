package org.example.projectdriving.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "lessons",
        indexes = {@Index(columnList = "startTime"), @Index(columnList = "instructor_id"), @Index(columnList = "student_id")})
public class LessonEntity {
   @Id
   private String id;

   private java.time.LocalDateTime startTime;
   private java.time.LocalDateTime endTime;

   private String status;
}
