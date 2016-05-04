package com.kspt.khandygo.web.resources;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kspt.khandygo.bl.AuthService;
import com.kspt.khandygo.bl.OutOfOfficesService;
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

@Path("/out_of_offices")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor(onConstructor = @__({@Inject}))
@Api(value = "/out_of_offices")
public class OutOfOfficesResource {

  private final AuthService authService;

  private final OutOfOfficesService outOfOfficesService;

  @Path("/create")
  @POST
  @ApiOperation(value = "create out of office.")
  public OutOfOfficeCreated create(
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
  @ApiOperation(value = "update out of office.")
  public OutOfOfficeUpdated cancel(
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
  @ApiOperation(value = "cancel out of office.")
  public OutOfOfficeCancelled cancel(
      final @HeaderParam("session_id") String session,
      final @PathParam("out_of_office_id") int outOfOfficeId) {
    final Employee requester = authService.bySession(session);
    outOfOfficesService.cancel(requester, outOfOfficeId);
    return new OutOfOfficeCancelled();
  }

  @ResourceRepresentationWithType
  @JsonTypeName(".out_of_office_created")
  @AllArgsConstructor
  private static class OutOfOfficeCreated {
    private final int id;
  }

  @ResourceRepresentationWithType
  @JsonTypeName(".out_of_office_updated")
  private static class OutOfOfficeUpdated {}

  @ResourceRepresentationWithType
  @JsonTypeName(".out_of_office_cancelled")
  private static class OutOfOfficeCancelled {}
}
