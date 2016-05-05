package com.kspt.khandygo.persistence.dao;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.OutOfOffice;
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
public class OutOfOfficesDAO {

  private final Gateway gateway;

  public int save(final OutOfOffice outOfOffice) {
    final OutOfOfficeEntity outOfOfficeEntity = OutOfOfficeEntity.newOne(outOfOffice);
    return gateway.save(outOfOfficeEntity).id;
  }

  public OutOfOffice get(final int id) {
    return gateway.find(OutOfOfficeEntity.class).where().eq("id", id).unique();
  }

  public OutOfOffice update(final int id, final OutOfOffice model) {
    final OutOfOfficeEntity outOfOfficeEntity = OutOfOfficeEntity.existedOne(id, model);
    return gateway.update(outOfOfficeEntity);
  }

  @Entity
  @Table(name = "out_of_offices")
  @EqualsAndHashCode(callSuper = true)
  @ToString
  private static class OutOfOfficeEntity extends OutOfOffice {
    @Id
    private final Integer id;

    private OutOfOfficeEntity(
        final EmployeeEntity employee,
        final long when,
        final long duration,
        final String reason,
        final boolean cancelled,
        final Integer id) {
      super(employee, when, duration, reason, cancelled);
      this.id = id;
    }

    static OutOfOfficeEntity newOne(final OutOfOffice outOfOffice) {
      Preconditions.checkState(outOfOffice.employee() instanceof EmployeeEntity,
          "There is no sufficient type information to save %s.", outOfOffice);
      return new OutOfOfficeEntity(
          (EmployeeEntity) outOfOffice.employee(),
          outOfOffice.when(),
          outOfOffice.duration(),
          outOfOffice.reason(),
          outOfOffice.cancelled(),
          null);
    }

    static OutOfOfficeEntity existedOne(final int id, final OutOfOffice outOfOffice) {
      Preconditions.checkState(outOfOffice.employee() instanceof EmployeeEntity,
          "There is no sufficient type information to save %s.", outOfOffice);
      return new OutOfOfficeEntity(
          (EmployeeEntity) outOfOffice.employee(),
          outOfOffice.when(),
          outOfOffice.duration(),
          outOfOffice.reason(),
          outOfOffice.cancelled(),
          id);
    }
  }
}
