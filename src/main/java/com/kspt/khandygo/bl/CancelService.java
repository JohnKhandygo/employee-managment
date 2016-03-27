package com.kspt.khandygo.bl;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import static com.google.common.collect.Lists.newArrayList;
import com.kspt.khandygo.bl.entities.beans.MessageBean;
import com.kspt.khandygo.bl.entities.payments.Award;
import com.kspt.khandygo.bl.entities.th.Meeting;
import com.kspt.khandygo.bl.entities.th.OutOfOffice;
import com.kspt.khandygo.bl.entities.th.SpentTime;
import com.kspt.khandygo.bl.entities.th.Vocation;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.CancelApi;
import com.kspt.khandygo.core.entities.Approved;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.approved.TimeHolder;
import com.kspt.khandygo.core.sys.Messenger;
import static com.kspt.khandygo.utils.TimeUtils.currentUTCMs;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

public class CancelService implements CancelApi {

  private final Repository<Approved> repository;

  private final Messenger messenger;

  @Inject
  public CancelService(final Repository<Approved> repository, final Messenger messenger) {
    this.repository = repository;
    this.messenger = messenger;
  }

  //TODO need to check time? if so then where and how? seems like Approved shouldn't has when().
  @Override
  public void cancel(final int id, final Employee requester) {
    Preconditions.checkState(repository.contains(id));
    final Approved approved = repository.get(id);
    Preconditions.checkState(Objects.equal(requester, approved.owner()));
    final Approved canceled = approved.cancel();
    final Approved updated = repository.update(canceled);
    notifyAbout(updated, requester);
  }

  private void notifyAbout(final Approved subject, final Employee author) {
    final List<Employee> subscribers;
    if (subject instanceof Award) {
      final Employee employee = ((Award) subject).employee();
      subscribers = newArrayList(employee.manager(), employee.paymaster());
    } else if (subject instanceof Meeting) {
      subscribers = ((Meeting) subject).participants();
    } else {
      final TimeHolder th = (TimeHolder) subject;
      final Employee employee = th.employee();
      if (subject instanceof OutOfOffice) {
        subscribers = newArrayList(employee.manager(), employee.paymaster());
      } else if (subject instanceof SpentTime) {
        subscribers = Collections.singletonList(employee.manager());
      } else if (subject instanceof Vocation) {
        subscribers = newArrayList(employee.manager(), employee.paymaster());
      } else {
        throw new RuntimeException();
      }
    }
    messenger.send(subscribers, new MessageBean(author, currentUTCMs(), subject));
  }
}
