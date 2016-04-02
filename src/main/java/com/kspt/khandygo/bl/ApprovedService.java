package com.kspt.khandygo.bl;

import com.google.common.base.Objects;
import com.google.common.base.Verify;
import com.kspt.khandygo.bl.entities.subjects.payments.Award;
import com.kspt.khandygo.bl.entities.subjects.payments.RegularPayment;
import com.kspt.khandygo.bl.entities.subjects.th.Meeting;
import com.kspt.khandygo.bl.entities.subjects.th.OutOfOffice;
import com.kspt.khandygo.bl.entities.subjects.th.SpentTime;
import com.kspt.khandygo.bl.entities.subjects.th.Vocation;
import com.kspt.khandygo.core.Notifier;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.ApprovedApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import javax.inject.Inject;

public class ApprovedService implements ApprovedApi {

  private final Repository<Approved> repository;

  private final Notifier notifier;

  @Inject
  ApprovedService(
      final Repository<Approved> repository,
      final Notifier notifier) {
    this.repository = repository;
    this.notifier = notifier;
  }

  @Override
  public void cancel(final int id, final Employee requester) {
    final Approved approved = repository.get(id);
    Verify.verify(Objects.equal(requester, approved.owner()));
    final Approved deleted = repository.delete(id);
    Verify.verify(Objects.equal(approved, deleted));
    notifier.notifyThat(deleted.subject()).hasBeenDeleted().onBehalfOf(requester);
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
    final Approved approved = new Approved(owner, subject);
    final int id = repository.add(approved);
    notifier.notifyThat(subject).hasBeenCreated().onBehalfOf(author);
    return id;
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Accessors(fluent = true)
  @Getter
  private static class Approved {

    private final Employee owner;

    private final Subject subject;
  }
}
