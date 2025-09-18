package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.config.FactoryConfiguration;
import org.example.projectdriving.dao.custome.UserDAO;
import org.example.projectdriving.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {
    @Override
    public UserEntity findByUserName(String userName) throws SQLException{
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Query query = session.createQuery(
                    "from UserEntity where username = :userName", UserEntity.class
            );
            query.setParameter("userName", userName);
            return (UserEntity) query.uniqueResult();
        }finally {
            session.close();
        }
    }

    @Override
    public void save(UserEntity user) throws SQLException {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        }catch(Exception e) {
            if (transaction != null) transaction.rollback();
            throw new SQLException("Error saving user", e);
        }finally {
            session.close();
        }
    }

    @Override
    public boolean updateUser(String userId, String newUsername, String newHashedPassword) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            UserEntity user = session.get(UserEntity.class, userId);
            if(user != null) {
                user.setUsername(newUsername);
                user.setPassword(newHashedPassword);
                session.merge(user);
                transaction.commit();
                return true;
            }
            return false;
        }catch(Exception e) {
            if (transaction != null) transaction.rollback();
            throw  new SQLException("Error updating user credentials" , e);

        }finally {
            session.close();
        }
    }
}
