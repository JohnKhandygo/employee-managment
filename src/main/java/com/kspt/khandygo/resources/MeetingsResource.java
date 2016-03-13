package com.kspt.khandygo.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/1.0/meetings")
public interface MeetingsResource {

  @GET
  @Path("/{id}")
  MeetingView get(final @PathParam("id") int id);

  @POST
  @Path("/{id}/accept")
  Response accept(
      final @PathParam("id") int id,
      final @QueryParam("employee_id") int employee);

  @POST
  @Path("/{id}/decline")
  Response decline(
      final @PathParam("id") int id,
      final @QueryParam("employee_id") int employee,
      final @QueryParam("reason") String reason);

  @POST
  int propose(final @QueryParam("meeting_view") MeetingView mv);

  @DELETE
  @Path("/{id}/cancel")
  Response cancel(final @PathParam("id") int id);

  @POST
  @Path("/{id}/invite")
  Response invite(
      final @PathParam("id") int id,
      final @QueryParam("employee_id") int employee);
}

class MeetingView {
}
