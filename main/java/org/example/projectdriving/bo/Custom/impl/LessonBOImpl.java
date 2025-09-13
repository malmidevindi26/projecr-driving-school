package org.example.projectdriving.bo.Custom.impl;

import org.example.projectdriving.bo.Custom.LessonBO;
import org.example.projectdriving.bo.exception.DuplicateException;
import org.example.projectdriving.bo.exception.NotFoundException;
import org.example.projectdriving.bo.util.EntityDTOConverter;
import org.example.projectdriving.dao.DAOFactory;
import org.example.projectdriving.dao.DAOTypes;
import org.example.projectdriving.dao.custome.CourseDAO;
import org.example.projectdriving.dao.custome.InstructorDAO;
import org.example.projectdriving.dao.custome.LessonDAO;
import org.example.projectdriving.dto.LessonDto;
import org.example.projectdriving.entity.CourseEntity;
import org.example.projectdriving.entity.InstructorEntity;
import org.example.projectdriving.entity.LessonEntity;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class LessonBOImpl implements LessonBO {
    private final CourseDAO courseDAO = DAOFactory.getInstance().getDAO(DAOTypes.COURSE);
    private final EntityDTOConverter converter = new EntityDTOConverter();
    private final LessonDAO lessonDAO = DAOFactory.getInstance().getDAO(DAOTypes.LESSON);
    private final InstructorDAO instructorDAO = DAOFactory.getInstance().getDAO(DAOTypes.INSTRUCTOR);
    @Override
    public List<LessonDto> getAllLessons() throws SQLException {
        try {

            List<LessonEntity> allLessons = lessonDAO.getAll();


            return allLessons.stream()
                    .map(converter::getLessonDto)
                    .toList();
        } catch (Exception e) {
            throw new SQLException("Failed to retrieve lessons from the database", e);

        }
    }

    @Override
    public void saveLesson(LessonDto dto) throws SQLException {


        Optional<CourseEntity> optionalCourse = courseDAO.findById(dto.getCourseId());
        if (optionalCourse.isEmpty()) {
            throw new DuplicateException("Course not found..!");
        }
        Optional<InstructorEntity> optionalInstructor = instructorDAO.findById(dto.getInstructorId());
        if (optionalInstructor.isEmpty()) {
            throw new DuplicateException("Instructor not found..!");
        }

        LocalTime start = LocalTime.from(dto.getStartTime());
        LocalTime end = start.plusHours(1);

        LocalTime open = LocalTime.of(8,0);
        LocalTime close = LocalTime.of(17,0);

        if ((start.isBefore(open) || end.isAfter(close))){
            throw new RuntimeException("Lesson must be between 08:00 and 17:00");
        }

        List<LessonEntity> lessons = lessonDAO.findByInstructorAndDate(dto.getInstructorId(), LocalDate.from(dto.getStartTime()));

        System.out.println("Lessons found for instructor " + dto.getInstructorId() + " on " + LocalDate.from(dto.getStartTime()) + ":");
        lessons.forEach(l -> System.out.println("  -> Lesson ID: " + l.getId() + ", Start Time: " + l.getStartTime() + ", End Time: " + l.getEndTime()));

        boolean conflict = lessons.stream().anyMatch(l ->
                start.isBefore(LocalTime.from(l.getEndTime())) && end.isAfter(LocalTime.from(l.getStartTime()))
        );
        if (conflict){
            throw new RuntimeException("Instructor not available at this time");
        }


        dto.setEndTime(dto.getStartTime().toLocalDate().atTime(end));

        LessonEntity lesson =  converter.getLessonEntity(dto);

        if(!lessonDAO.save(lesson)){
            throw new RuntimeException("Failed to save lesson");
        }
         }

    @Override
    public boolean deleteLesson(String id) throws SQLException {
        Optional<LessonEntity> optionallesson = lessonDAO.findById(id);
        if(optionallesson.isEmpty()){
            throw new NotFoundException("Course Not Found..!");
        }
        try {
            boolean delete = lessonDAO.delete(id);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateLesson(LessonDto dto) {
        try {
            Optional<LessonEntity> existingLessonOpt = lessonDAO.findById(dto.getId());
            if (existingLessonOpt.isEmpty()) {

                throw new NotFoundException("Lesson not found!");
            }


            LessonEntity existingLesson = existingLessonOpt.get();


            if (dto.getStartTime() == null) {
                throw new RuntimeException("Start time cannot be null for an update.");
            }

            LocalDateTime start = dto.getStartTime();
            LocalDateTime end = start.plusHours(1);



            LocalTime open = LocalTime.of(8, 0);
            LocalTime close = LocalTime.of(17, 0);
            if (start.toLocalTime().isBefore(open) || end.toLocalTime().isAfter(close)) {
                throw new RuntimeException("Lesson must be between 08:00 and 17:00");
            }


            List<LessonEntity> lessons = lessonDAO.findByInstructorAndDate(dto.getInstructorId(), start.toLocalDate());
            boolean conflict = lessons.stream().anyMatch(l ->
                    !l.getId().equals(dto.getId()) &&
                            start.isBefore(l.getEndTime()) &&
                            end.isAfter(l.getStartTime())
            );

            if (conflict) {
                throw new RuntimeException("Instructor is not available at this time");
            }


            dto.setEndTime(end);


            LessonEntity updatedLesson = converter.getLessonEntity(dto);


            return lessonDAO.update(updatedLesson);

        } catch (RuntimeException e) {

            throw e;
        } catch (Exception e) {

            throw new RuntimeException("Error updating lesson: " + e.getMessage(), e);

        }
        }



    @Override
    public String getNextId() throws SQLException {
        String lastId = lessonDAO.getLastId();
        char tableChar = 'L';
        if(lastId != null){
            String lastIdNumberString = lastId.substring(1);
            int  lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }
}
