package com.kspt.khandygo.web.resources;

import com.kspt.khandygo.bl.AuthService;
import com.kspt.khandygo.bl.AwardsService;
import com.kspt.khandygo.core.entities.Employee;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("/awards")
@AllArgsConstructor(onConstructor = @__({@Inject}))
public class AwardsResource {

  private final AuthService authService;

  private final AwardsService awardsService;

  @Path("/propose")
  @POST
  AwardProposed propose(
      final @HeaderParam("session_id") String session,
      final @QueryParam("when") long when,
      final @QueryParam("amount") long amount,
      final @QueryParam("employee_id") int employeeId) {
    final Employee requester = authService.bySession(session);
    final int id = awardsService.propose(requester, when, amount, employeeId);
    return new AwardProposed(id);
  }

  @Path("/approve/{award_id}")
  @POST
  AwardApproved approve(
      final @HeaderParam("session_id") String session,
      final @PathParam("award_id") int awardId) {
    final Employee requester = authService.bySession(session);
    awardsService.approve(requester, awardId);
    return new AwardApproved();
  }

  @Path("/reject/{award_id}")
  @POST
  AwardRejected reject(
      final @HeaderParam("session_id") String session,
      final @PathParam("award_id") int awardId) {
    final Employee requester = authService.bySession(session);
    awardsService.reject(requester, awardId);
    return new AwardRejected();
  }

  @Path("/cancel/{award_id}")
  @POST
  AwardCancelled cancel(
      final @HeaderParam("session_id") String session,
      final @PathParam("award_id") int awardId) {
    final Employee requester = authService.bySession(session);
    awardsService.cancel(requester, awardId);
    return new AwardCancelled();
  }

  @AllArgsConstructor
  private static class AwardProposed {
    private final int id;
  }

  private static class AwardApproved {}

  private static class AwardRejected {}

  private static class AwardCancelled {}
}
