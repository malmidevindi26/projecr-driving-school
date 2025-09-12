package org.example.projectdriving.dao.custome;

import org.example.projectdriving.dao.CrudDAO;
import org.example.projectdriving.entity.CourseEntity;
import org.example.projectdriving.entity.InstructorEntity;

import java.sql.SQLException;
import java.util.List;

public interface InstructorDAO extends CrudDAO<InstructorEntity> {
  //  List<InstructorEntity> getInstructorsByCourse(String courseId) throws SQLException;
    List<InstructorEntity> findByCourseId(String courseId) throws SQLException;


}
