package com.kspt.khandygo.bl;

import com.google.common.base.Preconditions;
import static com.google.common.collect.Lists.newArrayList;
import com.kspt.khandygo.bl.entities.beans.MessageBean;
import com.kspt.khandygo.bl.entities.payments.Award;
import com.kspt.khandygo.bl.entities.th.Meeting;
import com.kspt.khandygo.bl.entities.th.Vocation;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.AnswerApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Pending;
import com.kspt.khandygo.core.sys.Messenger;
import static com.kspt.khandygo.utils.TimeUtils.currentUTCMs;
import static java.util.Collections.singletonList;
import java.util.List;
import java.util.function.BiFunction;

public class AnswerService implements AnswerApi {

  private final Repository<Pending> pendings;

  private final Messenger messenger;

  public AnswerService(
      final Repository<Pending> pendings,
      final Messenger messenger) {
    this.pendings = pendings;
    this.messenger = messenger;
  }

  @Override
  public void approve(final int id, final Employee responsible) {
    commitAndNotify(id, Pending::approveBy, responsible);
  }

  @Override
  public void reject(final int id, final Employee responsible) {
    commitAndNotify(id, Pending::rejectBy, responsible);
  }

  private void commitAndNotify(final int id, final BiFunction<Pending, Employee, Pending> committer,
      final Employee responsible) {
    final Pending found = pendings.get(id);
    Preconditions.checkState(found.pending());
    final Pending committed = committer.apply(found, responsible);
    final Pending updated = pendings.update(committed);

    notifyAbout(updated, responsible);
  }

  private void notifyAbout(final Pending subject, final Employee author) {
    final List<Employee> subscribers;
    if (subject instanceof Award) {
      final Employee employee = ((Award) subject).employee();
      subscribers = newArrayList(employee, employee.manager());
    } else if (subject instanceof Meeting) {
      subscribers = ((Meeting) subject).participants();
    } else if (subject instanceof Vocation) {
      subscribers = singletonList(((Vocation) subject).employee());
    } else {
      throw new RuntimeException();
    }
    messenger.send(subscribers, new MessageBean(-1, author, currentUTCMs(), subject));
  }
}
