package org.example.projectdriving.util;

import org.example.projectdriving.dao.custome.UserDAO;
import org.example.projectdriving.dao.custome.impl.UserDAOImpl;
import org.example.projectdriving.entity.UserEntity;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class InitialUserSetup {
    public static void main(String[] args) {

        UserDAOImpl userDAO = new UserDAOImpl();

        UserEntity admin1 = new UserEntity();
        admin1.setUserId("U001");
        admin1.setUsername("admin1");
        admin1.setRole("ADMIN");
        String hashedPassword1 = BCrypt.hashpw("admin1", BCrypt.gensalt());
        admin1.setPassword(hashedPassword1);

        UserEntity admin2 = new UserEntity();
        admin2.setUserId("U002");
        admin2.setUsername("admin2");
        admin2.setRole("ADMIN");
        String hashedPassword2 = BCrypt.hashpw("admin2", BCrypt.gensalt());
        admin2.setPassword(hashedPassword2);

        UserEntity reception1 = new UserEntity();
        reception1.setUserId("U003");
        reception1.setUsername("user1");
        reception1.setRole("RECEPTION");
        String hashedPassword3 = BCrypt.hashpw("user1", BCrypt.gensalt());
        reception1.setPassword(hashedPassword3);

        UserEntity reception2 = new UserEntity();
        reception2.setUserId("U004");
        reception2.setUsername("user2");
        reception2.setRole("RECEPTION");
        String hashedPassword4 = BCrypt.hashpw("user2", BCrypt.gensalt());
        reception2.setPassword(hashedPassword4);

        try {
            userDAO.save(admin1);
            userDAO.save(admin2);
            userDAO.save(reception1);
            userDAO.save(reception2);
            System.out.println("save successful");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to save all initial users");
        }

    }
}
