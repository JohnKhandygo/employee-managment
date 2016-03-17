package com.kspt.khandygo.bl;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.bl.entities.beans.MessageBean;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.PaymentsApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Payment;
import com.kspt.khandygo.core.sys.Messenger;
import static com.kspt.khandygo.utils.TimeUtils.currentUTCMs;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

public class PaymentsService implements PaymentsApi {

  private final Repository<Payment> payments;

  private final Messenger messenger;

  @Inject
  public PaymentsService(
      final Repository<Payment> payments,
      final Messenger messenger) {
    this.payments = payments;
    this.messenger = messenger;
  }

  @Override
  public List<Payment> get(final int id, final long since, final long till) {
    throw new UnsupportedOperationException("not implemented yet.");
  }

  @Override
  public Payment add(final Payment p) {
    return payments.add(p);
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

    final Employee employee = payment.employee();
    messenger.send(
        employee.manager(),
        new MessageBean(-1, employee.paymaster(), currentUTCMs(), payment));
  }
}
