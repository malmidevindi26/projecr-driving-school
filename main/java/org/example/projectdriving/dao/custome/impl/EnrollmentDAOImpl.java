package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.config.FactoryConfiguration;
import org.example.projectdriving.dao.custome.EnrollmentDAO;
import org.example.projectdriving.entity.EnrollmentEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EnrollmentDAOImpl implements EnrollmentDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();
    @Override
    public List<EnrollmentEntity> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public String getLastId() throws SQLException {
        return "";
    }

    @Override
    public boolean save(EnrollmentEntity enrollmentEntity) throws SQLException {
        return false;
    }

    @Override
    public boolean update(EnrollmentEntity enrollmentEntity) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return false;
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<EnrollmentEntity> findById(String id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) throws SQLException {
        return false;
    }


    @Override
    public Optional<EnrollmentEntity> findByStudentAndCourse(String studentId, String courseName) {
        try (Session session = factoryConfiguration.getSession()) {
            Query<EnrollmentEntity> query = session.createQuery(
                    "from EnrollmentEntity e where e.student.id = :studentId and e.course.name = :courseName",
                    EnrollmentEntity.class
            );
            query.setParameter("studentId", studentId);
            query.setParameter("courseName", courseName);
            return query.uniqueResultOptional();
        }
    }
}
