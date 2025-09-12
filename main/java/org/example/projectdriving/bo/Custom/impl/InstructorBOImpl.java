package org.example.projectdriving.bo.Custom.impl;

import org.example.projectdriving.bo.Custom.InstructorBO;
import org.example.projectdriving.bo.exception.DuplicateException;
import org.example.projectdriving.bo.exception.NotFoundException;
import org.example.projectdriving.bo.util.EntityDTOConverter;
import org.example.projectdriving.dao.DAOFactory;
import org.example.projectdriving.dao.DAOTypes;
import org.example.projectdriving.dao.custome.InstructorDAO;
import org.example.projectdriving.dto.InstructorDto;
import org.example.projectdriving.entity.InstructorEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InstructorBOImpl implements InstructorBO {
    private final InstructorDAO instructorDAO = DAOFactory.getInstance().getDAO(DAOTypes.INSTRUCTOR);
    private final EntityDTOConverter converter = new EntityDTOConverter();
    @Override
    public List<InstructorDto> getAllInstructors() throws SQLException {
        List<InstructorEntity> instructorEntities = instructorDAO.getAll();
        List<InstructorDto> instructorDtos = new ArrayList<>();
        for (InstructorEntity entity : instructorEntities) {
            instructorDtos.add(converter.getInstructorDto(entity));
        }
        return instructorDtos;
    }

    @Override
    public void saveInstructor(InstructorDto dto) throws SQLException {

        Optional<InstructorEntity> optionalInstructor = instructorDAO.findById(dto.getId());
        if(optionalInstructor.isPresent()){
            throw new DuplicateException("Instructor already exists");
        }

        if (instructorDAO.existsByPhoneNumber(dto.getPhone())){
            throw new DuplicateException("Duplicate Instructor because the Phone number already exists");
        }

        InstructorEntity instructor = converter.getInstructorEntity(dto);
        boolean save = instructorDAO.save(instructor);
    }

    @Override
    public void updateInstructor(InstructorDto dto) throws SQLException {
        Optional<InstructorEntity> optionalInstructor = instructorDAO.findById(dto.getId());
        if(optionalInstructor.isEmpty()){
            throw new NotFoundException("Instructor not found");
        }

       InstructorEntity instructor = converter.getInstructorEntity(dto);
        instructorDAO.update(instructor);
    }

    @Override
    public boolean deleteInstructor(String id) throws SQLException {
        Optional<InstructorEntity> optionalInstructor = instructorDAO.findById(id);
        if(optionalInstructor.isEmpty()){
            throw  new NotFoundException("instructor not found");
        }

        try {
            boolean delete = instructorDAO.delete(id);
        }catch(Exception e){
            return  false;
        }
        return  true;
    }

    @Override
    public String getNextId() throws SQLException {
        String lastId = instructorDAO.getLastId();
        char tableChar = 'I';
        if (lastId != null) {
            String lastNumberString = lastId.substring(1);
            int lastNumber = Integer.parseInt(lastNumberString);
            int nextNumber = lastNumber + 1;
            return String.format(tableChar + "%03d",  nextNumber);
        }
            return tableChar + "001";
    }
}
