package com.kspt.khandygo.core.apis;

import static com.google.common.collect.Lists.newArrayList;
import com.kspt.khandygo.core.entities.Employee;
import static java.util.Collections.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
public class Notifier {

  private final Sender sender;

  public NewSpecifySubject notify(final List<Employee> recipients) {
    return new NewSpecifySubject(sender, unmodifiableList(recipients));
  }

  public NewSpecifySubject notify(Employee recipient) {
    return new NewSpecifySubject(sender, singletonList(recipient));
  }

  public NewSpecifySubject notify(Employee... recipients) {
    return new NewSpecifySubject(sender, unmodifiableList(newArrayList(recipients)));
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class NewSpecifySubject {

    private final Sender sender;

    private final List<Employee> recipients;

    public SpecifyDoneModification that(final Object subject) {
      return new SpecifyDoneModification(sender, recipients, subject);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SpecifyDoneModification {

      private final Sender sender;

      private final List<Employee> recipients;

      private final Object subject;

      public SpecifyAuthor hasBeen(final String doneModification) {
        return new SpecifyAuthor(sender, recipients, subject, doneModification);
      }

      @AllArgsConstructor(access = AccessLevel.PRIVATE)
      public static class SpecifyAuthor {

        private final Sender sender;

        private final List<Employee> recipients;

        private final Object subject;

        private final String doneModification;

        public void onBehalfOf(final Employee author) {
          sender.sendToAll(
              new NewNotification(
                  Instant.now().toEpochMilli(),
                  author,
                  subject,
                  doneModification),
              recipients);
        }

        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        private static class NewNotification {
          private final long origin;

          private final Employee author;

          private final Object subject;

          private final String state;
        }
      }
    }
  }
}
