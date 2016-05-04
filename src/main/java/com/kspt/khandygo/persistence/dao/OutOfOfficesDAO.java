package com.kspt.khandygo.persistence.dao;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.OutOfOffice;
import com.kspt.khandygo.persistence.Gateway;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor(onConstructor = @__({@Inject}))
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
  private static class OutOfOfficeEntity extends OutOfOffice {
    @Id
    private final Integer id;

    private OutOfOfficeEntity(
        final Employee employee,
        final long when,
        final long duration,
        final String reason,
        final boolean cancelled,
        final Integer id) {
      super(employee, when, duration, reason, cancelled);
      this.id = id;
    }

    static OutOfOfficeEntity newOne(final OutOfOffice outOfOffice) {
      return new OutOfOfficeEntity(
          outOfOffice.employee(),
          outOfOffice.when(),
          outOfOffice.duration(),
          outOfOffice.reason(),
          outOfOffice.cancelled(),
          null);
    }

    static OutOfOfficeEntity existedOne(final int id, final OutOfOffice outOfOffice) {
      return new OutOfOfficeEntity(
          outOfOffice.employee(),
          outOfOffice.when(),
          outOfOffice.duration(),
          outOfOffice.reason(),
          outOfOffice.cancelled(),
          id);
    }
  }
}
