package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.dao.custome.CourseDAO;
import org.example.projectdriving.entity.CourseEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CourseDAOImpl implements CourseDAO {
    @Override
    public List<CourseEntity> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public String getLastId() throws SQLException {
        return "";
    }

    @Override
    public boolean save(CourseEntity courseEntity) throws SQLException {
        return false;
    }

    @Override
    public boolean update(CourseEntity courseEntity) throws SQLException {
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
    public Optional<CourseEntity> findById(String id) throws SQLException {
        return Optional.empty();
    }
}
