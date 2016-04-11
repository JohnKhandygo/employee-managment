package com.kspt.khandygo.persistence;

import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.entities.Subject.Proposal;
import com.kspt.khandygo.core.entities.Subject.Proposal.ProposableSubjectVisitor;
import com.kspt.khandygo.persistence.Entry.ProposalEntry;
import com.kspt.khandygo.persistence.Entry.ProposalEntry.AwardProposalEntry;
import com.kspt.khandygo.persistence.Entry.ProposalEntry.MeetingProposalEntry;
import com.kspt.khandygo.persistence.Entry.ProposalEntry.VocationProposalEntry;

public class ProposalRepositoryOverGateway implements Repository<Proposal> {

  private final Gateway<ProposalEntry> gateway;

  private final ProposableSubjectVisitor<ProposalEntry> persistenceAudit;

  private ProposalRepositoryOverGateway(
      final Gateway<ProposalEntry> gateway,
      final ProposableSubjectVisitor<ProposalEntry> persistenceAudit) {
    this.gateway = gateway;
    this.persistenceAudit = persistenceAudit;
  }

  @Override
  public int add(final Proposal p) {
    return gateway.save(p.accept(persistenceAudit)).id();
  }

  @Override
  public Proposal get(final int id) {
    return gateway.get(id);
  }

  @Override
  public void update(final int id, final Proposal p) {
    final Entry mapped = p.accept(persistenceAudit).id(id);
    gateway.update(mapped);
  }

  @Override
  public Proposal delete(final int id) {
    final Entry deleted = gateway.get(id).markAsDeleted();
    return gateway.update(deleted);
  }

  public static Repository<Proposal> newOne(final Gateway<ProposalEntry> gateway) {
    return new ProposalRepositoryOverGateway(
        gateway,
        ProposableSubjectVisitor.from(
            MeetingProposalEntry::newOne,
            VocationProposalEntry::newOne,
            AwardProposalEntry::newOne)
    );
  }
}
