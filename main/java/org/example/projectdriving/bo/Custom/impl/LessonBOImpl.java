package org.example.projectdriving.bo.Custom.impl;

import org.example.projectdriving.bo.Custom.LessonBO;
import org.example.projectdriving.dto.LessonDto;

import java.util.List;

public class LessonBOImpl implements LessonBO {
    @Override
    public List<LessonDto> getAllLessons() {
        return List.of();
    }

    @Override
    public void saveLesson(LessonDto dto) {

    }

    @Override
    public void deleteLesson(String id) {

    }

    @Override
    public boolean updateLesson(LessonDto dto) {
        return false;
    }

    @Override
    public String getNextId() {
        return "";
    }
}
