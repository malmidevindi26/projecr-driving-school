package org.example.projectdriving.bo.Custom;

import org.example.projectdriving.bo.SuperBO;
import org.example.projectdriving.dto.LessonDto;

import java.util.List;

public interface LessonBO extends SuperBO {
    List<LessonDto> getAllLessons();

    void saveLesson(LessonDto dto);

    void deleteLesson(String id);

    boolean updateLesson(LessonDto dto);

    String getNextId();
}
