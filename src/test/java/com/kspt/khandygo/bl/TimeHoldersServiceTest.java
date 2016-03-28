package com.kspt.khandygo.bl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
import com.kspt.khandygo.bl.entities.th.Meeting;
import com.kspt.khandygo.bl.entities.th.OutOfOffice;
import com.kspt.khandygo.bl.entities.th.SpentTime;
import com.kspt.khandygo.bl.entities.th.Vocation;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.TimeHoldersApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.approved.TimeHolder;
import com.kspt.khandygo.core.sys.Message;
import com.kspt.khandygo.core.sys.Messenger;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

@RunWith(HierarchicalContextRunner.class)
public class TimeHoldersServiceTest {

  @Mock
  private Repository<TimeHolder> repository;

  @Mock
  private Messenger messenger;

  private TimeHoldersApi api;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    api = new TimeHoldersService(repository, messenger);
  }

  private Message matchingAuthorAndBody(Employee author, final TimeHolder body) {
    return Mockito.argThat(
        new ArgumentMatcher<Message>() {
          @Override
          public boolean matches(final Object argument) {
            if (argument instanceof Message) {
              final Message m = (Message) argument;
              return m.body().equals(body) && m.author().equals(author);
            }
            return false;
          }
        });
  }

  public class TimeHolderContext {
    @Mock
    private TimeHolder th;

    @Mock
    private Employee employee;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);
      doReturn(th).when(repository).add(eq(th));
      doReturn(employee).when(th).employee();
    }

    @Test(expected = RuntimeException.class)
    public void whenAdd_REThrows() {
      api.add(th);
    }
  }

  public class MetingContext {
    @Mock
    private Employee employee;

    @Mock
    private Meeting th;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);
      doReturn(employee).when(th).employee();
      doReturn(employee).when(th).employee();
      doReturn(th).when(repository).add(eq(th));
    }

    @Test
    public void whenAdd_TimeHolderAddedAndMessagesSent() {
      final TimeHolder added = api.add(th);
      verify(repository, times(1)).add(eq(th));
      verify(messenger, times(1)).send(
          eq(newLinkedHashSet(th.participants())),
          matchingAuthorAndBody(employee, th));
      verifyNoMoreInteractions(repository);
      verifyNoMoreInteractions(messenger);
      assertThat(added).isEqualTo(th);
    }
  }

  public class OutOfOfficeContext {
    @Mock
    private Employee employee;

    @Mock
    private Employee manager;

    @Mock
    private OutOfOffice th;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);
      doReturn(manager).when(employee).manager();
      doReturn(employee).when(th).employee();
      doReturn(employee).when(th).employee();
      doReturn(th).when(repository).add(eq(th));
    }

    @Test
    public void whenAdd_TimeHolderAddedAndMessagesSent() {
      final TimeHolder added = api.add(th);
      verify(repository, times(1)).add(eq(th));
      verify(messenger, times(1)).send(
          eq(newLinkedHashSet(singletonList(manager))),
          matchingAuthorAndBody(employee, th));
      verifyNoMoreInteractions(repository);
      verifyNoMoreInteractions(messenger);
      assertThat(added).isEqualTo(th);
    }
  }

  public class SpentTimeContext {
    @Mock
    private Employee employee;

    @Mock
    private Employee manager;

    @Mock
    private SpentTime th;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);
      doReturn(manager).when(employee).manager();
      doReturn(employee).when(th).employee();
      doReturn(employee).when(th).employee();
      doReturn(th).when(repository).add(eq(th));
    }

    @Test
    public void whenAdd_TimeHolderAddedAndMessagesSent() {
      final TimeHolder added = api.add(th);
      verify(repository, times(1)).add(eq(th));
      verify(messenger, times(1)).send(
          eq(newLinkedHashSet(singletonList(manager))),
          matchingAuthorAndBody(employee, th));
      verifyNoMoreInteractions(repository);
      verifyNoMoreInteractions(messenger);
      assertThat(added).isEqualTo(th);
    }
  }

  public class VocationContext {
    @Mock
    private Employee employee;

    @Mock
    private Employee manager;

    @Mock
    private Employee paymaster;

    @Mock
    private Vocation th;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);
      doReturn(manager).when(employee).manager();
      doReturn(paymaster).when(employee).paymaster();
      doReturn(employee).when(th).employee();
      doReturn(employee).when(th).employee();
      doReturn(th).when(repository).add(eq(th));
    }

    @Test
    public void whenAdd_TimeHolderAddedAndMessagesSent() {
      final TimeHolder added = api.add(th);
      verify(repository, times(1)).add(eq(th));
      verify(messenger, times(1)).send(
          eq(newLinkedHashSet(newArrayList(manager, paymaster))),
          matchingAuthorAndBody(employee, th));
      verifyNoMoreInteractions(repository);
      verifyNoMoreInteractions(messenger);
      assertThat(added).isEqualTo(th);
    }
  }
}