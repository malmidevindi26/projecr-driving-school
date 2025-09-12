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
    public List<LessonDto> getAllLessons() {
        return List.of();
    }

    @Override
    public void saveLesson(LessonDto dto) throws SQLException {
         try {
             Optional<CourseEntity> optionalCourse = courseDAO.findById(dto.getCourseId());
             if (optionalCourse.isEmpty()) {
                 throw new DuplicateException(" Course not found..!");
             }
             Optional<InstructorEntity> optionalInstructor = instructorDAO.findById(dto.getInstructorId());
             if (optionalInstructor.isEmpty()) {
                 throw new DuplicateException(" Instructor not found..!");
             }
             LocalTime start = LocalTime.from(dto.getStartTime());
             LocalTime end = start.plusHours(1);

             LocalTime open = LocalTime.of(8,0);
             LocalTime close = LocalTime.of(17,0);

             if ((start.isBefore(open) || end.isAfter(close))){
                 throw new RuntimeException("Lesson must be between 08:00 and 17:00");
             }

             List<LessonEntity> lessons = lessonDAO.findByInstructorAndDate(dto.getInstructorId(), LocalDate.from(dto.getStartTime()));

             boolean conflict = lessons.stream().anyMatch(l ->
                     start.isBefore(LocalTime.from(l.getEndTime())) && end.isAfter(LocalTime.from(l.getStartTime()))
             );
             if (conflict){
                 throw new RuntimeException("Instructor not available at this time");
             }
             dto.setEndTime(LocalDateTime.from(end));
             LessonEntity lesson =  converter.getLessonEntity(dto);

             if(!lessonDAO.save(lesson)){
                 throw new RuntimeException("Failed to save lesson");
             }
         } catch (Exception e) {
             throw new RuntimeException("Error saving lesson", e);
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
                    throw new RuntimeException("Lesson not found");
                }

                LessonEntity existingLesson = existingLessonOpt.get();


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

//
//                existingLesson.setStartTime(start);
//                existingLesson.setEndTime(end);
//                existingLesson.setInstructor(new InstructorEntity(dto.getInstructorId()));
//                existingLesson.setCourse(new CourseEntity(dto.getCourseId()));
//                existingLesson.setStatus(dto.getStatus());

                LessonEntity lesson =  converter.getLessonEntity(dto);


                return lessonDAO.update(lesson);

            } catch (Exception e) {
                throw new RuntimeException("Error updating lesson", e);
            }
        }



    @Override
    public String getNextId() throws SQLException {
        String lastId = courseDAO.getLastId();
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
