package com.kspt.khandygo.bl;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.bl.entities.payments.Award;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.PaymentsApi;
import com.kspt.khandygo.core.apis.ProposalApi;
import com.kspt.khandygo.core.apis.TimeHoldersApi;
import com.kspt.khandygo.core.entities.Approved;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Proposal;
import com.kspt.khandygo.core.entities.approved.TimeHolder;
import com.kspt.khandygo.core.sys.Message;
import com.kspt.khandygo.core.sys.Messenger;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
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

@RunWith(HierarchicalContextRunner.class)
public class ProposalServiceTest {

  @Mock
  private Repository<Proposal> repository;

  @Mock
  private Messenger messenger;

  @Mock
  private TimeHoldersApi thApi;

  @Mock
  private PaymentsApi pApi;

  private ProposalApi api;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    api = new ProposalService(repository, messenger, thApi, pApi);
  }

  private Message matchingAuthorAndBody(Employee author, final Proposal body) {
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

  public class ProposalContext {
    @Mock
    private Proposal proposal;

    @Mock
    private Employee author;

    @Mock
    private Employee recipient;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);
      doReturn(true).when(proposal).pending();
      doReturn(proposal).when(repository).add(eq(proposal));
      doReturn(proposal).when(repository).update(eq(proposal));
      doReturn(author).when(proposal).author();
      doReturn(recipient).when(proposal).recipient();
    }

    public class OutdatedProposalContext {

      @Before
      public void setUp() {
        doReturn(Instant.now().minusSeconds(1).toEpochMilli()).when(proposal).when();
      }

      @Test(expected = IllegalStateException.class)
      public void whenProposalIsOutdated_ISEThrows() {
        api.propose(proposal);
      }
    }

    public class ProposalWithCorrectDate {

      @Before
      public void setUp() {
        doReturn(Instant.now().plusSeconds(10).toEpochMilli()).when(proposal).when();
      }

      @Test
      public void whenPropose_ProposalAddedAndMessageSent() {
        api.propose(proposal);
        verify(repository).add(eq(proposal));
        verify(messenger).send(eq(recipient), matchingAuthorAndBody(author, proposal));
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(messenger);
        verifyZeroInteractions(thApi);
        verifyZeroInteractions(pApi);
      }
    }

    public class MissingProposalContext {

      @Test(expected = NullPointerException.class)
      public void whenTryingToApproveProposalWhichIsNotCommittedToRepository_NPEThrows() {
        api.approve(0, mock(Employee.class));
      }

      @Test(expected = NullPointerException.class)
      public void whenTryingToRejectProposalWhichIsNotCommittedToRepository_NPEThrows() {
        api.reject(0, mock(Employee.class));
      }
    }

    public class ExistingProposalContext {

      @Before
      public void setUp() {
        doReturn(proposal).when(repository).get(eq(0));
        doReturn(proposal).when(proposal).reject();
        doReturn(proposal).when(proposal).approve();
      }

      @Test
      public void whenReject_ProposalUpdatedAndMessageSent() {
        api.reject(0, recipient);
        verify(repository).update(eq(proposal));
        verify(messenger).send(eq(author), matchingAuthorAndBody(recipient, proposal));
        verifyNoMoreInteractions(messenger);
        verifyZeroInteractions(thApi);
        verifyZeroInteractions(pApi);
      }

      @Test(expected = RuntimeException.class)
      public void whenApproveProposalWithputSubject_REThrows() {
        api.approve(0, recipient);
      }

      public class MissingAccessRightsContext {

        @Mock
        private Employee requester;

        @Before
        public void setUp() {
          MockitoAnnotations.initMocks(this);
        }

        @Test(expected = IllegalStateException.class)
        public void whenAccessRightMissedOnApprove_ISEThrows()
        throws Exception {
          Preconditions.checkState(!requester.equals(recipient));
          api.approve(0, requester);
        }

        @Test(expected = IllegalStateException.class)
        public void whenAccessRightMissedOnReject_ISEThrows()
        throws Exception {
          Preconditions.checkState(!requester.equals(recipient));
          api.reject(0, requester);
        }
      }

      public class SubjectContext {

        public class NonConcreteSubjectContext {
          @Mock
          private Approved subject;

          @Before
          public void setUp() {
            MockitoAnnotations.initMocks(this);
            doReturn(subject).when(proposal).subject();
          }

          @Test(expected = RuntimeException.class)
          public void whenApprove_REThrows() {
            api.approve(0, recipient);
          }
        }

        public class TimeHolderSubjectContext {
          @Mock
          private TimeHolder subject;

          @Before
          public void setUp() {
            MockitoAnnotations.initMocks(this);
            doReturn(subject).when(proposal).subject();
          }

          @Test
          public void whenApprove_ProposalUpdatesThenMessageSentThenDelegateToTHApi() {
            api.approve(0, recipient);
            verify(repository, times(1)).update(proposal);
            verify(messenger).send(eq(author), matchingAuthorAndBody(recipient, proposal));
            verify(thApi).reserve(eq(subject));
            verifyNoMoreInteractions(messenger);
            verifyNoMoreInteractions(thApi);
            verifyZeroInteractions(pApi);
          }
        }

        public class AwardSubjectContext {
          @Mock
          private Award subject;

          @Before
          public void setUp() {
            MockitoAnnotations.initMocks(this);
            doReturn(subject).when(proposal).subject();
          }

          @Test
          public void whenApprove_ProposalUpdatesThenMessageSentThenDelegateToPApi() {
            api.approve(0, recipient);
            verify(repository, times(1)).update(proposal);
            verify(messenger).send(eq(author), matchingAuthorAndBody(recipient, proposal));
            verify(pApi).award(eq(subject));
            verifyNoMoreInteractions(messenger);
            verifyNoMoreInteractions(thApi);
            verifyZeroInteractions(thApi);
          }
        }
      }
    }
  }
}