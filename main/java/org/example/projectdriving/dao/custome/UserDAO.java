package org.example.projectdriving.dao.custome;

import org.example.projectdriving.dao.SuperDAO;
import org.example.projectdriving.dto.UserDto;
import org.example.projectdriving.entity.UserEntity;

import java.sql.SQLException;

public interface UserDAO extends SuperDAO {
    UserEntity findByUserName(String userName)throws SQLException;
    void save(UserEntity user) throws SQLException;
}
