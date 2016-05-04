package com.kspt.khandygo.persistence.dao;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.persistence.Finder;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@AllArgsConstructor(onConstructor = @__({@Inject}))
@Singleton
public class AuthDAO {

  private final Finder finder;

  public Employee get(final String login, final String password) {
    return finder.find(UserEntity.class).where()
        .eq("login", login)
        .and()
        .eq("password", hash(password))
        .unique();
  }

  private String hash(final String string) {
    final MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    md.update(string.getBytes());
    byte bytes[] = md.digest();
    StringBuffer sb = new StringBuffer();
    for (final byte byteValue : bytes) {
      sb.append(Integer.toString((byteValue & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  @Entity
  @Table(name = "users")
  private static class UserEntity extends Employee {
    @Id
    private final Integer id;

    private final String login;

    /*private final String password;*/

    public UserEntity(
        final String name,
        final Employee manager,
        final Employee paymaster,
        final Integer id,
        final String login,
        final String password) {
      super(name, manager, paymaster);
      this.id = id;
      this.login = login;
      /*this.password = password;*/
    }
  }
}
