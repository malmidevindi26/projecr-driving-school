package org.example.projectdriving.bo.Custom;

import org.example.projectdriving.bo.SuperBO;
import org.example.projectdriving.dto.StudentDto;

import java.util.List;

public interface StudentBO extends SuperBO {

    List<StudentDto> getAllStudents();

    void saveStudent(StudentDto dto);

    void updateStudent(StudentDto dto);

    boolean deleteStudent(String id);

    String getNextId();
}
