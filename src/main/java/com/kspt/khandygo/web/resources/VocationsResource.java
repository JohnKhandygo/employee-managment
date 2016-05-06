package com.kspt.khandygo.web.resources;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kspt.khandygo.bl.AuthService;
import com.kspt.khandygo.bl.VocationsService;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Vocation;
import com.kspt.khandygo.utils.Tuple2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import static java.util.stream.Collectors.toList;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/vocations")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor(onConstructor = @__({@Inject}))
@Api(value = "/vocations")
public class VocationsResource {

  private final AuthService authService;

  private final VocationsService vocationsService;

  @Path("/approved")
  @GET
  @ApiOperation(value = "get approved vocations for employee.")
  public List<VocationRepresentation> get(final @HeaderParam("session_id") String session) {
    final int requesterId = authService.employeeIdBySession(session);
    final List<Tuple2<Integer, Vocation>> awards = vocationsService.approvedFor(requesterId);
    return represent(awards);
  }

  private List<VocationRepresentation> represent(final List<Tuple2<Integer, Vocation>> awards) {
    return awards.stream()
        .map(t2 -> new VocationRepresentation(t2._1, t2._2.when(), t2._2.duration()))
        .collect(toList());
  }

  @Path("/pending/inbox")
  @GET
  @ApiOperation(value = "get pending vocations awaiting for employee decision.")
  public List<VocationRepresentation> getPendingInbox(
      final @HeaderParam("session_id") String session) {
    final int requesterId = authService.employeeIdBySession(session);
    final List<Tuple2<Integer, Vocation>> awards = vocationsService.pendingInboxFor(requesterId);
    return represent(awards);
  }

  @Path("/pending/outbox")
  @GET
  @ApiOperation(value = "get pending vocations proposed by employee.")
  public List<VocationRepresentation> getPendingOutbox(
      final @HeaderParam("session_id") String session) {
    final int requesterId = authService.employeeIdBySession(session);
    final List<Tuple2<Integer, Vocation>> awards = vocationsService.pendingOutboxFor(requesterId);
    return represent(awards);
  }

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
  @DELETE
  @ApiOperation(value = "cancel vocation.")
  public VocationCancelled cancel(
      final @HeaderParam("session_id") String session,
      final @PathParam("vocation_id") int vocationId) {
    final Employee requester = authService.employeeBySession(session);
    vocationsService.cancel(requester, vocationId);
    return new VocationCancelled();
  }

  @ResourceRepresentationWithType
  @JsonTypeName(".vocation")
  @AllArgsConstructor
  private static class VocationRepresentation {
    private final int id;

    private final long when;

    private final long duration;
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
