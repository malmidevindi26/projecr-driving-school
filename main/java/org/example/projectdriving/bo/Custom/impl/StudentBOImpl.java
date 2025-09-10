package org.example.projectdriving.bo.Custom.impl;

import org.example.projectdriving.bo.Custom.StudentBO;
import org.example.projectdriving.dto.StudentDto;

import java.util.List;

public class StudentBOImpl implements StudentBO {
    @Override
    public List<StudentDto> getAllStudents() {
        return List.of();
    }

    @Override
    public void saveStudent(StudentDto dto) {

    }

    @Override
    public void updateStudent(StudentDto dto) {

    }

    @Override
    public boolean deleteStudent(String id) {
        return false;
    }

    @Override
    public String getNextId() {
        return "";
    }
}
