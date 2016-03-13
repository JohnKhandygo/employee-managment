package com.kspt.khandygo.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/1.0/payments")
public interface PaymentsResource {

  @GET
  @Path("/{id}")
  PaymentPlanView get(final @PathParam("id") int id);

  @GET
  List<PaymentPlanView> get();

  @POST
  int approve(final @QueryParam("payment_plan_view") PaymentPlanView ppv);

  @POST
  @Path("/{id}/change")
  Response change(
      final @PathParam("id") int id,
      final @QueryParam("payment_plan_view") PaymentPlanView ppv);
}

class PaymentPlanView {
}