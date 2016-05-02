package com.kspt.khandygo.persistence;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject;
import com.kspt.khandygo.core.entities.Subject.Proposal.Award;
import com.kspt.khandygo.core.entities.Subject.Proposal.Meeting;
import com.kspt.khandygo.core.entities.Subject.Proposal.Vocation;
import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

abstract class SubjectEntry implements Subject, Entry {

  @Id
  private Long id;

  private boolean deleted;

  private SubjectEntry(final Long id, final boolean deleted) {
    this.id = id;
    this.deleted = deleted;
  }

  @Override
  public Entry id(final long id) {
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
  public long id() {
    return id;
  }

  @Entity
  @Table(name = "meetings")
  @Accessors(fluent = true)
  @Getter
  static class MeetingEntry extends SubjectEntry {
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

    private MeetingEntry(
        final Long id,
        final boolean deleted,
        final long origin,
        final long when,
        final long duration,
        final String where,
        final String description,
        final Employee author,
        final List<Employee> participants,
        final Employee employee) {
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
    public <T> T accept(final SubjectVisitor<T> visitor) {
      final Meeting subjectOfThis =
          new Meeting(origin, when, duration, where, description, author, participants,
              employee());
      return visitor.visit(subjectOfThis);
    }

    static SubjectEntry newOne(final Meeting subject) {
      return new MeetingEntry(
          null,
          false,
          subject.origin(),
          subject.when(),
          subject.duration(),
          subject.where(),
          subject.description(),
          subject.author(),
          subject.participants(),
          subject.employee());
    }
  }

  @Entity
  @Table(name = "vocations")
  @Accessors(fluent = true)
  @Getter
  static class VocationEntry extends SubjectEntry {
    private final long origin;

    private final long when;

    private final long duration;

    @ManyToOne
    private final Employee employee;

    private VocationEntry(
        final Long id,
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
    public <T> T accept(final SubjectVisitor<T> visitor) {
      final Vocation subjectOfThis = new Vocation(origin, when, duration, employee());
      return visitor.visit(subjectOfThis);
    }

    static SubjectEntry newOne(final Vocation subject) {
      return new VocationEntry(
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
  static class AwardEntry extends SubjectEntry {
    private final long origin;

    private final long when;

    private final long amount;

    @ManyToOne
    private final Employee employee;

    private AwardEntry(
        final Long id,
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
    public <T> T accept(final SubjectVisitor<T> visitor) {
      final Award subjectOfThis = new Award(origin, when, amount, employee());
      return visitor.visit(subjectOfThis);
    }

    static SubjectEntry newOne(final Award subject) {
      return new AwardEntry(
          null,
          false,
          subject.origin(),
          subject.when(),
          subject.amount(),
          subject.employee());
    }
  }

  @Entity
  @Table(name = "out_of_offices")
  @Accessors(fluent = true)
  @Getter
  static class OutOfOfficeEntry extends SubjectEntry {
    private final long origin;

    private final long when;

    private final long duration;

    private final String reason;

    @ManyToOne
    private final Employee employee;

    private OutOfOfficeEntry(
        final Long id,
        final boolean deleted,
        final long origin,
        final long when,
        final long duration, final String reason, final Employee employee) {
      super(id, deleted);
      this.origin = origin;
      this.when = when;
      this.duration = duration;
      this.reason = reason;
      this.employee = employee;
    }

    @Override
    public <T> T accept(final SubjectVisitor<T> visitor) {
      final OutOfOffice subjectOfThis = new OutOfOffice(origin, when, duration, reason,
          employee());
      return visitor.visit(subjectOfThis);
    }

    static SubjectEntry newOne(final OutOfOffice subject) {
      return new AwardEntry(
          null,
          false,
          subject.origin(),
          subject.when(),
          subject.duration(),
          subject.employee());
    }
  }

  @Entity
  @Table(name = "spent_times")
  @Accessors(fluent = true)
  @Getter
  static class SpentTimeEntry extends SubjectEntry {
    private final long origin;

    private final long when;

    private final long duration;

    private final String description;

    @ManyToOne
    private final Employee employee;

    private SpentTimeEntry(
        final Long id,
        final boolean deleted,
        final long origin,
        final long when,
        final long duration, final String description, final Employee employee) {
      super(id, deleted);
      this.origin = origin;
      this.when = when;
      this.duration = duration;
      this.description = description;
      this.employee = employee;
    }

    @Override
    public <T> T accept(final SubjectVisitor<T> visitor) {
      final SpentTime subjectOfThis =
          new SpentTime(origin, when, duration, description, employee());
      return visitor.visit(subjectOfThis);
    }

    static SubjectEntry newOne(final SpentTime subject) {
      return new SpentTimeEntry(
          null,
          false,
          subject.origin(),
          subject.when(),
          subject.duration(),
          subject.description(),
          subject.employee());
    }
  }

  @Entity
  @Table(name = "regular_payments")
  @Accessors(fluent = true)
  @Getter
  static class RegularPaymentEntry extends SubjectEntry {
    private final long origin;

    private final long when;

    private final long amount;

    @ManyToOne
    private final Employee employee;

    private RegularPaymentEntry(
        final Long id,
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
    public <T> T accept(final SubjectVisitor<T> visitor) {
      final RegularPayment subjectOfThis = new RegularPayment(origin, when, amount, employee());
      return visitor.visit(subjectOfThis);
    }

    static SubjectEntry newOne(final RegularPayment subject) {
      return new RegularPaymentEntry(
          null,
          false,
          subject.origin(),
          subject.when(),
          subject.amount(),
          subject.employee());
    }
  }
}