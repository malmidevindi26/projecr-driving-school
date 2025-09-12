package org.example.projectdriving.dao.custome;

import org.example.projectdriving.dao.CrudDAO;
import org.example.projectdriving.entity.CourseEntity;
import org.example.projectdriving.entity.LessonEntity;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface LessonDAO extends CrudDAO<LessonEntity> {
    List<LessonEntity> findByInstructorAndDate(String instructorId, LocalDate date) throws SQLException;

}
