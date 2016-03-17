package com.kspt.khandygo.bl;

import static com.google.common.collect.Lists.newArrayList;
import com.kspt.khandygo.bl.entities.beans.MessageBean;
import com.kspt.khandygo.bl.entities.payments.Award;
import com.kspt.khandygo.bl.entities.th.Meeting;
import com.kspt.khandygo.bl.entities.th.Vocation;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.CancelApi;
import com.kspt.khandygo.core.entities.Approved;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.sys.Messenger;
import static com.kspt.khandygo.utils.TimeUtils.currentUTCMs;
import java.util.List;

public class CancelService implements CancelApi {

  private final Repository<Approved> repository;

  private final Messenger messenger;

  public CancelService(final Repository<Approved> repository, final Messenger messenger) {
    this.repository = repository;
    this.messenger = messenger;
  }

  @Override
  public void cancel(final int id, final Employee owner) {
    final Approved approved = repository.get(id);
    //Preconditions.checkState(approved.when() > Instant.now().toEpochMilli());
    final Approved canceled = approved.cancelBy(owner);
    final Approved updated = repository.update(canceled);
    notifyAbout(updated, owner);
  }

  private void notifyAbout(final Approved subject, final Employee author) {
    final List<Employee> subscribers;
    if (subject instanceof Award) {
      final Employee employee = ((Award) subject).employee();
      subscribers = newArrayList(employee.manager(), employee.paymaster());
    } else if (subject instanceof Meeting) {
      subscribers = ((Meeting) subject).participants();
    } else if (subject instanceof Vocation) {
      final Employee employee = ((Vocation) subject).employee();
      subscribers = newArrayList(employee.manager(), employee.paymaster());
    } else {
      throw new RuntimeException();
    }
    messenger.send(subscribers, new MessageBean(-1, author, currentUTCMs(), subject));
  }
}
