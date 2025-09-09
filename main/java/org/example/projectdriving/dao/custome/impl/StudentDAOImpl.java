package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.config.FactoryConfiguration;
import org.example.projectdriving.dao.custome.StudentDAO;
import org.example.projectdriving.entity.StudentEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class StudentDAOImpl implements StudentDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public List<StudentEntity> getAll() throws SQLException {
        Session session = factoryConfiguration.getSession();
        try {
            Query<StudentEntity> query = session.createQuery("from StudentEntity", StudentEntity.class);
            List<StudentEntity> studentList = query.list();
            return studentList;
        }finally {
            session.close();
        }
    }

    @Override
    public String getLastId() throws SQLException {
        try (Session session = factoryConfiguration.getSession()) {
            Query<String> query = session.createQuery(
                    "select stu.id from StudentEntity stu order by stu.id desc ",
                    String.class
            ).setMaxResults(1);
            List<String> list = query.list();
            if (list.isEmpty()) {
                return null;
            }
            return list.get(0);
        }
    }

    @Override
    public boolean save(StudentEntity studentEntity) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(studentEntity);
            transaction.commit();
            return true;
        }catch(Exception e){
            transaction.rollback();
            e.printStackTrace();
            return false;
        }finally{
            session.close();
        }
    }

    @Override
    public boolean update(StudentEntity studentEntity) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(studentEntity);
            transaction.commit();
            return true;
        }catch(Exception e){
            transaction.rollback();
            e.printStackTrace();
            return false;
        }finally{
            session.close();
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            StudentEntity studentEntity = session.get(StudentEntity.class, id);
            if (studentEntity != null) {
                session.remove(studentEntity);
                transaction.commit();
                return true;
            }
            return false;
        }catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }finally{
            session.close();
        }
    }

    @Override
    public List<String> getAllIds() throws SQLException {
       Session session = factoryConfiguration.getSession();
       try {
           Query<String> query = session.createQuery("select s.id from StudentEntity s", String.class);
           return query.list();
       }finally {
           session.close();
       }
    }

    @Override
    public Optional<StudentEntity> findById(String id) throws SQLException {
        Session session = factoryConfiguration.getSession();
       try {
           StudentEntity studentEntity = session.get(StudentEntity.class, id);
           return Optional.ofNullable(studentEntity);
       }finally {
           session.close();
       }
    }
}
