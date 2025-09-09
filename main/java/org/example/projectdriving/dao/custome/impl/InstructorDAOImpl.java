package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.dao.custome.InstructorDAO;
import org.example.projectdriving.entity.InstructorEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class InstructorDAOImpl implements InstructorDAO {
    @Override
    public List<InstructorEntity> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public String getLastId() throws SQLException {
        return "";
    }

    @Override
    public boolean save(InstructorEntity instructorEntity) throws SQLException {
        return false;
    }

    @Override
    public boolean update(InstructorEntity instructorEntity) throws SQLException {
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
    public Optional<InstructorEntity> findById(String id) throws SQLException {
        return Optional.empty();
    }
}
