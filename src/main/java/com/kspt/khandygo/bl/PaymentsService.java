package com.kspt.khandygo.bl;

import com.google.common.base.Preconditions;
import static com.google.common.collect.Lists.newArrayList;
import com.kspt.khandygo.bl.entities.beans.MessageBean;
import com.kspt.khandygo.bl.entities.payments.Award;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.PaymentsApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.approved.Payment;
import com.kspt.khandygo.core.sys.Messenger;
import static com.kspt.khandygo.utils.TimeUtils.currentUTCMs;
import javax.inject.Inject;
import java.util.ArrayList;
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
  public Payment award(final Award award) {
    Preconditions.checkState(award.when() > currentUTCMs());
    final Payment added = payments.add(award);
    final Employee employee = added.employee();
    final ArrayList<Employee> subscribers = newArrayList(employee, employee.manager());
    messenger.send(subscribers, new MessageBean(employee.paymaster(), currentUTCMs(), added));
    return added;
  }
}
