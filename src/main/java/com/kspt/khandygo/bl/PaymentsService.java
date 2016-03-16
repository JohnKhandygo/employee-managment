package com.kspt.khandygo.bl;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.PaymentsApi;
import com.kspt.khandygo.core.entities.Award;
import com.kspt.khandygo.core.entities.Payment;
import java.time.Instant;

public class PaymentsService implements PaymentsApi {

  private final Repository<Payment> payments;

  public PaymentsService(
      final Repository<Payment> payments) {
    this.payments = payments;
  }

  @Override
  public Payment add(final Payment p) {
    return payments.add(p);
  }

  @Override
  public void accept(final int id) {
    final Payment payment = payments.get(id);
    if (payment instanceof Award) {
      final Award award = (Award) payment;
      final Award approved = award.approve();
      payments.update(approved);
    } else {
      throw new RuntimeException();
    }
  }

  @Override
  public void decline(final int id) {
    final Payment payment = payments.get(id);
    if (payment instanceof Award) {
      cancel(payment);
    } else {
      throw new RuntimeException();
    }
  }

  @Override
  public void cancel(final int id) {
    final Payment payment = payments.get(id);
    cancel(payment);
  }

  private void cancel(final Payment payment) {
    Preconditions.checkState(payment.when() > Instant.now().toEpochMilli());
    final Payment canceled = payment.cancel();
    payments.update(canceled);
  }
}
