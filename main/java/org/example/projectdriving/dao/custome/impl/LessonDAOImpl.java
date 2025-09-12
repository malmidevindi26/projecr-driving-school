package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.config.FactoryConfiguration;
import org.example.projectdriving.dao.custome.LessonDAO;
import org.example.projectdriving.entity.LessonEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class LessonDAOImpl implements LessonDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();
    private final Session session = factoryConfiguration.getSession();
    @Override
    public List<LessonEntity> getAll() throws SQLException {
        Session session = factoryConfiguration.getSession();
        try {
            Query<LessonEntity> query = session.createQuery("from LessonEntity ", LessonEntity.class);
            List<LessonEntity> lessonEntityList = query.list();
            return lessonEntityList;
        }finally {
            session.close();
        }
    }

    @Override
    public String getLastId() throws SQLException {
        try (Session session = factoryConfiguration.getSession()) {
            Query<String> query = session.createQuery(
                    "select les.id from LessonEntity les order by les.id desc ",
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
    public boolean save(LessonEntity lessonEntity) throws SQLException {
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(lessonEntity);
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
    public boolean update(LessonEntity lessonEntity) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(lessonEntity);
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
            LessonEntity lessonEntity = session.get(LessonEntity.class, id);
            if (lessonEntity != null) {
                session.remove(lessonEntity);
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
            Query<String> query = session.createQuery(
                    "select les.id from StudentEntity les", String.class);
            return query.list();
        }finally {
            session.close();
        }
    }

    @Override
    public Optional<LessonEntity> findById(String id) throws SQLException {
        Session session = factoryConfiguration.getSession();
        try {
            LessonEntity lessonEntity = session.get(LessonEntity.class, id);
            return Optional.ofNullable(lessonEntity);
        }finally {
            session.close();
        }
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) throws SQLException {
        return false;
    }


    @Override
    public List<LessonEntity> findByInstructorAndDate(String instructorId, LocalDate date) throws SQLException {

            try (Session session = factoryConfiguration.getSession()) {
//                Query<LessonEntity> query = session.createQuery(
//                        "from LessonEntity l where l.instructor.id = :insId and l.startTime = :date",
//                        LessonEntity.class
//                );
//                query.setParameter("insId", instructorId);
//                query.setParameter("date", date);
//                return query.list();
                LocalDateTime dayStart = date.atTime(8, 0);
                LocalDateTime dayEnd = date.atTime(17, 0);

                Query<LessonEntity> query = session.createQuery(
                        "from LessonEntity l " +
                                "where l.instructor.id = :insId " +
                                "and l.startTime < :dayEnd " +
                                "and l.endTime > :dayStart",
                        LessonEntity.class
                );
                query.setParameter("insId", instructorId);
                query.setParameter("dayStart", dayStart);
                query.setParameter("dayEnd", dayEnd);

                return query.list();

            }
        }

    }

