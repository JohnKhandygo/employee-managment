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
  public void accept(final int id) {
    final TimeHolder found = ths.get(id);
    final Employee employee = found.employee();
    if (found instanceof Meeting) {
      final Meeting accepted = ((Meeting) found).accept();
      for (final Employee recipient : accepted.participants()) {
        messenger.send(
            recipient,
            new MessageBean(-1, employee, currentUTCMs(), found));
      }
    } else if (found instanceof OutOfOffice) {
      throw new RuntimeException();
    } else if (found instanceof Vocation) {
      //TODO somehow extract identity of approver to use (at least) as author
      final Employee author = null;
      final Vocation vocation = (Vocation) found;
      final Vocation accepted = vocation.accept();
      messenger.send(
          employee,
          new MessageBean(-1, author, currentUTCMs(), accepted));
    } else {
      throw new RuntimeException();
    }
  }

  @Override
  public void decline(final int id) {
    final TimeHolder found = ths.get(id);
    final Employee employee = found.employee();
    if (found instanceof Meeting) {
      final Meeting accepted = ((Meeting) found).decline();
      for (final Employee recipient : accepted.participants()) {
        messenger.send(
            recipient,
            new MessageBean(-1, employee, currentUTCMs(), found));
      }
    } else if (found instanceof OutOfOffice) {
      throw new RuntimeException();
    } else if (found instanceof Vocation) {
      //TODO somehow extract identity of approver to use (at least) as author
      final Employee author = null;
      final Vocation vocation = (Vocation) found;
      final Vocation canceled = vocation.decline();
      messenger.send(
          employee,
          new MessageBean(-1, author, currentUTCMs(), canceled));
    } else {
      throw new RuntimeException();
    }
  }

  @Override
  public void cancel(final int id) {
    throw new UnsupportedOperationException("not implemented yet");
  }
}
