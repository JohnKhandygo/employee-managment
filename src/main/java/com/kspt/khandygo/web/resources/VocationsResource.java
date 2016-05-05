package com.kspt.khandygo.web.resources;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kspt.khandygo.bl.AuthService;
import com.kspt.khandygo.bl.VocationsService;
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

@Path("/vocations")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor(onConstructor = @__({@Inject}))
@Api(value = "/vocations")
public class VocationsResource {

  private final AuthService authService;

  private final VocationsService vocationsService;

  @Path("/propose")
  @POST
  @ApiOperation(value = "propose vocation.")
  public VocationProposed propose(
      final @HeaderParam("session_id") String session,
      final @QueryParam("when") long when,
      final @QueryParam("duration") long duration,
      final @QueryParam("employee_id") int employeeId) {
    final Employee requester = authService.employeeBySession(session);
    final int id = vocationsService.propose(requester, when, duration, employeeId);
    return new VocationProposed(id);
  }

  @Path("/{vocation_id}/approve")
  @POST
  @ApiOperation(value = "approve vocation.")
  public VocationApproved approve(
      final @HeaderParam("session_id") String session,
      final @PathParam("vocation_id") int vocationId) {
    final Employee requester = authService.employeeBySession(session);
    vocationsService.approve(requester, vocationId);
    return new VocationApproved();
  }

  @Path("/{vocation_id}/reject")
  @POST
  @ApiOperation(value = "reject vocation.")
  public VocationRejected reject(
      final @HeaderParam("session_id") String session,
      final @PathParam("vocation_id") int vocationId) {
    final Employee requester = authService.employeeBySession(session);
    vocationsService.reject(requester, vocationId);
    return new VocationRejected();
  }

  @Path("/{vocation_id}/cancel")
  @POST
  @ApiOperation(value = "cancel vocation.")
  public VocationCancelled cancel(
      final @HeaderParam("session_id") String session,
      final @PathParam("vocation_id") int vocationId) {
    final Employee requester = authService.employeeBySession(session);
    vocationsService.cancel(requester, vocationId);
    return new VocationCancelled();
  }

  @ResourceRepresentationWithType
  @JsonTypeName(".vocation_proposed")
  @AllArgsConstructor
  private static class VocationProposed {
    private final int id;
  }

  @ResourceRepresentationWithType
  @JsonTypeName(".vocation_approved")
  private static class VocationApproved {}

  @ResourceRepresentationWithType
  @JsonTypeName(".vocation_rejected")
  private static class VocationRejected {}

  @ResourceRepresentationWithType
  @JsonTypeName(".vocation_cancelled")
  private static class VocationCancelled {}
}
