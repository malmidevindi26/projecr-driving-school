package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.dao.custome.LessonDAO;
import org.example.projectdriving.entity.LessonEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LessonDAOImpl implements LessonDAO {
    @Override
    public List<LessonEntity> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public String getLastId() throws SQLException {
        return "";
    }

    @Override
    public boolean save(LessonEntity lessonEntity) throws SQLException {
        return false;
    }

    @Override
    public boolean update(LessonEntity lessonEntity) throws SQLException {
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
    public Optional<LessonEntity> findById(String id) throws SQLException {
        return Optional.empty();
    }
}
