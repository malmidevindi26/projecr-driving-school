package org.example.projectdriving.bo.Custom.impl;

import org.example.projectdriving.bo.Custom.PaymentBO;
import org.example.projectdriving.bo.util.EntityDTOConverter;
import org.example.projectdriving.dao.DAOFactory;
import org.example.projectdriving.dao.DAOTypes;
import org.example.projectdriving.dao.custome.CourseDAO;
import org.example.projectdriving.dao.custome.EnrollmentDAO;
import org.example.projectdriving.dao.custome.PaymentDAO;
import org.example.projectdriving.dao.custome.StudentDAO;
import org.example.projectdriving.dto.PaymentDto;
import org.example.projectdriving.entity.CourseEntity;
import org.example.projectdriving.entity.EnrollmentEntity;
import org.example.projectdriving.entity.PaymentEntity;
import org.example.projectdriving.entity.StudentEntity;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaymentBOImpl implements PaymentBO {
    private final EnrollmentDAO enrollmentDAO = DAOFactory.getInstance().getDAO(DAOTypes.ENROLLMENT);
    private final PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOTypes.PAYMENT);
    private final CourseDAO courseDAO = DAOFactory.getInstance().getDAO(DAOTypes.COURSE);
    private final EntityDTOConverter converter = new EntityDTOConverter();
    private final StudentDAO studentDAO = DAOFactory.getInstance().getDAO(DAOTypes.STUDENT);
    @Override
    public List<PaymentDto> getAllPayments() throws SQLException {
        return paymentDAO.getAll().stream()
                .map(converter::getPaymentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void savePayment(PaymentDto dto) throws SQLException {

        PaymentEntity paymentEntity = converter.getPaymentEntity(dto);
        Optional<StudentEntity> studentOptional = studentDAO.findById(dto.getStudentId());
        studentOptional.ifPresent(paymentEntity::setStudent);
        paymentDAO.save(paymentEntity);
    }

    @Override
    public void updatePayment(PaymentDto dto) throws SQLException {
        PaymentEntity paymentEntity = converter.getPaymentEntity(dto);
        Optional<StudentEntity> studentOptional = studentDAO.findById(dto.getStudentId());
        studentOptional.ifPresent(paymentEntity::setStudent);
        paymentDAO.update(paymentEntity);
    }

    @Override
    public boolean deletePayment(String id) throws SQLException {
       return paymentDAO.delete(id);
    }

    @Override
    public String getNextId() throws SQLException {
        String lastId = paymentDAO.getLastId();
        char tableChar = 'P';
        if(lastId != null){
            String lastIdNumberString = lastId.substring(1);
            int  lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }

    @Override
    public BigDecimal getEnrolledCourseId(String studentId, String courseName) {
        Optional<EnrollmentEntity> enrollment = enrollmentDAO.findByStudentAndCourse(studentId, courseName);
        return enrollment.map(EnrollmentEntity::getAmount).orElse(BigDecimal.ZERO);
    }

    @Override
    public boolean isFullPaymentCompleted(String studentId) throws SQLException {
        List<PaymentEntity> payments = paymentDAO.findPaymentsByStudentId(studentId);
        return payments.stream()
                .anyMatch(p -> "Full Payment".equals(p.getEnrollment()));
    }

    @Override
    public List<String> getEnrolledCourses(String studentId) throws SQLException {
        Optional<StudentEntity> student = studentDAO.findById(studentId);
        if(student.isPresent()) {
            return student.get().getEnrollments().stream()
                    .map(e -> e.getCourse().getName())
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public BigDecimal getCourseFee(String courseName) throws SQLException {
       Optional<CourseEntity> course = courseDAO.findById(courseName);
       return course.map(CourseEntity::getFee).orElse(BigDecimal.ZERO);
    }

    @Override
    public int getPaidInstallments(String studentId, String courseName) throws SQLException {
       List<PaymentEntity> payments = paymentDAO.findPaymentsByStudentIdAndCourse(studentId, courseName);
       return payments.size();
    }

    @Override
    public BigDecimal getEnrolledCourseFee(String studentId, String courseName) throws SQLException {
       Optional<EnrollmentEntity> enrollment = enrollmentDAO.findByStudentAndCourse(studentId, courseName);
       return enrollment.map(EnrollmentEntity::getAmount).orElse(BigDecimal.ZERO);
    }

    @Override
    public boolean isFullPaymentCompleted(String studentId, String courseName) throws SQLException {
        List<PaymentEntity>  payments = paymentDAO.findPaymentsByStudentIdAndCourse(studentId, courseName);
        return payments.stream()
                .anyMatch(p -> "Full Payment".equals(p.getEnrollment()));
    }
}
