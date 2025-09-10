package org.example.projectdriving.bo.Custom.impl;

import org.example.projectdriving.bo.Custom.PaymentBO;
import org.example.projectdriving.dto.PaymentDto;

import java.util.List;

public class PaymentBOImpl implements PaymentBO {
    @Override
    public List<PaymentDto> getAllPayments() {
        return List.of();
    }

    @Override
    public void savePayment(PaymentDto dto) {

    }

    @Override
    public void updatePayment(PaymentDto dto) {

    }

    @Override
    public boolean deletePayment(String id) {
        return false;
    }

    @Override
    public String getNextId() {
        return "";
    }
}
