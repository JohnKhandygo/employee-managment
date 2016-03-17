package com.kspt.khandygo.bl;

import com.google.common.base.Preconditions;
import static com.google.common.collect.Lists.newArrayList;
import com.kspt.khandygo.bl.entities.beans.MessageBean;
import com.kspt.khandygo.bl.entities.th.Meeting;
import com.kspt.khandygo.bl.entities.th.OutOfOffice;
import com.kspt.khandygo.bl.entities.th.SpentTime;
import com.kspt.khandygo.bl.entities.th.Vocation;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.TimeHoldersApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.TimeHolder;
import com.kspt.khandygo.core.sys.Messenger;
import static com.kspt.khandygo.utils.TimeUtils.currentUTCMs;
import static java.util.Collections.singletonList;
import javax.inject.Inject;
import java.util.List;

public class TimeHoldersService implements TimeHoldersApi {

  private final Repository<TimeHolder> ths;

  private final Messenger messenger;

  @Inject
  public TimeHoldersService(
      final Repository<TimeHolder> ths,
      final Messenger messenger) {
    this.ths = ths;
    this.messenger = messenger;
  }

  @Override
  public TimeHolder track(final TimeHolder th) {
    Preconditions.checkState(th.when() < currentUTCMs());
    return addAndNotify(th);
  }

  @Override
  public TimeHolder reserve(final TimeHolder th) {
    Preconditions.checkState(th.when() > currentUTCMs());
    return addAndNotify(th);
  }

  private TimeHolder addAndNotify(final TimeHolder th) {
    final TimeHolder added = ths.add(th);
    final Employee employee = added.employee();
    notifyAbout(added, employee);
    return added;
  }

  private void notifyAbout(final TimeHolder subject, final Employee author) {
    final List<Employee> subscribers;
    if (subject instanceof Meeting) {
      subscribers = ((Meeting) subject).participants();
    } else {
      final Employee employee = subject.employee();
      if (subject instanceof OutOfOffice) {
        subscribers = singletonList(employee.manager());
      } else if (subject instanceof Vocation) {
        subscribers = newArrayList(employee.manager(), employee.paymaster());
      } else if (subject instanceof SpentTime) {
        subscribers = singletonList(employee.manager());
      } else {
        throw new RuntimeException();
      }
    }
    messenger.send(subscribers, new MessageBean(-1, author, currentUTCMs(), subject));
  }
}
