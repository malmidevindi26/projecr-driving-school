package org.example.projectdriving.bo.Custom;

import org.example.projectdriving.bo.SuperBO;
import org.example.projectdriving.dto.StudentDto;

import java.sql.SQLException;
import java.util.List;

public interface StudentBO extends SuperBO {

    List<StudentDto> getAllStudents() throws SQLException;

    boolean saveStudent(StudentDto dto) throws SQLException;

    boolean updateStudent(StudentDto dto) throws SQLException;

    boolean deleteStudent(String id) throws SQLException;

    String getNextId() throws SQLException;

    StudentDto getStudentById(String id) throws SQLException;
}
