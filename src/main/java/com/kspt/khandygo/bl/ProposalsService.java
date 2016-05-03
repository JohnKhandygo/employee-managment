package com.kspt.khandygo.bl;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import static com.google.common.collect.Lists.newArrayList;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.Notifier;
import com.kspt.khandygo.core.apis.ProposalsApi;
import com.kspt.khandygo.core.apis.SubjectsApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject.Proposal;
import com.kspt.khandygo.core.entities.Subject.Proposal.ProposableSubjectVisitor;
import com.kspt.khandygo.core.entities.Subject.Proposal.Vocation;
import static java.util.Collections.singletonList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor(onConstructor = @__({@Inject}))
class ProposalsService implements ProposalsApi {

  private static final ProposableSubjectVisitor<List<Employee>> ON_PROPOSE_SUBSCRIBERS_AUDIT =
      ProposableSubjectVisitor.from(
          /*Meeting::participants,*/
          vocation -> singletonList(vocation.employee().manager()),
          award -> singletonList(award.employee().paymaster()));

  private static final ProposableSubjectVisitor<List<Employee>> ON_APPROVE_SUBSCRIBERS_AUDIT =
      ProposableSubjectVisitor.from(
          /*Meeting::participants,*/
          vocation -> newArrayList(vocation.employee(), vocation.employee().paymaster()),
          award -> newArrayList(award.employee(), award.employee().manager()));

  private static final ProposableSubjectVisitor<List<Employee>> ON_REJECT_SUBSCRIBERS_AUDIT =
      ProposableSubjectVisitor.from(
          /*Meeting::participants,*/
          vocation -> singletonList(vocation.employee()),
          award -> singletonList(award.employee().manager()));

  private static final ProposableSubjectVisitor<List<Employee>> ON_UPDATE_SUBSCRIBERS_AUDIT =
      ProposableSubjectVisitor.from(
          /*Meeting::participants,*/
          vocation -> singletonList(vocation.employee().manager()),
          award -> singletonList(award.employee().paymaster()));

  private static final ProposableSubjectVisitor<List<Employee>> ON_CANCEL_SUBSCRIBERS_AUDIT =
      ProposableSubjectVisitor.from(
          /*Meeting::participants,*/
          vocation -> singletonList(vocation.employee().manager()),
          award -> singletonList(award.employee().paymaster()));

  private final Repository<Proposal> repository;

  private final Notifier notifier;

  private final SubjectsApi subjectsApi;

  @Override
  public int propose(final Proposal toPropose, final Employee author) {
    Preconditions
        .checkState(ProposalsService.AccessRightsChecker.onPropose().check(toPropose, author),
            "access rights check failed for %s on proposing %s.", author, toPropose);
    final int id = repository.add(toPropose);
    //TODO for author need to add through subjects api.
    notifier.notify(toPropose.accept(ON_PROPOSE_SUBSCRIBERS_AUDIT))
        .that(toPropose).hasBeen("proposed").onBehalfOf(author);
    return id;
  }

  @Override
  public void approve(final int id, final Employee requester) {
    final Proposal toApprove = repository.get(id);
    Verify.verify(ProposalsService.AccessRightsChecker.onApprove().check(toApprove, requester),
        "access rights check failed for %s on approving %s.", requester, toApprove);
    notifier.notify(toApprove.accept(ON_APPROVE_SUBSCRIBERS_AUDIT))
        .that(toApprove).hasBeen("approved").onBehalfOf(requester);
    subjectsApi.add(toApprove, requester);
    repository.delete(id);
  }

  @Override
  public void reject(final int id, final Employee requester) {
    final Proposal toReject = repository.get(id);
    Verify.verify(ProposalsService.AccessRightsChecker.onReject().check(toReject, requester),
        "access rights check failed for %s on rejecting %s.", requester, toReject);
    repository.delete(id);
    notifier.notify(toReject.accept(ON_REJECT_SUBSCRIBERS_AUDIT))
        .that(toReject).hasBeen("rejected").onBehalfOf(requester);
  }

  @Override
  public void update(final int id, final Proposal toUpdate, final Employee requester) {
    final Proposal toBeUpdated = repository.get(id);
    Verify.verify(ProposalsService.AccessRightsChecker.onUpdate().check(toBeUpdated, requester),
        "access rights check failed for %s on updating %s.", requester, toBeUpdated);
    Verify.verify(Objects.equals(toUpdate.employee(), toBeUpdated.employee()),
        "expecting both subjects to reference on %s but got %s.",
        toBeUpdated.employee(), toUpdate.employee());
    repository.update(id, toUpdate);
    notifier.notify(toUpdate.accept(ON_UPDATE_SUBSCRIBERS_AUDIT))
        .that(toUpdate).hasBeen("updated").onBehalfOf(requester);
  }

  @Override
  public Proposal cancel(final int id, final Employee requester) {
    final Proposal toCancel = repository.get(id);
    Verify.verify(ProposalsService.AccessRightsChecker.onCancel().check(toCancel, requester),
        "access rights check failed for %s on cancelling %s.", requester, toCancel);
    notifier.notify(toCancel.accept(ON_CANCEL_SUBSCRIBERS_AUDIT))
        .that(toCancel).hasBeen("cancelled").onBehalfOf(requester);
    //TODO in case of meeting need to find all already approved and cancel them to.
    return repository.delete(id);
  }

  @Slf4j
  private static class AccessRightsChecker {

    private final ProposableSubjectVisitor<Employee> auditor;

    private AccessRightsChecker(final ProposableSubjectVisitor<Employee> auditor) {
      this.auditor = auditor;
    }

    boolean check(final Proposal subject, final Employee requester) {
      final Employee expected = subject.accept(auditor);
      if (Objects.equals(expected, requester)) {
        return true;
      } else {
        log.error("expecting {} but got {} when checking access rights.", expected, requester);
        return false;
      }
    }

    static ProposalsService.AccessRightsChecker onPropose() {
      return new ProposalsService.AccessRightsChecker(
          ProposableSubjectVisitor.from(
              /*Meeting::author,*/
              Vocation::employee,
              award -> award.employee().manager()));
    }

    static ProposalsService.AccessRightsChecker onApprove() {
      return new ProposalsService.AccessRightsChecker(
          ProposableSubjectVisitor.from(
              /*Meeting::employee,*/
              vocation -> vocation.employee().manager(),
              award -> award.employee().paymaster()));
    }

    static ProposalsService.AccessRightsChecker onReject() {
      return new ProposalsService.AccessRightsChecker(
          ProposableSubjectVisitor.from(
              /*Meeting::employee,*/
              vocation -> vocation.employee().manager(),
              award -> award.employee().paymaster()));
    }

    static ProposalsService.AccessRightsChecker onUpdate() {
      return new ProposalsService.AccessRightsChecker(
          ProposableSubjectVisitor.from(
              /*Meeting::author,*/
              Vocation::employee,
              award -> award.employee().manager()));
    }

    static ProposalsService.AccessRightsChecker onCancel() {
      return new ProposalsService.AccessRightsChecker(
          ProposableSubjectVisitor.from(
              /*Meeting::author,*/
              Vocation::employee,
              award -> award.employee().manager()));
    }
  }
}
