package org.example.projectdriving.bo.Custom;

import org.example.projectdriving.bo.SuperBO;
import org.example.projectdriving.dto.PaymentDto;

import java.util.List;

public interface PaymentBO extends SuperBO {

    List<PaymentDto> getAllPayments();

    void savePayment(PaymentDto dto);

    void updatePayment(PaymentDto dto);

    boolean deletePayment(String id);

    String getNextId();
}
