package com.kspt.khandygo.core;

import com.kspt.khandygo.bl.entities.subjects.payments.Award;
import com.kspt.khandygo.bl.entities.subjects.payments.RegularPayment;
import com.kspt.khandygo.bl.entities.subjects.th.Meeting;
import com.kspt.khandygo.bl.entities.subjects.th.OutOfOffice;
import com.kspt.khandygo.bl.entities.subjects.th.SpentTime;
import com.kspt.khandygo.bl.entities.subjects.th.Vocation;

public interface SubjectVisitor {

  void visit(final RegularPayment subject);

  void visit(final Award subject);

  void visit(final Meeting subject);

  void visit(final OutOfOffice subject);

  void visit(final SpentTime subject);

  void visit(final Vocation subject);
}
