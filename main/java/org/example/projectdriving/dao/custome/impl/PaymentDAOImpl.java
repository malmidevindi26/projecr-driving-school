package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.config.FactoryConfiguration;
import org.example.projectdriving.dao.custome.PaymentDAO;
import org.example.projectdriving.entity.PaymentEntity;
import org.example.projectdriving.entity.StudentEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public List<PaymentEntity> getAll() throws SQLException {

        try (Session session = factoryConfiguration.getSession()) {
            Query<PaymentEntity> query = session.createQuery("from PaymentEntity ", PaymentEntity.class);
            List<PaymentEntity> paymentEntities = query.list();
            return paymentEntities;
        }
    }

    @Override
    public String getLastId() throws SQLException {
        try (Session session = factoryConfiguration.getSession()) {
            Query<String> query = session.createQuery(
                    "select p.id from PaymentEntity p order by p.id desc ",
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
    public boolean save(PaymentEntity paymentEntity) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(paymentEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean update(PaymentEntity paymentEntity) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(paymentEntity);
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
            PaymentEntity paymentEntity = session.get(PaymentEntity.class, id);
            if (paymentEntity != null) {
                session.remove(paymentEntity);
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
            Query<String> query = session.createQuery("select p.id from PaymentEntity p", String.class);
            return query.list();
        }finally {
            session.close();
        }
    }

    @Override
    public Optional<PaymentEntity> findById(String id) throws SQLException {
        Session session = factoryConfiguration.getSession();
        try {
            PaymentEntity paymentEntity = session.get(PaymentEntity.class, id);
            return Optional.ofNullable(paymentEntity);
        }finally {
            session.close();
        }
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) throws SQLException {
        return false;
    }

}
