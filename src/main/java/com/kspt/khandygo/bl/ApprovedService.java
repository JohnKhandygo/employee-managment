package com.kspt.khandygo.bl;

import com.google.common.base.Objects;
import com.google.common.base.Verify;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
import com.kspt.khandygo.bl.entities.beans.MessageBean;
import com.kspt.khandygo.bl.entities.subjects.payments.Award;
import com.kspt.khandygo.bl.entities.subjects.payments.RegularPayment;
import com.kspt.khandygo.bl.entities.subjects.th.Meeting;
import com.kspt.khandygo.bl.entities.subjects.th.OutOfOffice;
import com.kspt.khandygo.bl.entities.subjects.th.SpentTime;
import com.kspt.khandygo.bl.entities.subjects.th.Vocation;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.ApprovedApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject;
import com.kspt.khandygo.core.sys.Messenger;
import static com.kspt.khandygo.utils.TimeUtils.currentUTCMs;
import static java.util.Collections.singletonList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import javax.inject.Inject;
import java.util.List;

public class ApprovedService implements ApprovedApi {

  private final Repository<Approved> repository;

  private final Messenger messenger;

  @Inject
  ApprovedService(
      final Repository<Approved> repository,
      final Messenger messenger) {
    this.repository = repository;
    this.messenger = messenger;
  }

  public void cancel(final int id, final Employee requester) {
    final Approved approved = repository.get(id);
    Verify.verify(Objects.equal(requester, approved.owner()));
    final Approved deleted = repository.delete(id);
    Verify.verify(Objects.equal(approved, deleted));
    notifyAboutDeletion(deleted, requester);
  }

  @Override
  public int add(final Subject subject, final Employee author) {
    final Employee owner;
    if (subject instanceof RegularPayment) {
      owner = author;
    } else if (subject instanceof Award) {
      owner = subject.employee();
    } else if (subject instanceof Meeting) {
      owner = subject.employee();
    } else if (subject instanceof OutOfOffice) {
      owner = subject.employee();
    } else if (subject instanceof Vocation) {
      owner = subject.employee();
    } else if (subject instanceof SpentTime) {
      owner = subject.employee();
    } else {
      throw new RuntimeException();
    }
    final Approved approved = new Approved(-1, owner, subject);
    final int id = repository.add(approved);
    notifyAboutNew(approved, author);
    return id;
  }

  private void notifyAboutNew(final Approved added, final Employee author) {
    final List<Employee> subscribers;
    final Subject subject = added.subject();
    final Employee employee = subject.employee();
    if (subject instanceof RegularPayment) {
      subscribers = newArrayList(employee);
    } else if (subject instanceof Award) {
      subscribers = newArrayList(employee, employee.manager());
    } else if (subject instanceof Meeting) {
      subscribers = ((Meeting) subject).participants();
    } else if (subject instanceof OutOfOffice) {
      subscribers = newArrayList(employee.manager(), employee.paymaster());
    } else if (subject instanceof Vocation) {
      subscribers = newArrayList(employee.manager(), employee.paymaster());
    } else if (subject instanceof SpentTime) {
      subscribers = singletonList(employee.manager());
    } else {
      throw new RuntimeException();
    }
    messenger.send(newLinkedHashSet(subscribers), new MessageBean(author, currentUTCMs(), added));
  }

  private void notifyAboutDeletion(final Approved deleted, final Employee author) {
    final List<Employee> subscribers;
    final Subject subject = deleted.subject();
    final Employee employee = subject.employee();
    if (subject instanceof RegularPayment) {
      subscribers = newArrayList(subject.employee());
    } else if (subject instanceof Award) {
      subscribers = newArrayList(employee.manager(), employee.paymaster());
    } else if (subject instanceof Meeting) {
      subscribers = ((Meeting) deleted).participants();
    } else if (subject instanceof OutOfOffice) {
      subscribers = newArrayList(employee.manager(), employee.paymaster());
    } else if (subject instanceof Vocation) {
      subscribers = newArrayList(employee.manager(), employee.paymaster());
    } else if (subject instanceof SpentTime) {
      subscribers = singletonList(employee.manager());
    } else {
      throw new RuntimeException();
    }
    messenger.send(newLinkedHashSet(subscribers), new MessageBean(author, currentUTCMs(), deleted));
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Accessors(fluent = true)
  @Getter
  private static class Approved {

    private final int id;

    private final Employee owner;

    private final Subject subject;
  }
}
