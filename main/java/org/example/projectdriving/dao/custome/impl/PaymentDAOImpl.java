package org.example.projectdriving.dao.custome.impl;

import org.example.projectdriving.dao.custome.PaymentDAO;
import org.example.projectdriving.entity.PaymentEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public List<PaymentEntity> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public String getLastId() throws SQLException {
        return "";
    }

    @Override
    public boolean save(PaymentEntity paymentEntity) throws SQLException {
        return false;
    }

    @Override
    public boolean update(PaymentEntity paymentEntity) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return false;
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<PaymentEntity> findById(String id) throws SQLException {
        return Optional.empty();
    }
}
