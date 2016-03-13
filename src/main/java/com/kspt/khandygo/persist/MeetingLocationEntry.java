package com.kspt.khandygo.persist;

import com.google.common.base.Preconditions;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "meeting_locations")
class MeetingLocationEntry {

  @Id
  private final Integer id;

  @Column(nullable = false, unique = true)
  private final String name;

  private MeetingLocationEntry(final Integer id, final String name) {
    this.id = id;
    this.name = name;
  }

  public static MeetingLocationEntry newOne(final String name) {
    Preconditions.checkNotNull(name);
    return new MeetingLocationEntry(null, name);
  }

  public static MeetingLocationEntry existedOne(final Integer id, final String name) {
    Preconditions.checkNotNull(id);
    Preconditions.checkNotNull(name);
    return new MeetingLocationEntry(id, name);
  }
}
