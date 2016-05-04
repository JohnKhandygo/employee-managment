package com.kspt.khandygo.persistence.dao;

import com.kspt.khandygo.core.entities.Award;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.persistence.Gateway;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor(onConstructor = @__({@Inject}))
public class AwardsDAO {

  private final Gateway gateway;

  public int save(final Award award) {
    final AwardEntity awardEntity = AwardEntity.newOne(award);
    return gateway.save(awardEntity).id;
  }

  public Award get(final int id) {
    return gateway.find(AwardEntity.class).where().eq("id", id).unique();
  }

  public Award update(final int id, final Award model) {
    final AwardEntity awardEntity = AwardEntity.existedOne(id, model);
    return gateway.update(awardEntity);
  }

  @Entity
  @Table(name = "awards")
  private static class AwardEntity extends Award {
    @Id
    private final Integer id;

    private AwardEntity(
        final Employee employee,
        final long when,
        final long amount,
        final boolean approved,
        final boolean rejected,
        final boolean cancelled,
        final Integer id) {
      super(employee, when, amount, approved, rejected, cancelled);
      this.id = id;
    }

    static AwardEntity newOne(final Award award) {
      return new AwardEntity(
          award.employee(),
          award.when(),
          award.amount(),
          award.approved(),
          award.rejected(),
          award.cancelled(),
          null);
    }

    static AwardEntity existedOne(final int id, final Award award) {
      return new AwardEntity(
          award.employee(),
          award.when(),
          award.amount(),
          award.approved(),
          award.rejected(),
          award.cancelled(),
          id);
    }
  }
}
