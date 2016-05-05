package com.kspt.khandygo.persistence.dao;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.Vocation;
import com.kspt.khandygo.persistence.Gateway;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor(onConstructor = @__({@Inject}))
@Singleton
public class VocationsDAO {

  private final Gateway gateway;

  public int save(final Vocation vocation) {
    final VocationEntity vocationEntity = VocationEntity.newOne(vocation);
    return gateway.save(vocationEntity).id;
  }

  public Vocation get(final int id) {
    return gateway.find(VocationEntity.class).where().eq("id", id).unique().toVocation();
  }

  public Vocation update(final int id, final Vocation model) {
    final VocationEntity vocationEntity = VocationEntity.existedOne(id, model);
    return gateway.update(vocationEntity).toVocation();
  }

  @Entity
  @Table(name = "vocations")
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @EqualsAndHashCode
  @ToString
  private static class VocationEntity /*extends Vocation*/ {
    @Id
    private final Integer id;

    private final UserEntity employee;

    private final Long timestamp;

    private final Long duration;

    private final Boolean approved;

    private final Boolean rejected;

    private final Boolean cancelled;

    /*public VocationEntity(
        final Integer id,
        final EmployeeEntity employee,
        final Long timestamp,
        final Long duration,
        final Boolean approved, final Boolean rejected, final Boolean cancelled) {
      this.id = id;
      this.employee = employee;
      this.timestamp = timestamp;
      this.duration = duration;
      this.approved = approved;
      this.rejected = rejected;
      this.cancelled = cancelled;
    }*/

    static VocationEntity newOne(final Vocation vocation) {
      Preconditions.checkState(vocation.employee() instanceof UserEntity,
          "There is no sufficient type information to save %s.", vocation);
      return new VocationEntity(
          null,
          (UserEntity) vocation.employee(),
          vocation.when(),
          vocation.duration(),
          vocation.approved(),
          vocation.rejected(),
          vocation.cancelled());
    }

    static VocationEntity existedOne(final int id, final Vocation vocation) {
      Preconditions.checkState(vocation.employee() instanceof UserEntity,
          "There is no sufficient type information to save %s.", vocation);
      return new VocationEntity(
          id,
          (UserEntity) vocation.employee(),
          vocation.when(),
          vocation.duration(),
          vocation.approved(),
          vocation.rejected(),
          vocation.cancelled());
    }

    Vocation toVocation() {
      return new Vocation(employee, timestamp, duration, approved, rejected, cancelled);
    }
  }
}
