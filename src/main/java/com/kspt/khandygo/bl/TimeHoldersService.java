package com.kspt.khandygo.bl;

import com.kspt.khandygo.bl.entities.beans.MessageBean;
import com.kspt.khandygo.bl.entities.th.Meeting;
import com.kspt.khandygo.bl.entities.th.OutOfOffice;
import com.kspt.khandygo.bl.entities.th.Vocation;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.TimeHoldersApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.TimeHolder;
import com.kspt.khandygo.core.sys.Messenger;
import static com.kspt.khandygo.utils.TimeUtils.currentUTCMs;
import javax.inject.Inject;

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
  public TimeHolder reserve(final TimeHolder th) {
    final TimeHolder added = ths.add(th);
    final Employee employee = added.employee();
    if (th instanceof Meeting) {
      messenger.send(
          employee,
          new MessageBean(-1, employee, currentUTCMs(), added));
    } else if (th instanceof OutOfOffice) {
      messenger.send(
          employee.manager(),
          new MessageBean(-1, employee, currentUTCMs(), added));
    } else if (th instanceof Vocation) {
      messenger.send(
          employee.manager(),
          new MessageBean(-1, employee, currentUTCMs(), added));
    } else {
      throw new RuntimeException();
    }
    return added;
  }

  @Override
  public void cancel(final int id) {
    throw new UnsupportedOperationException("not implemented yet");
  }
}
