package com.kspt.khandygo.web.resources;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kspt.khandygo.bl.AuthService;
import com.kspt.khandygo.bl.EmployeeService;
import com.kspt.khandygo.core.entities.Employee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import static java.util.stream.Collectors.toList;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor(onConstructor = @__({@Inject}))
@Api(value = "/employees")
public class EmployeesResource {

  private final AuthService authService;

  private final EmployeeService employeeService;

  @Path("/manager")
  @GET
  @ApiOperation(value = "get employee's manager.")
  public EmployeeRepresentation getManager(
      final @HeaderParam("session_id") String session) {
    final Employee manager = authService.employeeBySession(session).manager();
    if (manager == null) return new NullEmployeeRepresentation();
    else return new ExistedEmployeeRepresentation(manager.name());
  }

  @Path("/paymaster")
  @GET
  @ApiOperation(value = "get employee's paymaster.")
  public EmployeeRepresentation getPaymaster(
      final @HeaderParam("session_id") String session) {
    final Employee paymaster = authService.employeeBySession(session).paymaster();
    if (paymaster == null) return new NullEmployeeRepresentation();
    else return new ExistedEmployeeRepresentation(paymaster.name());
  }

  @Path("/teammates")
  @GET
  @ApiOperation(value = "get employee's teammates.")
  public List<ExistedEmployeeRepresentation> getTeammates(
      final @HeaderParam("session_id") String session) {
    final Employee employee = authService.employeeBySession(session);
    final Employee manager = employee.manager();
    return employeeService.getAllUnderThePatronageOf(manager)
        .stream()
        .<Employee>map(t2 -> t2._2)
        .filter(e -> !e.equals(employee))
        .map(e -> new ExistedEmployeeRepresentation(e.name()))
        .collect(toList());
  }

  @Path("/patronaged")
  @GET
  @ApiOperation(value = "get employee's teammates.")
  public List<EmployeeWithIdRepresentation> getPatronaged(
      final @HeaderParam("session_id") String session) {
    final Employee employee = authService.employeeBySession(session);
    return employeeService.getAllUnderThePatronageOf(employee)
        .stream()
        .map(t2 -> new EmployeeWithIdRepresentation(t2._2.name(), t2._1))
        .collect(toList());
  }

  /*@ResourceRepresentationWithType
  @JsonTypeName(".employee")
  @AllArgsConstructor*/
  private static class EmployeeRepresentation {

  }

  @ResourceRepresentationWithType
  @JsonTypeName(".employee")
  @AllArgsConstructor
  private static class ExistedEmployeeRepresentation extends EmployeeRepresentation {
    private final String name;
  }

  @ResourceRepresentationWithType
  @JsonTypeName(".employee_with_id")
  private static class EmployeeWithIdRepresentation extends ExistedEmployeeRepresentation {
    private final int id;

    EmployeeWithIdRepresentation(final String name, final int id) {
      super(name);
      this.id = id;
    }
  }

  @ResourceRepresentationWithType
  @JsonTypeName(".null")
  @AllArgsConstructor
  private static class NullEmployeeRepresentation extends EmployeeRepresentation {
  }
}
