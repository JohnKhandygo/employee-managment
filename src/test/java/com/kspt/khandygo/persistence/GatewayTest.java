package com.kspt.khandygo.persistence;

import lombok.AllArgsConstructor;
import org.junit.Test;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class GatewayTest {

  @Test
  public void justRun()
  throws Exception {
    final SqlServer sqlServer = MysqlServer
        .newOne("127.0.0.1", "3307", "test", "elvis", "crut##7$3uTaG$2rewAcHebAgeje?+8-");
    Gateway shit = new Gateway(new Finder(sqlServer), new Writer(sqlServer));
    final B b = new B(null, "reference b entry");
    final A a = new A(null, "a with reference", b);
    //shit.save(a);
    //final A found = shit.find(A.class).where().eq("id", 1).unique();
    /*final A a2 = new A(1, "just a. updated.", shit.find(B.class).where().eq("id", 9).unique());
    shit.update(a2);*/
    //shit.save(new A(null, "aaaaa"));
    //final A found = shit.find(A.class).where().eq("id", 1).unique();
    //shit.update(new A(1, "bbbb"));
  }

  @Entity
  @Table(name = "a")
  @AllArgsConstructor
  static class A {
    @Id
    private final Integer id;

    private final String a;

    private final B b;
  }

  @Entity
  @Table(name = "b")
  @AllArgsConstructor
  static class B {
    @Id
    private final Integer id;

    private final String b;
  }
}