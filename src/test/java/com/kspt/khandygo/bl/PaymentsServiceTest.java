package com.kspt.khandygo.bl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
import com.kspt.khandygo.bl.entities.payments.Award;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.PaymentsApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.approved.Payment;
import com.kspt.khandygo.core.sys.Message;
import com.kspt.khandygo.core.sys.Messenger;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
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
import java.time.Instant;
import java.util.List;

@RunWith(HierarchicalContextRunner.class)
public class PaymentsServiceTest {

  @Mock
  private Award award;

  @Mock
  private Repository<Payment> repository;

  @Mock
  private Messenger messenger;

  private PaymentsApi api;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    api = new PaymentsService(repository, messenger);
  }

  public class OutdatedAward {

    @Before
    public void setUp() {
      doReturn(Instant.now().minusSeconds(1).toEpochMilli()).when(award).when();
    }

    @Test(expected = IllegalStateException.class)
    public void whenAwardIsOutdated_ISEThrows() {
      api.award(award);
    }
  }

  public class EmployeeContext {

    @Mock
    private Award added;

    @Mock
    private Employee employee;

    @Mock
    private Employee manager;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);
      doReturn(Instant.now().plusSeconds(10).toEpochMilli()).when(award).when();
      doReturn(added).when(repository).add(eq(award));
      doReturn(employee).when(added).employee();
      doReturn(manager).when(employee).manager();
    }

    @Test
    public void whenNormalFlow_thenAwardIsAddedAndMessagesSent() {
      final Payment payment = api.award(award);
      verify(repository, times(1)).add(eq(PaymentsServiceTest.this.award));
      final List<Employee> recipients = newArrayList(employee, manager);
      verify(messenger, times(1)).send(
          eq(newLinkedHashSet(recipients)),
          matchingBody(added));
      verifyNoMoreInteractions(repository);
      verifyNoMoreInteractions(messenger);
      assertThat(payment).isEqualTo(added);
    }

    private Message matchingBody(final Award body) {
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
  }
}