package com.kspt.khandygo.core;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject;
import java.util.List;
import java.util.function.BiFunction;

public class Notifier {

  private final NotifyStation notifyStation;

  public Notifier(final NotifyStation notifyStation) {
    this.notifyStation = notifyStation;
  }

  public SpecifyReason notifyThat(final Subject subject) {
    return new SpecifyReason(subject);
  }

  public class SpecifyReason {

    private final Subject subject;

    SpecifyReason(final Subject subject) {
      this.subject = subject;
    }

    public SpecifyAuthor hasBeenProposed() {
      return null;
    }

    public SpecifyAuthor hasBeenApproved() {
      return null;
    }

    public SpecifyAuthor hasBeenRejected() {
      return null;
    }

    public SpecifyAuthor hasBeenCreated() {
      return null;
    }

    public SpecifyAuthor hasBeenUpdated() {
      return null;
    }

    public SpecifyAuthor hasBeenDeleted() {
      return null;
    }

    public class SpecifyAuthor {

      private final BiFunction<Subject, Employee, Notification> creator;

      SpecifyAuthor(final BiFunction<Subject, Employee, Notification> creator) {
        this.creator = creator;
      }

      public void onBehalfOf(final Employee author) {
        final Notification notification = new Notification(subject, author);
        sender.notifyAll(recipients, notification);
      }
    }
  }
}

abstract class NotificationSender {

  void notifyAll(final List<Employee> recipients, final Notification notification) {
    recipients.stream().forEach(r -> notify(r, notification));
  }

  protected abstract void notify(final Employee recipient, final Notification notification);
}

class Notification {

  private final Subject subject;

  private final Employee author;

  Notification(final Subject subject, final Employee author) {
    this.subject = subject;
    this.author = author;
  }

  public Subject subject() {
    return subject;
  }

  public Employee author() {
    return author;
  }

  public static class ProposeCreated extends Notification {
    ProposeCreated(
        final Subject subject,
        final Employee author) {
      super(subject, author);
    }
  }

  public static class ProposeUpdated extends Notification {
    ProposeUpdated(
        final Subject subject,
        final Employee author) {
      super(subject, author);
    }
  }

  public static class ProposeApproved extends Notification {
    ProposeApproved(
        final Subject subject,
        final Employee author) {
      super(subject, author);
    }
  }

  public static class ProposeRejected extends Notification {
    ProposeRejected(
        final Subject subject,
        final Employee author) {
      super(subject, author);
    }
  }

  public static class ProposeCancelled extends Notification {
    ProposeCancelled(
        final Subject subject,
        final Employee author) {
      super(subject, author);
    }
  }

  public static class ApprovedCreated extends Notification {
    ApprovedCreated(
        final Subject subject,
        final Employee author) {
      super(subject, author);
    }
  }

  public static class ApprovedUpdated extends Notification {
    ApprovedUpdated(
        final Subject subject,
        final Employee author) {
      super(subject, author);
    }
  }

  public static class ApprovedCancelled extends Notification {
    ApprovedCancelled(
        final Subject subject,
        final Employee author) {
      super(subject, author);
    }
  }
}
