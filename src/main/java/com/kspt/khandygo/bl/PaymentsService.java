package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.PaymentsApi;
import com.kspt.khandygo.core.entities.Payment;
import com.kspt.khandygo.core.sys.Messenger;
import javax.inject.Inject;
import java.util.List;

public class PaymentsService implements PaymentsApi {

  private final Repository<Payment> payments;

  @Inject
  public PaymentsService(
      final Repository<Payment> payments,
      final Messenger messenger) {
    this.payments = payments;
  }

  @Override
  public List<Payment> get(final int id, final long since, final long till) {
    throw new UnsupportedOperationException("not implemented yet.");
  }

  @Override
  public Payment add(final Payment p) {
    return payments.add(p);
  }
}
