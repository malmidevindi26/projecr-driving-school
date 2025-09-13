package org.example.projectdriving.bo.Custom;

import org.example.projectdriving.bo.SuperBO;
import org.example.projectdriving.dto.LessonDto;

import java.sql.SQLException;
import java.util.List;

public interface LessonBO extends SuperBO {
    List<LessonDto> getAllLessons() throws SQLException;

    void saveLesson(LessonDto dto) throws SQLException;

    boolean deleteLesson(String id) throws SQLException;

    boolean updateLesson(LessonDto dto);

    String getNextId() throws SQLException;
}
