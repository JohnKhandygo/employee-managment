package com.kspt.khandygo.persistence;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject.Proposal;
import com.kspt.khandygo.core.entities.Subject.Proposal.ProposableSubjectVisitor;
import com.kspt.khandygo.persistence.ProposalRepositoryOverGateway.ProposalEntry.AwardProposalEntry;
import com.kspt.khandygo.persistence.ProposalRepositoryOverGateway.ProposalEntry.MeetingProposalEntry;
import com.kspt.khandygo.persistence.ProposalRepositoryOverGateway.ProposalEntry.VocationProposalEntry;
import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

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

  static abstract class ProposalEntry implements Proposal, Entry {

    @Id
    private Integer id;

    private boolean deleted;

    private ProposalEntry(final Integer id, final boolean deleted) {
      this.id = id;
      this.deleted = deleted;
    }

    @Override
    public Entry id(final int id) {
      Preconditions.checkState(this.id == null);
      this.id = id;
      return this;
    }

    @Override
    public Entry markAsDeleted() {
      this.deleted = deleted();
      return this;
    }

    @Override
    public boolean deleted() {
      return deleted;
    }

    @Override
    public int id() {
      return id;
    }

    @Entity
    @Table(name = "meetings_proposals")
    @Accessors(fluent = true)
    @Getter
    static class MeetingProposalEntry extends ProposalEntry {
      private final long origin;

      private final long when;

      private final long duration;

      private final String where;

      private final String description;

      @ManyToOne
      private final Employee author;

      @OneToMany
      private final List<Employee> participants;

      @ManyToOne
      private final Employee employee;

      private MeetingProposalEntry(
          final Integer id,
          final boolean deleted,
          final long origin,
          final long when,
          final long duration,
          final String where,
          final String description,
          final Employee author, final List<Employee> participants, final Employee employee) {
        super(id, deleted);
        this.origin = origin;
        this.when = when;
        this.duration = duration;
        this.where = where;
        this.description = description;
        this.author = author;
        this.participants = participants;
        this.employee = employee;
      }

      @Override
      public <T> T accept(final ProposableSubjectVisitor<T> visitor) {
        final Meeting subjectOfThis =
            new Meeting(origin, when, duration, where, description, author, participants,
                employee());
        return visitor.visit(subjectOfThis);
      }

      @Override
      public <T> T accept(final SubjectVisitor<T> visitor) {
        final Meeting subjectOfThis =
            new Meeting(origin, when, duration, where, description, author, participants,
                employee());
        return visitor.visit(subjectOfThis);
      }

      static ProposalEntry newOne(final Meeting meeting) {
        return new MeetingProposalEntry(
            null,
            false,
            meeting.origin(),
            meeting.when(),
            meeting.duration(),
            meeting.where(),
            meeting.description(),
            meeting.author(),
            meeting.participants(),
            meeting.employee());
      }
    }

    @Entity
    @Table(name = "vocation_proposals")
    @Accessors(fluent = true)
    @Getter
    static class VocationProposalEntry extends ProposalEntry {
      private final long origin;

      private final long when;

      private final long duration;

      @ManyToOne
      private final Employee employee;

      private VocationProposalEntry(
          final Integer id,
          final boolean deleted,
          final long origin,
          final long when,
          final long duration, final Employee employee) {
        super(id, deleted);
        this.origin = origin;
        this.when = when;
        this.duration = duration;
        this.employee = employee;
      }

      @Override
      public <T> T accept(final ProposableSubjectVisitor<T> visitor) {
        final Vocation subjectOfThis = new Vocation(origin, when, duration, employee());
        return visitor.visit(subjectOfThis);
      }

      @Override
      public <T> T accept(final SubjectVisitor<T> visitor) {
        final Vocation subjectOfThis = new Vocation(origin, when, duration, employee());
        return visitor.visit(subjectOfThis);
      }

      static ProposalEntry newOne(final Vocation subject) {
        return new VocationProposalEntry(
            null,
            false,
            subject.origin(),
            subject.when(),
            subject.duration(),
            subject.employee());
      }
    }

    @Entity
    @Table(name = "awards")
    @Accessors(fluent = true)
    @Getter
    static class AwardProposalEntry extends ProposalEntry {
      private final long origin;

      private final long when;

      private final long amount;

      @ManyToOne
      private final Employee employee;

      private AwardProposalEntry(
          final Integer id,
          final boolean deleted,
          final long origin,
          final long when,
          final long amount,
          final Employee employee) {
        super(id, deleted);
        this.origin = origin;
        this.when = when;
        this.amount = amount;
        this.employee = employee;
      }

      @Override
      public <T> T accept(final ProposableSubjectVisitor<T> visitor) {
        final Award subjectOfThis = new Award(origin, when, amount, employee());
        return visitor.visit(subjectOfThis);
      }

      @Override
      public <T> T accept(final SubjectVisitor<T> visitor) {
        final Award subjectOfThis = new Award(origin, when, amount, employee());
        return visitor.visit(subjectOfThis);
      }

      static ProposalEntry newOne(final Award subject) {
        return new AwardProposalEntry(
            null,
            false,
            subject.origin(),
            subject.when(),
            subject.amount(),
            subject.employee());
      }
    }
  }
}