package com.kspt.khandygo.bl;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.kspt.khandygo.bl.entities.beans.MessageBean;
import com.kspt.khandygo.bl.entities.payments.Award;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.PaymentsApi;
import com.kspt.khandygo.core.apis.ProposalApi;
import com.kspt.khandygo.core.apis.TimeHoldersApi;
import com.kspt.khandygo.core.entities.Approved;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Proposal;
import com.kspt.khandygo.core.entities.approved.TimeHolder;
import com.kspt.khandygo.core.sys.Messenger;
import static com.kspt.khandygo.utils.TimeUtils.currentUTCMs;
import javax.inject.Inject;
import java.util.function.BiFunction;

public class ProposalService implements ProposalApi {

  private final Repository<Proposal> proposals;

  private final Messenger messenger;

  private final TimeHoldersApi timeHoldersApi;

  private final PaymentsApi paymentsApi;

  @Inject
  public ProposalService(
      final Repository<Proposal> proposals,
      final Messenger messenger,
      final TimeHoldersApi timeHoldersApi,
      final PaymentsApi paymentsApi) {
    this.proposals = proposals;
    this.messenger = messenger;
    this.timeHoldersApi = timeHoldersApi;
    this.paymentsApi = paymentsApi;
  }

  @Override
  public Proposal propose(final Proposal proposal) {
    Preconditions.checkState(proposal.when() > currentUTCMs());
    final Proposal added = proposals.add(proposal);
    final Employee author = added.author();
    messenger.send(added.recipient(), new MessageBean(-1, author, currentUTCMs(), added));
    return added;
  }

  @Override
  public void approve(final int id, final Employee requester) {
    final Proposal updated = commitAndNotify(id, Proposal::approveBy, requester);
    final Approved proposalSubject = updated.subject();
    if (proposalSubject instanceof TimeHolder) {
      timeHoldersApi.reserve((TimeHolder) proposalSubject);
    } else if (proposalSubject instanceof Award) {
      paymentsApi.award((Award) proposalSubject);
    }
  }

  @Override
  public void reject(final int id, final Employee requester) {
    commitAndNotify(id, Proposal::rejectBy, requester);
  }

  private Proposal commitAndNotify(
      final int id,
      final BiFunction<Proposal, Employee, Proposal> committer,
      final Employee requester) {
    final Proposal found = proposals.get(id);
    checkConsistency(requester, found);
    final Proposal committed = committer.apply(found, requester);
    final Proposal updated = proposals.update(committed);
    messenger.send(updated.recipient(), new MessageBean(-1, requester, currentUTCMs(), updated));
    return updated;
  }

  private void checkConsistency(final Employee requester, final Proposal found) {
    Preconditions.checkState(found.pending());
    Preconditions.checkState(Objects.equal(found.recipient(), requester));
  }
}
