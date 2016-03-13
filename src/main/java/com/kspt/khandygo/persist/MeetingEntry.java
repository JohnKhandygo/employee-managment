package com.kspt.khandygo.persist;

import com.google.common.base.Preconditions;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Entity
@Table(
    name = "meetings",
    uniqueConstraints = @UniqueConstraint(columnNames = {"when", "where"}))
class MeetingEntry {

  @Id
  private final Integer id;

  @Column(nullable = false)
  private final Long when;

  @ManyToOne
  private final MeetingLocationEntry where;

  @ManyToOne
  private final EmployeeEntry author;

  @ManyToOne
  private final GroupEntry group;

  private MeetingEntry(
      final Integer id,
      final Long when,
      final MeetingLocationEntry where,
      final EmployeeEntry author,
      final GroupEntry group) {
    this.id = id;
    this.when = when;
    this.where = where;
    this.author = author;
    this.group = group;
  }

  public static MeetingEntry newOne(
      final Long when,
      final MeetingLocationEntry where,
      final EmployeeEntry author,
      final GroupEntry group) {
    Preconditions.checkNotNull(when);
    final long nowPlusTenMinutes = Instant.now().toEpochMilli() + TimeUnit.MINUTES.toMillis(10);
    Preconditions.checkState(when > nowPlusTenMinutes);
    Preconditions.checkNotNull(where);
    Preconditions.checkNotNull(author);
    Preconditions.checkNotNull(group);
    return new MeetingEntry(null, when, where, author, group);
  }

  public static MeetingEntry existedOne(
      final Integer id,
      final Long when,
      final MeetingLocationEntry where,
      final EmployeeEntry author,
      final GroupEntry group) {
    Preconditions.checkNotNull(id);
    Preconditions.checkNotNull(when);
    final long nowPlusTenMinutes = Instant.now().toEpochMilli() + TimeUnit.MINUTES.toMillis(10);
    Preconditions.checkState(when > nowPlusTenMinutes);
    Preconditions.checkNotNull(where);
    Preconditions.checkNotNull(author);
    Preconditions.checkNotNull(group);
    return new MeetingEntry(id, when, where, author, group);
  }
}
