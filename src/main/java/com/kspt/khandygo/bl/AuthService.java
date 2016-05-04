package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.persistence.dao.AuthDAO;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

@AllArgsConstructor(onConstructor = @__({@Inject}))
@Singleton
public class AuthService {

  private final Map<String, Employee> authorizedUsers;

  private final AuthDAO authDAO;

  public Employee bySession(final String session) {
    return authorizedUsers.get(session);
  }

  public String auth(final String login, final String password) {
    final Employee user = authDAO.get(login, password);
    if (authorizedUsers.containsValue(user))
      return authorizedUsers.entrySet().stream()
          .filter(e -> e.getValue().equals(user))
          .map(Entry::getKey)
          .findFirst()
          .get();
    final String session = UUID.randomUUID().toString();
    authorizedUsers.put(session, user);
    return session;
  }
}
