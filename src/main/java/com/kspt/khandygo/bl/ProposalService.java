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
import java.util.function.Function;

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
    messenger.send(added.recipient(), new MessageBean(author, currentUTCMs(), added));
    return added;
  }

  @Override
  public void approve(final int id, final Employee requester) {
    final Proposal found = getProposalSafely(id, requester);
    final Proposal updated = update(Proposal::approve, found);
    final Approved proposalSubject = updated.subject();
    if (proposalSubject instanceof TimeHolder) {
      timeHoldersApi.add((TimeHolder) proposalSubject);
    } else if (proposalSubject instanceof Award) {
      paymentsApi.award((Award) proposalSubject);
    } else {
      throw new RuntimeException();
    }
  }

  @Override
  public void reject(final int id, final Employee requester) {
    final Proposal found = getProposalSafely(id, requester);
    final Proposal updated = update(Proposal::reject, found);
    notify(requester, updated);
  }

  private void notify(final Employee requester, final Proposal updated) {
    messenger.send(updated.author(), new MessageBean(requester, currentUTCMs(), updated));
  }

  private Proposal update(final Function<Proposal, Proposal> beforeUpdate, final Proposal found) {
    final Proposal committed = beforeUpdate.apply(found);
    return proposals.update(committed);
  }

  private Proposal getProposalSafely(final int id, final Employee requester) {
    final Proposal found = proposals.get(id);
    checkConsistency(requester, found);
    return found;
  }

  private void checkConsistency(final Employee requester, final Proposal proposal) {
    Preconditions.checkState(proposal.pending());
    Preconditions.checkState(Objects.equal(proposal.recipient(), requester));
  }
}
