package org.example.projectdriving.bo.Custom.impl;

import org.example.projectdriving.bo.Custom.UserBO;
import org.example.projectdriving.bo.util.EntityDTOConverter;
import org.example.projectdriving.dao.DAOFactory;
import org.example.projectdriving.dao.DAOTypes;
import org.example.projectdriving.dao.custome.UserDAO;
import org.example.projectdriving.dto.UserDto;
import org.example.projectdriving.entity.UserEntity;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class UserBOImpl implements UserBO {
    private final UserDAO userDAO = DAOFactory.getInstance().getDAO(DAOTypes.USER);
    private final EntityDTOConverter converter = new EntityDTOConverter();
    @Override
    public UserDto authenticate(String username, String password) throws SQLException {
        UserEntity userEntity = userDAO.findByUserName(username);
        if (userEntity != null && BCrypt.checkpw(password, userEntity.getPassword())) {
            return converter.getUserDto(userEntity);
        }
        return null;
    }

    @Override
    public boolean isUsernameValid(String username) throws SQLException {
        UserEntity userEntity = userDAO.findByUserName(username);
        return userEntity != null;
    }
}
