package com.kspt.khandygo.bl;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.kspt.khandygo.core.Notifier;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.ApprovedApi;
import com.kspt.khandygo.core.apis.ProposalApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Proposal;
import com.kspt.khandygo.core.entities.Subject;
import static com.kspt.khandygo.utils.TimeUtils.currentUTCMs;
import javax.inject.Inject;

public class ProposalService implements ProposalApi {

  private final Repository<Proposal> repository;

  private final Notifier notifier;

  private final ApprovedApi approvedApi;

  @Inject
  ProposalService(
      final Repository<Proposal> repository,
      final Notifier notifier,
      final ApprovedApi approvedApi) {
    this.repository = repository;
    this.notifier = notifier;
    this.approvedApi = approvedApi;
  }

  @Override
  public int propose(final Employee author, final Subject subject) {
    final int id = addOnBehalfOf(author, subject);
    notifier.notifyThat(subject).hasBeenProposed().onBehalfOf(author);
    return id;
  }

  private int addOnBehalfOf(final Employee author, final Subject subject) {
    Preconditions.checkState(subject.when() > currentUTCMs());
    final Proposal proposal = new Proposal(currentUTCMs(), author, subject);
    return repository.add(proposal);
  }

  @Override
  public int approve(final int id, final Employee requester) {
    final Proposal deleted = deleteOnBehalfOf(id, requester);
    notifier.notifyThat(deleted.subject()).hasBeenApproved().onBehalfOf(requester);
    return approvedApi.add(deleted.subject(), requester);
  }

  @Override
  public void reject(final int id, final Employee requester) {
    final Proposal deleted = deleteOnBehalfOf(id, requester);
    notifier.notifyThat(deleted.subject()).hasBeenRejected().onBehalfOf(requester);
  }

  private Proposal deleteOnBehalfOf(final int id, final Employee requester) {
    final Proposal found = repository.get(id);
    Verify.verify(Objects.equal(found.subject().employee(), requester));
    final Proposal deleted = repository.delete(id);
    Verify.verify(Objects.equal(found, deleted));
    return deleted;
  }
}
