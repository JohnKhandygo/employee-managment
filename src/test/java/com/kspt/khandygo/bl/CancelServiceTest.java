package com.kspt.khandygo.bl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
import com.kspt.khandygo.bl.entities.payments.Award;
import com.kspt.khandygo.bl.entities.payments.RegularPayment;
import com.kspt.khandygo.bl.entities.th.Meeting;
import com.kspt.khandygo.bl.entities.th.OutOfOffice;
import com.kspt.khandygo.bl.entities.th.SpentTime;
import com.kspt.khandygo.bl.entities.th.Vocation;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.CancelApi;
import com.kspt.khandygo.core.entities.Approved;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.sys.Message;
import com.kspt.khandygo.core.sys.Messenger;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import java.util.List;

@RunWith(HierarchicalContextRunner.class)
public class CancelServiceTest {

  @Mock
  private Repository<Approved> repository;

  @Mock
  private Messenger messenger;

  private CancelApi api;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    api = new CancelService(repository, messenger);
  }

  public class RepositoryWithoutSearchedKey {

    @Before
    public void setUp() {
      doReturn(false).when(repository).contains(eq(0));
    }

    @Test(expected = IllegalStateException.class)
    public void whenRepositoryDoesNotContainsApproved_ISEThrows() {
      api.cancel(0, mock(Employee.class));
    }
  }

  public class OwnerContext {

    @Mock
    private Approved instance;

    @Mock
    private Employee owner;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);
      doReturn(owner).when(instance).owner();
      doReturn(true).when(repository).contains(eq(0));
      doReturn(instance).when(repository).get(eq(0));
    }

    @Test(expected = IllegalStateException.class)
    public void whenNotAnOwnerTryingToCancel_ISEThrows() {
      api.cancel(0, mock(Employee.class));
    }

    public class NoConcreteContext {

      @Mock
      Approved cancelled;

      @Before
      public void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(cancelled).when(instance).cancel();
        doReturn(cancelled).when(repository).update(eq(cancelled));
      }

      @Test(expected = RuntimeException.class)
      public void whenNoConcreteImplementationProvided_REThrows() {
        api.cancel(0, owner);
      }
    }

    public class EmployeeContext {

      @Mock
      private Employee employee;

      @Mock
      private Employee manager;

      @Mock
      private Employee paymaster;

      @Before
      public void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(owner).when(instance).owner();
        doReturn(true).when(repository).contains(eq(0));
        doReturn(instance).when(repository).get(eq(0));
        doReturn(manager).when(employee).manager();
        doReturn(paymaster).when(employee).paymaster();
      }

      private Message matchingBody(final Approved body) {
        return Mockito.argThat(
            new ArgumentMatcher<Message>() {
              @Override
              public boolean matches(final Object argument) {
                if (argument instanceof Message) {
                  final Message m = (Message) argument;
                  return m.body().equals(body);
                }
                return false;
              }
            });
      }

      public class RegularPaymentContext {
        @Mock
        RegularPayment cancelled;

        @Before
        public void setUp() {
          MockitoAnnotations.initMocks(this);
          doReturn(cancelled).when(instance).cancel();
          doReturn(cancelled).when(repository).update(eq(cancelled));
        }

        @Test
        public void whenNormalFlow_thenAwardUpdatedAndNoMessagesSent() {
          api.cancel(0, owner);
          verify(instance, times(1)).cancel();
          verify(repository, times(1)).update(eq(cancelled));
          verifyZeroInteractions(messenger);
        }
      }

      public class AwardContext {

        @Mock
        Award cancelled;

        @Before
        public void setUp() {
          MockitoAnnotations.initMocks(this);
          doReturn(cancelled).when(instance).cancel();
          doReturn(cancelled).when(repository).update(eq(cancelled));
          doReturn(employee).when(cancelled).employee();
        }

        @Test
        public void whenNormalFlow_thenAwardUpdatedAndMessagesSent() {
          api.cancel(0, owner);
          verify(instance, times(1)).cancel();
          verify(repository, times(1)).update(eq(cancelled));
          final Employee employee = cancelled.employee();
          final List<Employee> recipients = newArrayList(employee.manager(), employee.paymaster());
          verify(messenger, times(1)).send(
              eq(newLinkedHashSet(recipients)),
              matchingBody(cancelled));
          verifyNoMoreInteractions(messenger);
        }
      }

      public class MeetingContext {

        @Mock
        Meeting cancelled;

        @Before
        public void setUp() {
          MockitoAnnotations.initMocks(this);
          doReturn(cancelled).when(instance).cancel();
          doReturn(cancelled).when(repository).update(eq(cancelled));
        }

        @Test
        public void whenNormalFlow_thenAwardUpdatedAndMessagesSent() {
          api.cancel(0, owner);
          verify(instance, times(1)).cancel();
          verify(repository, times(1)).update(eq(cancelled));
          verify(messenger, times(1)).send(
              eq(newLinkedHashSet(cancelled.participants())),
              matchingBody(cancelled));
          verifyNoMoreInteractions(messenger);
        }
      }

      public class OutOfOfficeContext {

        @Mock
        OutOfOffice cancelled;

        @Before
        public void setUp() {
          MockitoAnnotations.initMocks(this);
          doReturn(cancelled).when(instance).cancel();
          doReturn(cancelled).when(repository).update(eq(cancelled));
          doReturn(employee).when(cancelled).employee();
        }

        @Test
        public void whenNormalFlow_thenAwardUpdatedAndMessagesSent() {
          api.cancel(0, owner);
          verify(instance, times(1)).cancel();
          verify(repository, times(1)).update(eq(cancelled));
          final Employee employee = cancelled.employee();
          final List<Employee> recipients = newArrayList(employee.manager(), employee.paymaster());
          verify(messenger, times(1)).send(
              eq(newLinkedHashSet(recipients)),
              matchingBody(cancelled));
          verifyNoMoreInteractions(messenger);
        }
      }

      public class SpentTimeContext {

        @Mock
        SpentTime cancelled;

        @Before
        public void setUp() {
          MockitoAnnotations.initMocks(this);
          doReturn(cancelled).when(instance).cancel();
          doReturn(cancelled).when(repository).update(eq(cancelled));
          doReturn(employee).when(cancelled).employee();
        }

        @Test
        public void whenNormalFlow_thenAwardUpdatedAndMessagesSent() {
          api.cancel(0, owner);
          verify(instance, times(1)).cancel();
          verify(repository, times(1)).update(eq(cancelled));
          final Employee employee = cancelled.employee();
          final List<Employee> recipients = newArrayList(employee.manager());
          verify(messenger, times(1)).send(
              eq(newLinkedHashSet(recipients)),
              matchingBody(cancelled));
          verifyNoMoreInteractions(messenger);
        }
      }

      public class VocationContext {

        @Mock
        Vocation cancelled;

        @Before
        public void setUp() {
          MockitoAnnotations.initMocks(this);
          doReturn(cancelled).when(instance).cancel();
          doReturn(cancelled).when(repository).update(eq(cancelled));
          doReturn(employee).when(cancelled).employee();
        }

        @Test
        public void whenNormalFlow_thenAwardUpdatedAndMessagesSent() {
          api.cancel(0, owner);
          verify(instance, times(1)).cancel();
          verify(repository, times(1)).update(eq(cancelled));
          final Employee employee = cancelled.employee();
          final List<Employee> recipients = newArrayList(employee.manager(), employee.paymaster());
          verify(messenger, times(1)).send(
              eq(newLinkedHashSet(recipients)),
              matchingBody(cancelled));
          verifyNoMoreInteractions(messenger);
        }
      }

      /*public class RegularPaymentContext {

        @Mock
        RegularPayment cancelled;

        @Before
        public void setUp() {
          MockitoAnnotations.initMocks(this);
          doReturn(cancelled).when(instance).cancel();
          doReturn(cancelled).when(repository).update(eq(cancelled));
        }

        @Test
        public void whenNormalFlow_thenRegularPaymentUpdatedAndNoMessagesSent() {
          api.cancel(0, owner);
          verify(instance, times(1)).cancel();
          verify(repository, times(1)).update(eq(cancelled));
          final Employee employee = cancelled.employee();
          final List<Employee> recipients = newArrayList(employee.manager(), employee.paymaster());
          verify(messenger, times(1)).send(
              eq(newLinkedHashSet(recipients)),
              matchingBody(cancelled));
          verifyNoMoreInteractions(messenger);
        }
      }*/
    }
  }
}