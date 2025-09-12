package org.example.projectdriving.dao.custome;

import org.example.projectdriving.dao.CrudDAO;
import org.example.projectdriving.entity.CourseEntity;

import java.sql.SQLException;
import java.util.List;

public interface CourseDAO extends CrudDAO<CourseEntity> {
    // CourseDAO.java
    List<CourseEntity> findCoursesByStudentId(String studentId) throws SQLException;

}
