package com.kspt.khandygo.bl;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import static com.google.common.collect.Lists.newArrayList;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.Notifier;
import com.kspt.khandygo.core.apis.SubjectsApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject;
import com.kspt.khandygo.core.entities.Subject.OutOfOffice;
import com.kspt.khandygo.core.entities.Subject.Proposal.Vocation;
import com.kspt.khandygo.core.entities.Subject.SubjectVisitor;
import static java.util.Collections.singletonList;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

class SubjectsService implements SubjectsApi {

  private static final SubjectVisitor<List<Employee>> ON_ADD_SUBSCRIBERS_AUDIT =
      SubjectVisitor.from(
          /*Meeting::participants,*/
          outOfOffice -> singletonList(outOfOffice.employee().manager()),
          /*spentTime -> singletonList(spentTime.employee().manager()),*/
          vocation -> singletonList(vocation.employee()),
          award -> singletonList(award.employee().paymaster())/*,
          regularPayment -> singletonList(regularPayment.employee())*/);

  private static final SubjectVisitor<List<Employee>> ON_CANCEL_SUBSCRIBERS_AUDIT =
      SubjectVisitor.from(
          /*Meeting::participants,*/
          outOfOffice -> singletonList(outOfOffice.employee().manager()),
          /*spentTime -> singletonList(spentTime.employee().manager()),*/
          vocation -> newArrayList(vocation.employee().manager(), vocation.employee().paymaster()),
          award -> newArrayList(award.employee().manager(), award.employee().paymaster())/*,
          regularPayment -> singletonList(regularPayment.employee())*/);

  private final Repository<Subject> repository;

  private final Notifier notifier;

  @Inject
  SubjectsService(final Repository<Subject> repository, final Notifier notifier) {
    this.repository = repository;
    this.notifier = notifier;
  }

  @Override
  public int add(final Subject toAdd, final Employee author) {
    Preconditions.checkState(SubjectsService.AccessRightsChecker.onAdd().check(toAdd, author),
        "access rights check failed for %s on adding %s.", author, toAdd);
    final int id = repository.add(toAdd);
    notifier.notify(toAdd.accept(ON_ADD_SUBSCRIBERS_AUDIT))
        .that(toAdd).hasBeen("added").onBehalfOf(author);
    return id;
  }

  @Override
  public Subject cancel(final int id, final Employee requester) {
    final Subject toCancel = repository.get(id);
    Verify.verify(SubjectsService.AccessRightsChecker.onAdd().check(toCancel, requester),
        "access rights check failed for %s on adding %s.", requester, toCancel);
    notifier.notify(toCancel.accept(ON_CANCEL_SUBSCRIBERS_AUDIT))
        .that(toCancel).hasBeen("added").onBehalfOf(requester);
    //TODO in case of meeting and author need to find all already approved and cancel them to.
    return repository.delete(id);
  }

  @Slf4j
  private static class AccessRightsChecker {

    private final SubjectVisitor<Employee> owningAudit;

    private AccessRightsChecker(final SubjectVisitor<Employee> owningAudit) {
      this.owningAudit = owningAudit;
    }

    boolean check(final Subject subject, final Employee requester) {
      final Employee expected = subject.accept(owningAudit);
      if (Objects.equals(expected, requester)) {
        return true;
      } else {
        log.error("expecting {} but got {} when checking access rights.", expected, requester);
        return false;
      }
    }

    static SubjectsService.AccessRightsChecker onAdd() {
      return new SubjectsService.AccessRightsChecker(
          SubjectVisitor.from(
              /*Meeting::author,*/
              OutOfOffice::employee,
              /*SpentTime::employee,*/
              vocation -> vocation.employee().manager(),
              award -> award.employee().paymaster()/*,
              regularPayment -> regularPayment.employee().paymaster()*/));
    }

    static SubjectsService.AccessRightsChecker onCancel() {
      return new SubjectsService.AccessRightsChecker(
          SubjectVisitor.from(
              /*Meeting::employee,*/
              OutOfOffice::employee,
              /*SpentTime::employee,*/
              Vocation::employee,
              award -> award.employee().manager()/*,
              regularPayment -> regularPayment.employee().paymaster()*/));
    }
  }
}
