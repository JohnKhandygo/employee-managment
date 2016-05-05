package com.kspt.khandygo.persistence.dao;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.Award;
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
public class AwardsDAO {

  private final Gateway gateway;

  public int save(final Award award) {
    final AwardEntity awardEntity = AwardEntity.newOne(award);
    return gateway.save(awardEntity).id;
  }

  public Award get(final int id) {
    return gateway.find(AwardEntity.class).where().eq("id", id).unique().toAward();
  }

  public Award update(final int id, final Award model) {
    final AwardEntity awardEntity = AwardEntity.existedOne(id, model);
    return gateway.update(awardEntity).toAward();
  }

  @Entity
  @Table(name = "awards")
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @EqualsAndHashCode
  @ToString
  private static class AwardEntity /*extends Award*/ {
    @Id
    private final Integer id;

    private final UserEntity employee;

    private final Long timestamp;

    private final Long amount;

    private final Boolean approved;

    private final Boolean rejected;

    private final Boolean cancelled;

    /*private AwardEntity(
        final EmployeeEntity employee,
        final long timestamp,
        final long amount,
        final boolean approved,
        final boolean rejected,
        final boolean cancelled,
        final Integer id) {
      super(employee, timestamp, amount, approved, rejected, cancelled);
      this.id = id;
    }*/

    Award toAward() {
      return new Award(employee, timestamp, amount, approved, rejected, cancelled);
    }

    static AwardEntity newOne(final Award award) {
      Preconditions.checkState(award.employee() instanceof UserEntity,
          "There is no sufficient type information to save %s.", award);
      return new AwardEntity(
          null,
          (UserEntity) award.employee(),
          award.when(),
          award.amount(),
          award.approved(),
          award.rejected(),
          award.cancelled());
    }

    static AwardEntity existedOne(final int id, final Award award) {
      Preconditions.checkState(award.employee() instanceof UserEntity,
          "There is no sufficient type information to save %s.", award);
      return new AwardEntity(
          id,
          (UserEntity) award.employee(),
          award.when(),
          award.amount(),
          award.approved(),
          award.rejected(),
          award.cancelled());
    }
  }
}
