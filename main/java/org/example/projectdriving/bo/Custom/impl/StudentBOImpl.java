package org.example.projectdriving.bo.Custom.impl;

import org.example.projectdriving.bo.Custom.StudentBO;
import org.example.projectdriving.bo.util.EntityDTOConverter;
import org.example.projectdriving.config.FactoryConfiguration;
import org.example.projectdriving.dao.DAOFactory;
import org.example.projectdriving.dao.DAOTypes;
import org.example.projectdriving.dao.custome.CourseDAO;
import org.example.projectdriving.dao.custome.StudentDAO;
import org.example.projectdriving.dto.CourseDto;
import org.example.projectdriving.dto.StudentDto;
import org.example.projectdriving.entity.CourseEntity;
import org.example.projectdriving.entity.EnrollmentEntity;
import org.example.projectdriving.entity.StudentEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentBOImpl implements StudentBO {
    private final EntityDTOConverter converter = new EntityDTOConverter();
    private final CourseDAO courseDAO = DAOFactory.getInstance().getDAO(DAOTypes.COURSE);
    private  final StudentDAO studentDAO = DAOFactory.getInstance().getDAO(DAOTypes.STUDENT);
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();
    @Override
    public List<StudentDto> getAllStudents() throws SQLException {
        List<StudentEntity> entities = studentDAO.getAll();
        List<StudentDto> dtos = new ArrayList<>();
        for (StudentEntity entity : entities) {
            dtos.add(converter.getStudentDto(entity));
        }
        return dtos;
    }


@Override
public boolean saveStudent(StudentDto dto) throws SQLException {
        if (studentDAO.findByStudentNic(dto.getNic()).isPresent()) {
            throw  new SQLException("Student with NIC " + dto.getNic() + " already exists");
        }

    Session session = factoryConfiguration.getSession();
    Transaction transaction = session.beginTransaction();

    try {

        List<CourseEntity> courseEntities = dto.getCourseIds().stream()
                .map(id -> session.get(CourseEntity.class, id))
                .toList();

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(dto.getId());
        studentEntity.setFullName(dto.getFullName());
        studentEntity.setEmail(dto.getEmail());
        studentEntity.setPhone(dto.getPhone());
        studentEntity.setNic(dto.getNic());
        studentEntity.setEnrollments(new ArrayList<>());

        for (CourseEntity course : courseEntities) {
            EnrollmentEntity enrollment = new EnrollmentEntity();
            enrollment.setStudent(studentEntity);
            enrollment.setCourse(course);
            enrollment.setDate(new Date());
            enrollment.setAmount(course.getFee());

            studentEntity.getEnrollments().add(enrollment);

            if (course.getEnrollments() == null) {
                course.setEnrollments(new ArrayList<>());
            }
            course.getEnrollments().add(enrollment);
        }

        session.persist(studentEntity);
        transaction.commit();
        return true;

    } catch (Exception e) {
        if (transaction != null) transaction.rollback();
        throw new SQLException("Failed to save student with enrollments", e);
    } finally {
        session.close();
    }
}


@Override
public boolean updateStudent(StudentDto dto) throws SQLException {

    Session session = factoryConfiguration.getSession();
    Transaction transaction = session.beginTransaction();

    try {
        StudentEntity existing = session.get(StudentEntity.class, dto.getId());
        if (existing == null) {
            return false;
        }

        Optional<StudentEntity> nicOwner = studentDAO.findByStudentNic(dto.getNic());
        if (nicOwner.isPresent() && !nicOwner.get().getId().equals(dto.getId())) {
            throw new SQLException("NIC already exists for another student: " + dto.getNic());
        }

        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setNic(dto.getNic());

        List<String> currentEnrolledCourseIds = existing.getEnrollments().stream()
                .map(e -> e.getCourse().getId())
                .collect(Collectors.toList());

        for (String newCourseId : dto.getCourseIds()) {
            if (!currentEnrolledCourseIds.contains(newCourseId)) {
                CourseEntity course = session.get(CourseEntity.class, newCourseId);
                if (course != null) {
                    EnrollmentEntity enrollment = new EnrollmentEntity();
                    enrollment.setStudent(existing);
                    enrollment.setCourse(course);
                    enrollment.setDate(new Date());
                    enrollment.setAmount(course.getFee());

                    existing.getEnrollments().add(enrollment);
                }
            }
        }

        session.merge(existing);
        transaction.commit();
        return true;

    } catch (Exception e) {
        if (transaction != null) transaction.rollback();
        throw new SQLException("Failed to update student with enrollments", e);
    } finally {
        session.close();
    }
}


    @Override
    public boolean deleteStudent(String id) throws SQLException {
       Session session = factoryConfiguration.getSession();
       Transaction transaction = session.beginTransaction();

       try {
           StudentEntity student = session.get(StudentEntity.class, id);
           if (student != null) {
               session.remove(student);
               transaction.commit();
               return true;
           }
           return false;
       }catch (Exception e) {
           if (transaction != null) transaction.rollback();
           throw new SQLException("Failed to delete student", e);
       }finally {
           session.close();
       }

    }

    @Override
    public String getNextId() throws SQLException {
        String lastId = studentDAO.getLastId();
        char tableChar = 'S';
        if(lastId != null){
            String lastIdNumberString = lastId.substring(1);
            int  lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }

    @Override
    public StudentDto getStudentById(String id) throws SQLException {
        Optional<StudentEntity> optionalStudent = studentDAO.findById(id);
        return optionalStudent.map(converter::getStudentDto).orElse(null);
    }


}

