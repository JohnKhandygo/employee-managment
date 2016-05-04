package com.kspt.khandygo.web.resources;

import com.kspt.khandygo.bl.AuthService;
import com.kspt.khandygo.bl.OutOfOfficesService;
import com.kspt.khandygo.core.entities.Employee;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("/out_of_offices")
@AllArgsConstructor(onConstructor = @__({@Inject}))
public class OutOfOfficesResource {

  private final AuthService authService;

  private final OutOfOfficesService outOfOfficesService;

  @Path("/create")
  @POST
  OutOfOfficeCreated create(
      final @HeaderParam("session_id") String session,
      final @QueryParam("when") long when,
      final @QueryParam("duration") long duration,
      final @QueryParam("reason") String reason,
      final @QueryParam("employee_id") int employeeId) {
    final Employee requester = authService.bySession(session);
    final int id = outOfOfficesService.create(requester, when, duration, reason, employeeId);
    return new OutOfOfficeCreated(id);
  }

  @Path("/update/{out_of_office_id}")
  @POST
  OutOfOfficeUpdated cancel(
      final @HeaderParam("session_id") String session,
      final @PathParam("out_of_office_id") int outOfOfficeId,
      final @QueryParam("when") long when,
      final @QueryParam("duration") long duration,
      final @QueryParam("reason") String reason) {
    final Employee requester = authService.bySession(session);
    outOfOfficesService.update(requester, outOfOfficeId, when, duration, reason);
    return new OutOfOfficeUpdated();
  }

  @Path("/cancel/{out_of_office_id}")
  @POST
  OutOfOfficeCancelled cancel(
      final @HeaderParam("session_id") String session,
      final @PathParam("out_of_office_id") int outOfOfficeId) {
    final Employee requester = authService.bySession(session);
    outOfOfficesService.cancel(requester, outOfOfficeId);
    return new OutOfOfficeCancelled();
  }

  @AllArgsConstructor
  private static class OutOfOfficeCreated {
    private final int id;
  }

  private static class OutOfOfficeUpdated {}

  private static class OutOfOfficeCancelled {}
}
