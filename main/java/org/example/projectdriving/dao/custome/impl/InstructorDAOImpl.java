package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.config.FactoryConfiguration;
import org.example.projectdriving.dao.custome.InstructorDAO;
import org.example.projectdriving.entity.InstructorEntity;
import org.example.projectdriving.entity.StudentEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class InstructorDAOImpl implements InstructorDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public List<InstructorEntity> getAll() throws SQLException {
        Session session = factoryConfiguration.getSession();
        try {
            Query<InstructorEntity> query = session.createQuery("from InstructorEntity ", InstructorEntity.class);
            List<InstructorEntity> instructorEntityList = query.list();
            return instructorEntityList;
        }finally {
            session.close();
        }
    }

    @Override
    public String getLastId() throws SQLException {
        try (Session session = factoryConfiguration.getSession()) {
            Query<String> query = session.createQuery(
                    "select ins.id from InstructorEntity ins order by ins.id desc ",
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
    public boolean save(InstructorEntity instructorEntity) throws SQLException {

        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(instructorEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean update(InstructorEntity instructorEntity) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(instructorEntity);
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
            InstructorEntity instructorEntity = session.get(InstructorEntity.class, id);
            if (instructorEntity != null) {
                session.remove(instructorEntity);
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
            Query<String> query = session.createQuery("select ins.id from InstructorEntity ins", String.class);
            return query.list();
        }finally {
            session.close();
        }
    }

    @Override
    public Optional<InstructorEntity> findById(String id) throws SQLException {
        Session session = factoryConfiguration.getSession();
        try {
            InstructorEntity instructorEntity = session.get(InstructorEntity.class, id);
            return Optional.ofNullable(instructorEntity);
        }finally {
            session.close();
        }
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) throws SQLException {
        Session session =factoryConfiguration.getSession();
        try {
            Query<InstructorEntity> query = session.createQuery(
                    "from InstructorEntity i where i.phone = :phone", InstructorEntity.class);

            query.setParameter("phone", phoneNumber);
            return  query.uniqueResultOptional().isPresent();
        }finally {
            session.close();
        }
    }

//    @Override
//    public List<InstructorEntity> getInstructorsByCourse(String courseId) throws SQLException {
//        try (Session session = factoryConfiguration.getSession()) {
//            // Assuming InstructorEntity has a collection named 'courses' mapped to CourseEntity
//            Query<InstructorEntity> query = session.createQuery(
//                    "select i from InstructorEntity i join i.course c where c.id = :courseId", InstructorEntity.class
//            );
//            query.setParameter("courseId", courseId);
//            return query.list();
//        }
//    }

    @Override
    public List<InstructorEntity> findByCourseId(String courseId) throws SQLException {
        try (Session session = factoryConfiguration.getSession()) {
            Query<InstructorEntity> query = session.createQuery(
                    "from InstructorEntity i where i.courses.id = :courseId", InstructorEntity.class);
            query.setParameter("courseId", courseId);
            return query.list();
        }
    }

}
