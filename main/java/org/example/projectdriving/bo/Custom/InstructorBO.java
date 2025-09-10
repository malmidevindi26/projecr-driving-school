package org.example.projectdriving.bo.Custom;

import org.example.projectdriving.bo.SuperBO;
import org.example.projectdriving.dto.InstructorDto;

import java.sql.SQLException;
import java.util.List;

public interface InstructorBO extends SuperBO {

    List<InstructorDto> getAllInstructors() throws SQLException;

    void saveInstructor(InstructorDto dto) throws SQLException;

    void updateInstructor(InstructorDto dto) throws SQLException;

    boolean deleteInstructor(String id) throws SQLException;

    String getNextId() throws SQLException;
}
