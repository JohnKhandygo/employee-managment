/*
package com.kspt.khandygo.resources;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.BiMap;
import com.kspt.khandygo.core.apis.Notifier;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject.Proposal.Award;
import com.kspt.khandygo.persistence.Gateway;
import com.kspt.khandygo.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/awards")
@AllArgsConstructor
public class AwardResource {

  private final EmployeesRegistry employeesRegistry;

  private final AwardService service;

  @Path("/propose")
  @POST
  AwardProposed propose(
      final @HeaderParam("session_id") String session,
      final @QueryParam("when") long when,
      final @QueryParam("amount") long amount,
      final @QueryParam("employee_id") int employeeId) {
    final Employee requester = employeesRegistry.get(session);
    final CreateAwardRequest awardRequest = new CreateAwardRequest(when, amount, employeeId, requester);
    final int processed = service.process(awardRequest);
    return new AwardProposed(processed);
  }

  @Path("/approve")
  @POST
  AwardAccepted approve(
      final @HeaderParam("session_id") String session,
      final @QueryParam("award_id") int id) {
    final Employee requester = employeesRegistry.get(session);
    service.process(new AcceptAwardRequest(id, requester));
    return new AwardAccepted();
  }

  @Path("/reject")
  @POST
  AwardRejected reject(
      final @HeaderParam("session_id") String session,
      final @QueryParam("award_id") int id) {
    final Employee requester = employeesRegistry.get(session);
    service.process(new RejectAwardRequest(id, requester));
    return new AwardRejected();
  }

  @Path("/cancel")
  @POST
  AwardCancelled cancel(
      final @HeaderParam("session_id") String session,
      final @QueryParam("award_id") int id) {
    final Employee requester = employeesRegistry.get(session);
    service.process(new CancelAwardRequest(id, requester));
    return new AwardCancelled();
  }

  @Path("/update")
  @POST
  AwardCancelled update(
      final @HeaderParam("session_id") String session,
      final @QueryParam("award_id") int id,
      final @QueryParam("when") long when,
      final @QueryParam("amount") long amount,
      final @QueryParam("employee_id") int employeeId) {
    final Employee requester = employeesRegistry.get(session);
    service.process(new CancelAwardRequest(id, requester));
    return new AwardCancelled();
  }

  @AllArgsConstructor
  private static class AwardProposed {
    private final int id;
  }

  private static class AwardAccepted {

  }

  private static class AwardRejected {

  }

  private static class AwardCancelled {

  }
}

@AllArgsConstructor
class EmployeesRegistry {
  private final BiMap<String, Employee> employees;

  @NonNull
  public Employee get(final String session) {
    return employees.get(session);
  }
}

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
@ToString
class CreateAwardRequest {
  private final long when;

  private final long amount;

  private final int employeeId;

  private final Employee requester;
}

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
@ToString
class AcceptAwardRequest {
  private final int id;

  private final Employee requester;
}

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
@ToString
class RejectAwardRequest {
  private final int id;

  private final Employee requester;
}

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
@ToString
class CancelAwardRequest {
  private final int id;

  private final Employee requester;
}

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
@ToString
class UpdateAwardRequest {
  private final int id;

  private final long when;

  private final long amount;

  private final Employee requester;
}

@AllArgsConstructor
class AwardsDao {
  private final Gateway gateway;

  public int save(final @NonNull Award award) {
    return gateway.save(AwardEntity.newFrom(award)).id;
  }

  @NonNull
  public Award get(final int id) {
    final AwardEntity awardEntity = gateway.find(AwardEntity.class).where().eq("id", id).unique();
    Preconditions.checkState(!awardEntity.deleted);
    return awardEntity;
  }

  @NonNull
  public Award update(final int id, final @NonNull Award award) {
    return gateway.update(AwardEntity.existedFrom(id, award));
  }

  public void delete(final int id) {
    final Award found = get(id);
    final AwardEntity awardEntity = AwardEntity.deletedFrom(id, found);
    update(id, awardEntity);
  }

  @Entity
  @Table(name = "awards")
  private static class AwardEntity extends Award {
    @Id
    private final Integer id;

    private final boolean deleted;

    public AwardEntity(
        final long when,
        final long amount,
        final Employee employee,
        final boolean accepted,
        final boolean rejected,
        final Integer id, final boolean deleted) {
      super(when, amount, employee, accepted, rejected);
      this.id = id;
      this.deleted = deleted;
    }

    private static AwardEntity deletedFrom(final int id, final Award award) {
      return new AwardEntity(
          award.when(), award.amount(), award.employee(), false, false, id, true);
    }

    private static AwardEntity existedFrom(final int id, final Award award) {
      return new AwardEntity(
          award.when(), award.amount(), award.employee(), award.approved(), award.rejected(),
          id, false);
    }

    private static AwardEntity newFrom(final Award award) {
      return new AwardEntity(
          award.when(), award.amount(), award.employee(), false, false, null, true);
    }
  }
}

@AllArgsConstructor
class EmployeesDao {
  private final Gateway gateway;

  @NonNull
  public Employee get(final int id) {
    return gateway.find(Employee.class).where().eq("id", id).unique();
  }
}

@AllArgsConstructor
class AwardService {

  private final EmployeesDao employeesDao;

  private final AwardsDao awardsDao;

  private final Notifier notifier;

  public int process(final @NonNull CreateAwardRequest request) {
    Preconditions.checkState(request.when() > TimeUtils.currentUTCMs());
    Preconditions.checkState(request.amount() > 0);
    final Award award = createAward(request);
    final int id = awardsDao.save(award);
    notifier.notify(award.employee().paymaster())
        .that(award)
        .hasBeen("proposed")
        .onBehalfOf(request.requester());
    return id;
  }

  @NonNull
  private Award createAward(final @NonNull CreateAwardRequest request) {
    final Employee employee = employeesDao.get(request.employeeId());
    return new Award(request.when(), request.amount(), employee, false, false);
  }

  public void process(final @NonNull AcceptAwardRequest request) {
    final Award award = awardsDao.get(request.id());
    final Award approved = award.approve();
    final Award updated = awardsDao.update(request.id(), approved);
    Verify.verify(updated.approved() && !updated.rejected());
    notifier.notify(award.employee().manager(), award.employee())
        .that(award)
        .hasBeen("approved")
        .onBehalfOf(request.requester());
  }

  public void process(final @NonNull RejectAwardRequest request) {
    final Award award = awardsDao.get(request.id());
    final Award rejected = award.reject();
    final Award updated = awardsDao.update(request.id(), rejected);
    Verify.verify(!updated.approved() && updated.rejected());
    notifier.notify(award.employee().manager())
        .that(award)
        .hasBeen("rejected")
        .onBehalfOf(request.requester());
  }

  public void process(final @NonNull CancelAwardRequest request) {
    final Award award = awardsDao.get(request.id());
    Preconditions.checkState(!award.approved() && !award.rejected());
    Preconditions.checkState(request.requester().equals(award.employee().manager()));
    awardsDao.delete(request.id());
    notifier.notify(award.employee().paymaster())
        .that(award)
        .hasBeen("deleted")
        .onBehalfOf(request.requester());
  }

  public void process(final @NonNull UpdateAwardRequest request) {
    final Award award = awardsDao.get(request.id());
    Preconditions.checkState(!award.approved() && !award.rejected());
    Preconditions.checkState(request.requester().equals(award.employee().manager()));
    Preconditions.checkState(request.when() > TimeUtils.currentUTCMs());
    Preconditions.checkState(request.amount() > 0);
    final Award changed = new Award(
        request.when(), request.amount(), award.employee(), award.approved(), award.rejected());
    awardsDao.update(request.id(), changed);
    notifier.notify(award.employee().paymaster())
        .that(changed)
        .hasBeen("updated")
        .onBehalfOf(request.requester());
  }
}*/
