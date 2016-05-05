package com.kspt.khandygo.persistence.dao;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.Vocation;
import com.kspt.khandygo.persistence.Gateway;
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
    return gateway.find(VocationEntity.class).where().eq("id", id).unique();
  }

  public Vocation update(final int id, final Vocation model) {
    final VocationEntity vocationEntity = VocationEntity.existedOne(id, model);
    return gateway.update(vocationEntity);
  }

  @Entity
  @Table(name = "vocations")
  @EqualsAndHashCode(callSuper = true)
  @ToString
  private static class VocationEntity extends Vocation {
    @Id
    private final Integer id;

    private VocationEntity(
        final EmployeeEntity employee,
        final long when,
        final long duration,
        final boolean approved,
        final boolean rejected,
        final boolean cancelled,
        final Integer id) {
      super(employee, when, duration, approved, rejected, cancelled);
      this.id = id;
    }

    static VocationEntity newOne(final Vocation vocation) {
      Preconditions.checkState(vocation.employee() instanceof EmployeeEntity,
          "There is no sufficient type information to save %s.", vocation);
      return new VocationEntity(
          (EmployeeEntity) vocation.employee(),
          vocation.when(),
          vocation.duration(),
          vocation.approved(),
          vocation.rejected(),
          vocation.cancelled(),
          null);
    }

    static VocationEntity existedOne(final int id, final Vocation vocation) {
      Preconditions.checkState(vocation.employee() instanceof EmployeeEntity,
          "There is no sufficient type information to save %s.", vocation);
      return new VocationEntity(
          (EmployeeEntity) vocation.employee(),
          vocation.when(),
          vocation.duration(),
          vocation.approved(),
          vocation.rejected(),
          vocation.cancelled(),
          id);
    }
  }
}
