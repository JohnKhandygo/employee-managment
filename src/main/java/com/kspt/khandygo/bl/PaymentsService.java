package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.PaymentsApi;
import com.kspt.khandygo.core.entities.PaymentPlan;

public class PaymentsService implements PaymentsApi {

  private final Repository<PaymentPlan> paymentPlansRepository;

  public PaymentsService(final Repository<PaymentPlan> paymentPlansRepository) {
    this.paymentPlansRepository = paymentPlansRepository;
  }

  @Override
  public PaymentPlan get(final int id) {
    return paymentPlansRepository.get(id);
  }
}
