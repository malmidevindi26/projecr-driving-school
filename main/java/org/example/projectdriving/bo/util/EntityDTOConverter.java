package org.example.projectdriving.bo.util;

import org.example.projectdriving.dto.*;
import org.example.projectdriving.entity.*;

public class EntityDTOConverter {

    public StudentDto getStudentDto(StudentEntity studentEntity) {
        StudentDto dto = new StudentDto();
        dto.setId(studentEntity.getId());
        dto.setFullName(studentEntity.getFullName());
        dto.setEmail(studentEntity.getEmail());
        dto.setPhone(studentEntity.getPhone());
        dto.setCourse(studentEntity.getCourse());
        return dto;
    }
    public StudentEntity getStudentEntity(StudentDto dto) {
        StudentEntity student = new StudentEntity();
        student.setId(dto.getId());
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setCourse(dto.getCourse());
        return student;
    }

    public InstructorDto getInstructorDto(InstructorEntity instructorEntity) {
        InstructorDto dto = new InstructorDto();
        dto.setId(instructorEntity.getId());
        dto.setFullName(instructorEntity.getFullName());
        dto.setPhone(instructorEntity.getPhone());
        dto.setEmail(instructorEntity.getEmail());
        dto.setCourse(instructorEntity.getCourse());
        return dto;
    }
    public InstructorEntity getInstructorEntity(InstructorDto dto) {
        InstructorEntity instructor = new InstructorEntity();
        instructor.setId(dto.getId());
        instructor.setFullName(dto.getFullName());
        instructor.setPhone(dto.getPhone());
        instructor.setEmail(dto.getEmail());
        instructor.setCourse(dto.getCourse());
        return instructor;
    }

    public LessonDto getLessonDto(LessonEntity lessonEntity) {
        LessonDto dto = new LessonDto();
        dto.setId(lessonEntity.getId());
        dto.setStartTime(lessonEntity.getStartTime());
        dto.setEndTime(lessonEntity.getEndTime());
        dto.setStatus(lessonEntity.getStatus());
        return dto;
    }
    public LessonEntity getLessonEntity(LessonDto dto) {
        LessonEntity lesson = new LessonEntity();
        lesson.setId(dto.getId());
        lesson.setStartTime(dto.getStartTime());
        lesson.setEndTime(dto.getEndTime());
        lesson.setStatus(dto.getStatus());
        return lesson;
    }

    public PaymentDto getPaymentDto(PaymentEntity paymentEntity) {
        PaymentDto dto = new PaymentDto();
        dto.setId(paymentEntity.getId());
        dto.setAmount(paymentEntity.getAmount());
        dto.setMethod(paymentEntity.getMethod());
        dto.setStatus(paymentEntity.getStatus());
        dto.setReference(paymentEntity.getReference());
        return dto;
    }
    public PaymentEntity getPaymentEntity(PaymentDto dto) {
        PaymentEntity payment = new PaymentEntity();
        payment.setId(dto.getId());
        payment.setAmount(dto.getAmount());
        payment.setMethod(dto.getMethod());
        payment.setStatus(dto.getStatus());
        payment.setReference(dto.getReference());
        return payment;
    }


    public CourseDto getCourseDto(CourseEntity courseEntity) {
        CourseDto dto = new CourseDto();
        dto.setId(courseEntity.getId());
        dto.setName(courseEntity.getName());
        dto.setDuration(courseEntity.getDuration());
        dto.setFee(courseEntity.getFee());
        dto.setDescription(courseEntity.getDescription());
        return dto;
    }
    public CourseEntity getCourseEntity(CourseDto dto) {
        CourseEntity course = new CourseEntity();
        course.setId(dto.getId());
        course.setName(dto.getName());
        course.setDuration(dto.getDuration());
        course.setFee(dto.getFee());
        course.setDescription(dto.getDescription());
        return course;
    }
}
