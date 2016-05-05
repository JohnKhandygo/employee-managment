package com.kspt.khandygo.web.resources;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kspt.khandygo.bl.AuthService;
import com.kspt.khandygo.bl.AwardsService;
import com.kspt.khandygo.core.entities.Employee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/awards")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor(onConstructor = @__({@Inject}))
@Api(value = "/awards")
public class AwardsResource {

  private final AuthService authService;

  private final AwardsService awardsService;

  @Path("/propose")
  @POST
  @ApiOperation(value = "propose award.")
  public AwardProposed propose(
      final @HeaderParam("session_id") String session,
      final @QueryParam("when") long when,
      final @QueryParam("amount") long amount,
      final @QueryParam("employee_id") int employeeId) {
    final Employee requester = authService.bySession(session);
    final int id = awardsService.propose(requester, when, amount, employeeId);
    return new AwardProposed(id);
  }

  @Path("/{award_id}/approve")
  @POST
  @ApiOperation(value = "approve award.")
  public AwardApproved approve(
      final @HeaderParam("session_id") String session,
      final @PathParam("award_id") int awardId) {
    final Employee requester = authService.bySession(session);
    awardsService.approve(requester, awardId);
    return new AwardApproved();
  }

  @Path("/{award_id}/reject")
  @POST
  @ApiOperation(value = "reject award.")
  public AwardRejected reject(
      final @HeaderParam("session_id") String session,
      final @PathParam("award_id") int awardId) {
    final Employee requester = authService.bySession(session);
    awardsService.reject(requester, awardId);
    return new AwardRejected();
  }

  @Path("/{award_id}/cancel")
  @POST
  @ApiOperation(value = "cancel award.")
  public AwardCancelled cancel(
      final @HeaderParam("session_id") String session,
      final @PathParam("award_id") int awardId) {
    final Employee requester = authService.bySession(session);
    awardsService.cancel(requester, awardId);
    return new AwardCancelled();
  }

  @ResourceRepresentationWithType
  @JsonTypeName(".award_proposed")
  @AllArgsConstructor
  private static class AwardProposed {
    private final int id;
  }

  @ResourceRepresentationWithType
  @JsonTypeName(".award_approved")
  private static class AwardApproved {}

  @ResourceRepresentationWithType
  @JsonTypeName(".award_rejected")
  private static class AwardRejected {}

  @ResourceRepresentationWithType
  @JsonTypeName(".award_cancelled")
  private static class AwardCancelled {}
}
