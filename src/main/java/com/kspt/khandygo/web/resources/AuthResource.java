package com.kspt.khandygo.web.resources;

import com.kspt.khandygo.bl.AuthService;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/auth")
@AllArgsConstructor(onConstructor = @__({@Inject}))
public class AuthResource {

  private final AuthService authService;

  @Path("/")
  @POST
  UserAuthorized authorize(final String login, final String password) {
    final String session = authService.auth(login, password);
    return new UserAuthorized(session);
  }

  @AllArgsConstructor
  private static class UserAuthorized {
    private final String session;
  }
}
