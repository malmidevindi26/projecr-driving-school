package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.dao.custome.EnrollmentDAO;
import org.example.projectdriving.entity.EnrollmentEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EnrollmentDAOImpl implements EnrollmentDAO {
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
}
