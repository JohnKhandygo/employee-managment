package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.EmployeesApi;
import com.kspt.khandygo.core.entities.Employee;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

@RunWith(HierarchicalContextRunner.class)
public class EmployeesServiceTest {

  @Mock
  private Repository<Employee> repository;

  private EmployeesApi api;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    api = new EmployeesService(repository);
  }

  public class MissingEmployeeContext {

    @Test(expected = NullPointerException.class)
    public void whenGetEmployee_NPEThrows() {
      api.get(0);
    }
  }

  public class ExistedEmployeeContext {
    @Mock
    private Employee employee;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);
      doReturn(employee).when(repository).get(eq(0));
      doReturn(employee).when(repository).update(eq(employee));
    }

    @Test
    public void whenGetCalled_delegateToRepository() {
      final Employee found = api.get(0);
      verify(repository, times(1)).get(eq(0));
      verifyNoMoreInteractions(repository);
      assertThat(found).isEqualTo(employee);
    }

    @Test
    public void whenUpdateCalled_delegateToRepository() {
      final Employee updated = api.update(employee);
      verify(repository, times(1)).update(eq(employee));
      verifyNoMoreInteractions(repository);
      assertThat(updated).isEqualTo(employee);
    }
  }
}