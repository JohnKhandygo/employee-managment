package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;
import static java.util.Collections.unmodifiableList;
import java.time.Instant;
import java.util.List;

public class Notifier {

  private final Sender sender;

  public Notifier(final Sender sender) {
    this.sender = sender;
  }

  public NewSpecifySubject notify(final List<Employee> recipients) {
    return new NewSpecifySubject(sender, unmodifiableList(recipients));
  }

  /*public NewSpecifySubject notify(Employee recipient) {
    return new NewSpecifySubject(sender, singletonList(recipient));
  }

  public NewSpecifySubject notify(Employee... recipients) {
    return new NewSpecifySubject(sender, unmodifiableList(newArrayList(recipients)));
  }*/

  public static class NewSpecifySubject {

    private final Sender sender;

    private final List<Employee> recipients;

    private NewSpecifySubject(final Sender sender, final List<Employee> recipients) {
      this.sender = sender;
      this.recipients = recipients;
    }

    public SpecifyDoneModification that(final Object subject) {
      return new SpecifyDoneModification(sender, recipients, subject);
    }

    public static class SpecifyDoneModification {

      private final Sender sender;

      private final List<Employee> recipients;

      private final Object subject;

      private SpecifyDoneModification(
          final Sender sender,
          final List<Employee> recipients,
          final Object subject) {
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
      }

      public NewSpecifyAuthor hasBeen(final String doneModification) {
        return new NewSpecifyAuthor(sender, recipients, subject, doneModification);
      }

      public static class NewSpecifyAuthor {

        private final Sender sender;

        private final List<Employee> recipients;

        private final Object subject;

        private final String doneModification;

        private NewSpecifyAuthor(
            final Sender sender, final List<Employee> recipients,
            final Object subject,
            final String doneModification) {
          this.sender = sender;
          this.recipients = recipients;
          this.subject = subject;
          this.doneModification = doneModification;
        }

        public void onBehalfOf(final Employee author) {
          sender.sendToAll(
              new NewNotification(
                  Instant.now().toEpochMilli(),
                  author,
                  subject,
                  doneModification),
              recipients);
        }

        private static class NewNotification {
          private final long origin;

          private final Employee author;

          private final Object subject;

          private final String state;

          private NewNotification(
              final long origin,
              final Employee author,
              final Object subject,
              final String state) {
            this.origin = origin;
            this.author = author;
            this.subject = subject;
            this.state = state;
          }
        }
      }
    }
  }
}
