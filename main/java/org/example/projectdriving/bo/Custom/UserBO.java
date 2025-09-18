package org.example.projectdriving.bo.Custom;

import org.example.projectdriving.bo.SuperBO;
import org.example.projectdriving.dto.UserDto;

import java.sql.SQLException;

public interface UserBO extends SuperBO {
    UserDto authenticate(String username, String password) throws SQLException;

    boolean isUsernameValid(String username) throws SQLException;

    boolean updateUserCredentials(String userId, String newUsername, String newPassword) throws SQLException;
}
