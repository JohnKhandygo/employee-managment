package com.kspt.khandygo.persist;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.Employee;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "groups")
class GroupEntry {

  @Id
  private final Integer id;

  @OneToMany
  private final List<Employee> participants;

  private GroupEntry(
      final Integer id,
      final List<Employee> participants) {
    this.id = id;
    this.participants = participants;
  }

  public static GroupEntry newOne(final List<Employee> participants) {
    Preconditions.checkNotNull(participants);
    return new GroupEntry(null, participants);
  }

  public static GroupEntry existedOne(final Integer id, final List<Employee> participants) {
    Preconditions.checkNotNull(id);
    Preconditions.checkNotNull(participants);
    return new GroupEntry(id, participants);
  }
}
