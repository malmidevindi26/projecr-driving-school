package org.example.projectdriving.entity;

import jakarta.persistence.*;
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

   @ManyToOne
   @JoinColumn(name = "instructor_id", nullable = false)
   private InstructorEntity instructor;

   @ManyToOne
   @JoinColumn(name = "student_id", nullable = false)
   private StudentEntity student;

}
