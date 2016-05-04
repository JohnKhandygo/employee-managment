package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.entities.Employee;

public interface AuthService {

  Employee bySession(final String session);
}
