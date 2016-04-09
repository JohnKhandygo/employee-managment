package com.kspt.khandygo.core;

import static com.google.common.collect.Lists.newArrayList;
import com.kspt.khandygo.bl.entities.subjects.payments.Award;
import com.kspt.khandygo.bl.entities.subjects.payments.RegularPayment;
import com.kspt.khandygo.bl.entities.subjects.th.Meeting;
import com.kspt.khandygo.bl.entities.subjects.th.OutOfOffice;
import com.kspt.khandygo.bl.entities.subjects.th.SpentTime;
import com.kspt.khandygo.bl.entities.subjects.th.Vocation;
import com.kspt.khandygo.core.Notification.ApprovedCancelled;
import com.kspt.khandygo.core.Notification.ApprovedCreated;
import com.kspt.khandygo.core.Notification.ApprovedUpdated;
import com.kspt.khandygo.core.Notification.ProposeApproved;
import com.kspt.khandygo.core.Notification.ProposeCancelled;
import com.kspt.khandygo.core.Notification.ProposeCreated;
import com.kspt.khandygo.core.Notification.ProposeRejected;
import com.kspt.khandygo.core.Notification.ProposeUpdated;
import com.kspt.khandygo.core.entities.Employee;
import static java.util.Collections.singletonList;
import java.util.List;

abstract class NotifyStation implements SubjectVisitor<List<Employee>> {

  private final NotifyStation next;

  public NotifyStation(final NotifyStation next) {
    this.next = next;
  }

  abstract boolean match(final Notification notification);

  List<Employee> handle(final Notification notification) {
    if (match(notification)) {
      return notification.subject().accept(this);
    } else {
      return next.handle(notification);
    }
  }

  public static class ProposeCRUDNotifyStation extends NotifyStation {

    public ProposeCRUDNotifyStation(final NotifyStation next) {
      super(next);
    }

    @Override
    public List<Employee> visit(final RegularPayment subject) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Employee> visit(final Award subject) {
      return singletonList(subject.employee().paymaster());
    }

    @Override
    public List<Employee> visit(final Meeting subject) {
      return singletonList(subject.employee());
    }

    @Override
    public List<Employee> visit(final OutOfOffice subject) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Employee> visit(final SpentTime subject) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Employee> visit(final Vocation subject) {
      return singletonList(subject.employee().manager());
    }

    @Override
    boolean match(final Notification notification) {
      return notification instanceof ProposeCreated
          || notification instanceof ProposeUpdated
          || notification instanceof ProposeCancelled;
    }
  }

  public static class ProposeARNotifyStation extends NotifyStation {

    public ProposeARNotifyStation(final NotifyStation next) {
      super(next);
    }

    @Override
    public List<Employee> visit(final RegularPayment subject) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Employee> visit(final Award subject) {
      return singletonList(subject.employee().manager());
    }

    @Override
    public List<Employee> visit(final Meeting subject) {
      return subject.participants();
    }

    @Override
    public List<Employee> visit(final OutOfOffice subject) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Employee> visit(final SpentTime subject) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Employee> visit(final Vocation subject) {
      return singletonList(subject.employee());
    }

    @Override
    boolean match(final Notification notification) {
      return notification instanceof ProposeApproved || notification instanceof ProposeRejected;
    }
  }

  public static class ApprovedCRUNotifyStation extends NotifyStation {
    public ApprovedCRUNotifyStation(final NotifyStation next) {
      super(next);
    }

    @Override
    public List<Employee> visit(final RegularPayment subject) {
      return singletonList(subject.employee());
    }

    @Override
    public List<Employee> visit(final Award subject) {
      return singletonList(subject.employee());
    }

    @Override
    public List<Employee> visit(final Meeting subject) {
      return singletonList(subject.employee());
    }

    @Override
    public List<Employee> visit(final OutOfOffice subject) {
      return newArrayList(subject.employee().manager(), subject.employee().paymaster());
    }

    @Override
    public List<Employee> visit(final SpentTime subject) {
      return newArrayList(subject.employee().manager(), subject.employee().paymaster());
    }

    @Override
    public List<Employee> visit(final Vocation subject) {
      return singletonList(subject.employee().paymaster());
    }

    @Override
    boolean match(final Notification notification) {
      return notification instanceof ApprovedCreated || notification instanceof ApprovedUpdated;
    }
  }

  public static class ApprovedDNotifyStation extends NotifyStation {
    public ApprovedDNotifyStation(final NotifyStation next) {
      super(next);
    }

    @Override
    public List<Employee> visit(final RegularPayment subject) {
      return singletonList(subject.employee());
    }

    @Override
    public List<Employee> visit(final Award subject) {
      return newArrayList(subject.employee(), subject.employee().paymaster());
    }

    @Override
    public List<Employee> visit(final Meeting subject) {
      return singletonList(subject.employee());
    }

    @Override
    public List<Employee> visit(final OutOfOffice subject) {
      return newArrayList(subject.employee().manager(), subject.employee().paymaster());
    }

    @Override
    public List<Employee> visit(final SpentTime subject) {
      return newArrayList(subject.employee().manager(), subject.employee().paymaster());
    }

    @Override
    public List<Employee> visit(final Vocation subject) {
      return newArrayList(subject.employee().manager(), subject.employee().paymaster());
    }

    @Override
    boolean match(final Notification notification) {
      return notification instanceof ApprovedCancelled;
    }
  }
}