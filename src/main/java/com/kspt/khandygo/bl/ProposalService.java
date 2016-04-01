package com.kspt.khandygo.bl;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.kspt.khandygo.bl.entities.beans.MessageBean;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.ApprovedApi;
import com.kspt.khandygo.core.apis.ProposalApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject;
import com.kspt.khandygo.core.sys.Messenger;
import static com.kspt.khandygo.utils.TimeUtils.currentUTCMs;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import javax.inject.Inject;

public class ProposalService implements ProposalApi {

  private final Repository<Proposal> repository;

  private final Messenger messenger;

  private final ApprovedApi approvedApi;

  @Inject
  ProposalService(
      final Repository<Proposal> repository,
      final Messenger messenger,
      final ApprovedApi approvedApi) {
    this.repository = repository;
    this.messenger = messenger;
    this.approvedApi = approvedApi;
  }

  @Override
  public int propose(final Employee author, final Subject subject) {
    final int id = addOnBehalfOf(author, subject);
    messenger.send(subject.employee(), new MessageBean(author, currentUTCMs(), subject));
    return id;
  }

  private int addOnBehalfOf(final Employee author, final Subject subject) {
    Preconditions.checkState(subject.when() > currentUTCMs());
    final Proposal proposal = new Proposal(-1, currentUTCMs(), author, subject);
    return repository.add(proposal);
  }

  @Override
  public int approve(final int id, final Employee requester) {
    final Proposal deleted = deleteOnBehalfOf(id, requester);
    return approvedApi.add(deleted.subject(), requester);
  }

  @Override
  public void reject(final int id, final Employee requester) {
    final Proposal deleted = deleteOnBehalfOf(id, requester);
    messenger.send(deleted.author(), new MessageBean(requester, currentUTCMs(), deleted));
  }

  private Proposal deleteOnBehalfOf(final int id, final Employee requester) {
    final Proposal found = repository.get(id);
    Verify.verify(Objects.equal(found.subject().employee(), requester));
    final Proposal deleted = repository.delete(id);
    Verify.verify(Objects.equal(found, deleted));
    return deleted;
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Accessors(fluent = true)
  @Getter
  private static class Proposal {

    private final int id;

    private final long origin;

    private final Employee author;

    private final Subject subject;
  }
}
