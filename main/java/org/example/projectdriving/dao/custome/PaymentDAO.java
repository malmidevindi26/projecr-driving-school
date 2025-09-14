package org.example.projectdriving.dao.custome;

import org.example.projectdriving.dao.CrudDAO;
import org.example.projectdriving.entity.CourseEntity;
import org.example.projectdriving.entity.PaymentEntity;

import java.sql.SQLException;
import java.util.List;

public interface PaymentDAO extends CrudDAO<PaymentEntity> {
    List<PaymentEntity> findPaymentsByStudentId(String studentId) throws SQLException;
    List<PaymentEntity> findPaymentsByStudentIdAndCourse(String studentId, String courseName) throws SQLException;
}
