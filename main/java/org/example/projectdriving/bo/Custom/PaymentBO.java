package org.example.projectdriving.bo.Custom;

import org.example.projectdriving.bo.SuperBO;
import org.example.projectdriving.dto.PaymentDto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface PaymentBO extends SuperBO {

    List<PaymentDto> getAllPayments() throws SQLException;

    void savePayment(PaymentDto dto) throws SQLException;

    void updatePayment(PaymentDto dto) throws SQLException;

    boolean deletePayment(String id) throws SQLException;

    String getNextId() throws SQLException;

    BigDecimal getEnrolledCourseId(String studentId, String courseName);

    boolean isFullPaymentCompleted(String studentId) throws SQLException;

    List<String> getEnrolledCourses(String studentId)  throws SQLException;

    BigDecimal getCourseFee(String courseId) throws SQLException;

    int getPaidInstallments(String studentId, String courseId) throws SQLException;

    BigDecimal getEnrolledCourseFee(String studentId, String courseName) throws SQLException;
}

