package com.kspt.khandygo.core;

import static com.google.common.collect.Lists.newArrayList;
import com.kspt.khandygo.bl.entities.subjects.payments.Award;
import com.kspt.khandygo.bl.entities.subjects.payments.RegularPayment;
import com.kspt.khandygo.bl.entities.subjects.th.Meeting;
import com.kspt.khandygo.bl.entities.subjects.th.OutOfOffice;
import com.kspt.khandygo.bl.entities.subjects.th.SpentTime;
import com.kspt.khandygo.bl.entities.subjects.th.Vocation;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject;
import static java.util.Collections.singletonList;
import java.util.List;

public class Notifier {

  private final NotificationSender sender;

  public Notifier(final NotificationSender sender) {
    this.sender = sender;
  }

  public SpecifyReason notifyThat(final Subject subject) {
    return new SpecifyReason(subject);
  }

  public class SpecifyReason {

    private final Subject subject;

    SpecifyReason(final Subject subject) {
      this.subject = subject;
    }

    public SpecifyAuthor hasBeenCreated() {
      final List<Employee> recipients;
      if (subject instanceof RegularPayment) {
        recipients = singletonList(subject.employee());
      } else if (subject instanceof Award) {
        recipients = newArrayList(subject.employee());
      } else if (subject instanceof Meeting) {
        recipients = singletonList(subject.employee());
      } else if (subject instanceof OutOfOffice) {
        recipients = newArrayList(subject.employee().manager());
      } else if (subject instanceof SpentTime) {
        recipients = newArrayList(subject.employee().manager(), subject.employee().paymaster());
      } else if (subject instanceof Vocation) {
        recipients = newArrayList(subject.employee().paymaster());
      } else {
        throw new RuntimeException();
      }
      return new SpecifyAuthor(recipients, "created");
    }

    public SpecifyAuthor hasBeenUpdated() {
      final List<Employee> recipients;
      if (subject instanceof RegularPayment) {
        recipients = singletonList(subject.employee());
      } else if (subject instanceof Award) {
        recipients = newArrayList(subject.employee());
      } else if (subject instanceof Meeting) {
        recipients = singletonList(subject.employee());
      } else if (subject instanceof OutOfOffice) {
        recipients = newArrayList(subject.employee().manager());
      } else if (subject instanceof SpentTime) {
        recipients = newArrayList(subject.employee().manager(), subject.employee().paymaster());
      } else if (subject instanceof Vocation) {
        recipients = newArrayList(subject.employee().paymaster());
      } else {
        throw new RuntimeException();
      }
      return new SpecifyAuthor(recipients, "updated");
    }

    public SpecifyAuthor hasBeenDeleted() {
      final List<Employee> recipients;
      if (subject instanceof RegularPayment) {
        recipients = singletonList(subject.employee());
      } else if (subject instanceof Award) {
        recipients = newArrayList(subject.employee(), subject.employee().manager());
      } else if (subject instanceof Meeting) {
        recipients = singletonList(subject.employee());
      } else if (subject instanceof OutOfOffice) {
        recipients = newArrayList(subject.employee().manager());
      } else if (subject instanceof SpentTime) {
        recipients = newArrayList(subject.employee().manager(), subject.employee().paymaster());
      } else if (subject instanceof Vocation) {
        recipients = newArrayList(subject.employee().manager(), subject.employee().paymaster());
      } else {
        throw new RuntimeException();
      }
      return new SpecifyAuthor(recipients, "deleted");
    }

    public SpecifyAuthor hasBeenProposed() {
      final List<Employee> recipients;
      if (subject instanceof RegularPayment) {
        throw new RuntimeException();
      } else if (subject instanceof Award) {
        recipients = newArrayList(subject.employee().manager());
      } else if (subject instanceof Meeting) {
        recipients = singletonList(subject.employee());
      } else if (subject instanceof OutOfOffice) {
        throw new RuntimeException();
      } else if (subject instanceof SpentTime) {
        throw new RuntimeException();
      } else if (subject instanceof Vocation) {
        recipients = newArrayList(subject.employee().manager());
      } else {
        throw new RuntimeException();
      }
      return new SpecifyAuthor(recipients, "proposed");
    }

    public SpecifyAuthor hasBeenApproved() {
      final List<Employee> recipients;
      if (subject instanceof RegularPayment) {
        throw new RuntimeException();
      } else if (subject instanceof Award) {
        recipients = newArrayList(subject.employee().manager());
      } else if (subject instanceof Meeting) {
        recipients = ((Meeting) subject).participants();
      } else if (subject instanceof OutOfOffice) {
        throw new RuntimeException();
      } else if (subject instanceof SpentTime) {
        throw new RuntimeException();
      } else if (subject instanceof Vocation) {
        recipients = newArrayList(subject.employee());
      } else {
        throw new RuntimeException();
      }
      return new SpecifyAuthor(recipients, "approved");
    }

    public SpecifyAuthor hasBeenRejected() {
      final List<Employee> recipients;
      if (subject instanceof RegularPayment) {
        throw new RuntimeException();
      } else if (subject instanceof Award) {
        recipients = newArrayList(subject.employee().manager());
      } else if (subject instanceof Meeting) {
        recipients = ((Meeting) subject).participants();
      } else if (subject instanceof OutOfOffice) {
        throw new RuntimeException();
      } else if (subject instanceof SpentTime) {
        throw new RuntimeException();
      } else if (subject instanceof Vocation) {
        recipients = newArrayList(subject.employee());
      } else {
        throw new RuntimeException();
      }
      return new SpecifyAuthor(recipients, "rejected");
    }

    public class SpecifyAuthor {
      private final List<Employee> recipients;

      private final String even;

      SpecifyAuthor(final List<Employee> recipients, final String even) {
        this.recipients = recipients;
        this.even = even;
      }

      public void onBehalfOf(final Employee author) {
        final Notification notification = new Notification(even, subject, author);
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
  private final String about;

  private final Subject subject;

  private final Employee author;

  Notification(final String about, final Subject subject, final Employee author) {
    this.about = about;
    this.subject = subject;
    this.author = author;
  }
}
