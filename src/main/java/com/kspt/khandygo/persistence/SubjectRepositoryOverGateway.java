package com.kspt.khandygo.persistence;

import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.entities.Subject;
import com.kspt.khandygo.core.entities.Subject.SubjectVisitor;
import com.kspt.khandygo.persistence.Entry.SubjectEntry;
import com.kspt.khandygo.persistence.Entry.SubjectEntry.AwardEntry;
import com.kspt.khandygo.persistence.Entry.SubjectEntry.MeetingEntry;
import com.kspt.khandygo.persistence.Entry.SubjectEntry.OutOfOfficeEntry;
import com.kspt.khandygo.persistence.Entry.SubjectEntry.RegularPaymentEntry;
import com.kspt.khandygo.persistence.Entry.SubjectEntry.SpentTimeEntry;
import com.kspt.khandygo.persistence.Entry.SubjectEntry.VocationEntry;

public class SubjectRepositoryOverGateway implements Repository<Subject> {

  private final Gateway<SubjectEntry> gateway;

  private final SubjectVisitor<SubjectEntry> persistenceAudit;

  private SubjectRepositoryOverGateway(
      final Gateway<SubjectEntry> gateway,
      final SubjectVisitor<SubjectEntry> persistenceAudit) {
    this.gateway = gateway;
    this.persistenceAudit = persistenceAudit;
  }

  @Override
  public int add(final Subject s) {
    final SubjectEntry mapped = s.accept(persistenceAudit);
    return gateway.save(mapped).id();
  }

  @Override
  public Subject get(final int id) {
    return gateway.get(id);
  }

  @Override
  public void update(final int id, final Subject s) {
    final Entry mapped = s.accept(persistenceAudit).id(id);
    gateway.update(mapped);
  }

  @Override
  public Subject delete(final int id) {
    final Entry deleted = gateway.get(id).markAsDeleted();
    return gateway.update(deleted);
  }

  public static Repository<Subject> newOne(final Gateway<SubjectEntry> gateway) {
    return new SubjectRepositoryOverGateway(
        gateway,
        SubjectVisitor.from(
            MeetingEntry::newOne,
            OutOfOfficeEntry::newOne,
            SpentTimeEntry::newOne,
            VocationEntry::newOne,
            AwardEntry::newOne,
            RegularPaymentEntry::newOne)
    );
  }
}