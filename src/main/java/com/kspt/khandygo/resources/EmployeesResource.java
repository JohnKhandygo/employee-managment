package com.kspt.khandygo.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/1.0/employees")
public interface EmployeesResource {

  @GET
  @Path("/{id}")
  EmployeeView get(final @PathParam("id") int id);

  @POST
  int hire(final @QueryParam("employee_view") EmployeeView ev);

  @DELETE
  @Path("/{id}/fire")
  Response fire(final @PathParam("id") int id);

  @POST
  @Path("/{id}/update")
  Response update(
      final @PathParam("id") int id,
      final @QueryParam("employee_view") EmployeeView ev);

  @POST
  @Path("/{id}/track")
  Response track(final @PathParam("id") int id, final @QueryParam("amount") long amount);

  @POST
  @Path("/{id}/award")
  Response award(final @PathParam("id") int id, final @QueryParam("amount") long amount);
}

class EmployeeView {
}