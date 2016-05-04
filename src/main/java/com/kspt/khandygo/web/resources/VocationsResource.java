package com.kspt.khandygo.web.resources;

import com.kspt.khandygo.bl.AuthService;
import com.kspt.khandygo.bl.VocationsService;
import com.kspt.khandygo.core.entities.Employee;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("/vocations")
@AllArgsConstructor(onConstructor = @__({@Inject}))
public class VocationsResource {

  private final AuthService authService;

  private final VocationsService vocationsService;

  @Path("/propose")
  @POST
  VocationProposed propose(
      final @HeaderParam("session_id") String session,
      final @QueryParam("when") long when,
      final @QueryParam("duration") long duration,
      final @QueryParam("employee_id") int employeeId) {
    final Employee requester = authService.bySession(session);
    final int id = vocationsService.propose(requester, when, duration, employeeId);
    return new VocationProposed(id);
  }

  @Path("/approve/{vocation_id}")
  @POST
  VocationApproved approve(
      final @HeaderParam("session_id") String session,
      final @PathParam("vocation_id") int vocationId) {
    final Employee requester = authService.bySession(session);
    vocationsService.approve(requester, vocationId);
    return new VocationApproved();
  }

  @Path("/reject/{vocation_id}")
  @POST
  VocationRejected reject(
      final @HeaderParam("session_id") String session,
      final @PathParam("vocation_id") int vocationId) {
    final Employee requester = authService.bySession(session);
    vocationsService.reject(requester, vocationId);
    return new VocationRejected();
  }

  @Path("/cancel/{vocation_id}")
  @POST
  VocationCancelled cancel(
      final @HeaderParam("session_id") String session,
      final @PathParam("vocation_id") int vocationId) {
    final Employee requester = authService.bySession(session);
    vocationsService.cancel(requester, vocationId);
    return new VocationCancelled();
  }

  @AllArgsConstructor
  private static class VocationProposed {
    private final int id;
  }

  private static class VocationApproved {}

  private static class VocationRejected {}

  private static class VocationCancelled {}
}
