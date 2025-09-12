package org.example.projectdriving.bo.Custom;

import org.example.projectdriving.bo.SuperBO;
import org.example.projectdriving.dto.CourseDto;

import java.sql.SQLException;
import java.util.List;

public interface CourseBO extends SuperBO {
    List<CourseDto> getAllCourses() throws SQLException;

    void saveCourse(CourseDto dto) throws SQLException;

    void updateCourse(CourseDto dto) throws SQLException;

    boolean deleteCourse(String id) throws SQLException;

    String getNextId() throws SQLException;

    List<String> getAllCoursesName() throws SQLException, ClassNotFoundException;
}
