package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.config.FactoryConfiguration;
import org.example.projectdriving.dao.custome.CourseDAO;
import org.example.projectdriving.entity.CourseEntity;
import org.example.projectdriving.entity.StudentEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CourseDAOImpl implements CourseDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();
    @Override
    public List<CourseEntity> getAll() throws SQLException {
        Session session = factoryConfiguration.getSession();
        try {
            Query<CourseEntity> query = session.createQuery("from CourseEntity ", CourseEntity.class);
            List<CourseEntity> courseEntityList = query.list();
            return courseEntityList;
        }finally {
            session.close();
        }
    }

    @Override
    public String getLastId() throws SQLException {
        try (Session session = factoryConfiguration.getSession()) {
            Query<String> query = session.createQuery(
                    "select cou.id from CourseEntity cou order by cou.id desc ",
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
    public boolean save(CourseEntity courseEntity) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(courseEntity);
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
    public boolean update(CourseEntity courseEntity) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(courseEntity);
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
            CourseEntity courseEntity = session.get(CourseEntity.class, id);
            if (courseEntity != null) {
                session.remove(courseEntity);
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
            Query<String> query = session.createQuery("select c.id from CourseEntity c", String.class);
            return query.list();
        }finally {
            session.close();
        }
    }

    @Override
    public Optional<CourseEntity> findById(String id) throws SQLException {
        Session session = factoryConfiguration.getSession();
        try {
            CourseEntity courseEntity = session.get(CourseEntity.class, id);
            return Optional.ofNullable(courseEntity);
        }finally {
            session.close();
        }
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) throws SQLException {
        return false;
    }


    @Override
    public List<CourseEntity> findCoursesByStudentId(String studentId) throws SQLException {
        try (Session session = factoryConfiguration.getSession()) {
            Query<CourseEntity> query = session.createQuery(
                    "select e.course from EnrollmentEntity e where e.student.id = :studentId",
                    CourseEntity.class
            );
            query.setParameter("studentId", studentId);
            return query.list();
        } catch (Exception e) {
            throw new SQLException("Failed to get courses for student " + studentId, e);
        }
    }

    //////////////////////////


    ///////////////////////////

}
