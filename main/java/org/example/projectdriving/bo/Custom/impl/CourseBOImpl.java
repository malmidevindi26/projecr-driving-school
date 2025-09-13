package org.example.projectdriving.bo.Custom.impl;

import org.example.projectdriving.bo.Custom.CourseBO;
import org.example.projectdriving.bo.exception.DuplicateException;
import org.example.projectdriving.bo.exception.NotFoundException;
import org.example.projectdriving.bo.util.EntityDTOConverter;
import org.example.projectdriving.config.FactoryConfiguration;
import org.example.projectdriving.dao.DAOFactory;
import org.example.projectdriving.dao.DAOTypes;
import org.example.projectdriving.dao.custome.CourseDAO;
import org.example.projectdriving.dto.CourseDto;
import org.example.projectdriving.entity.CourseEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseBOImpl implements CourseBO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();
    private final CourseDAO courseDAO = DAOFactory.getInstance().getDAO(DAOTypes.COURSE);
    private final EntityDTOConverter converter = new EntityDTOConverter();
    @Override
    public List<CourseDto> getAllCourses() throws SQLException {
        List<CourseEntity> entities = courseDAO.getAll();
        List<CourseDto> dtos = new ArrayList<>();
        for (CourseEntity entity : entities) {
            dtos.add(converter.getCourseDto(entity));
        }
        return dtos;
    }

    @Override
    public void saveCourse(CourseDto dto) throws SQLException {
        Optional<CourseEntity> optionalCourse = courseDAO.findById(dto.getId());
        if(optionalCourse.isPresent()){
            throw new DuplicateException(" Duplicate Course Id..!");
        }

        CourseEntity course =  converter.getCourseEntity(dto);
         courseDAO.save(course);
    }

    @Override
    public void updateCourse(CourseDto dto) throws SQLException {
        Optional<CourseEntity> optionalCourse = courseDAO.findById(dto.getId());
        if(optionalCourse.isEmpty()){
            throw new NotFoundException("Course Not Found..!");
        }

        CourseEntity courseEntity = converter.getCourseEntity(dto);
        courseDAO.update(courseEntity);
    }

    @Override
    public boolean deleteCourse(String id) throws SQLException {
        Optional<CourseEntity> optionalCourse = courseDAO.findById(id);
        if(optionalCourse.isEmpty()){
            throw new NotFoundException("Course Not Found..!");
        }

        try {
            boolean delete = courseDAO.delete(id);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String getNextId() throws SQLException {
       String lastId = courseDAO.getLastId();
       char tableChar = 'C';
       if(lastId != null){
           String lastIdNumberString = lastId.substring(1);
           int  lastIdNumber = Integer.parseInt(lastIdNumberString);
           int nextIdNumber = lastIdNumber + 1;
           return String.format(tableChar + "%03d", nextIdNumber);
       }
       return tableChar + "001";
    }

    @Override
    public List<String> getAllCoursesName() throws SQLException, ClassNotFoundException {
        return courseDAO.getAllIds();
    }

    // CourseBOImpl.java
    @Override
    public List<CourseDto> getCoursesByStudent(String studentId) throws SQLException {
        List<CourseEntity> courses = courseDAO.findCoursesByStudentId(studentId);
        return courses.stream()
                .map(converter::getCourseDto)
                .toList();
    }

}
