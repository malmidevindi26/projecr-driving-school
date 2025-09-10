package org.example.projectdriving.dao.custome;

import org.example.projectdriving.dao.CrudDAO;
import org.example.projectdriving.entity.CourseEntity;
import org.example.projectdriving.entity.StudentEntity;

import java.sql.SQLException;
import java.util.Optional;

public interface StudentDAO extends CrudDAO<StudentEntity> {

    Optional<StudentEntity> findByStudentNic(String nic) throws SQLException;
}
