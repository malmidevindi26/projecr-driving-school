package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.dao.custome.StudentDAO;
import org.example.projectdriving.entity.StudentEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudentDAOImpl implements StudentDAO {
    @Override
    public List<StudentEntity> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public String getLastId() throws SQLException {
        return "";
    }

    @Override
    public boolean save(StudentEntity studentEntity) throws SQLException {
        return false;
    }

    @Override
    public boolean update(StudentEntity studentEntity) throws SQLException {
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
    public Optional<StudentEntity> findById(String id) throws SQLException {
        return Optional.empty();
    }
}
