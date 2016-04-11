package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.entities.Subject.Proposal.Award;
import com.kspt.khandygo.core.entities.Subject.Proposal.Meeting;
import com.kspt.khandygo.core.entities.Subject.Proposal.Vocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.List;
import java.util.function.Function;

public interface Subject {
  Employee employee();

  <T> T accept(final SubjectVisitor<T> visitor);

  interface SubjectVisitor<T> {
    T visit(final Meeting subject);

    T visit(final OutOfOffice subject);

    T visit(final SpentTime subject);

    T visit(final Vocation subject);

    T visit(final Award subject);

    T visit(final RegularPayment subject);

    static <T> SubjectVisitor<T> from(
        final Function<Meeting, T> onMeeting,
        final Function<OutOfOffice, T> onOutOfOffice,
        final Function<SpentTime, T> onSpentTime,
        final Function<Vocation, T> onVocation,
        final Function<Award, T> onAward,
        final Function<RegularPayment, T> onRegularPayment) {
      return new SubjectVisitor<T>() {
        @Override
        public T visit(final Meeting subject) {
          return onMeeting.apply(subject);
        }

        @Override
        public T visit(final OutOfOffice subject) {
          return onOutOfOffice.apply(subject);
        }

        @Override
        public T visit(final SpentTime subject) {
          return onSpentTime.apply(subject);
        }

        @Override
        public T visit(final Vocation subject) {
          return onVocation.apply(subject);
        }

        @Override
        public T visit(final Award subject) {
          return onAward.apply(subject);
        }

        @Override
        public T visit(final RegularPayment subject) {
          return onRegularPayment.apply(subject);
        }
      };
    }
  }

  interface Proposal extends Subject {

    <T> T accept(final ProposableSubjectVisitor<T> visitor);

    interface ProposableSubjectVisitor<T> {
      T visit(final Meeting proposable);

      T visit(final Vocation proposable);

      T visit(final Award proposable);

      static <T> ProposableSubjectVisitor<T> from(
          final Function<Meeting, T> onMeeting,
          final Function<Vocation, T> onVocation,
          final Function<Award, T> onAward) {
        return new ProposableSubjectVisitor<T>() {
          @Override
          public T visit(final Meeting subject) {
            return onMeeting.apply(subject);
          }

          @Override
          public T visit(final Vocation subject) {
            return onVocation.apply(subject);
          }

          @Override
          public T visit(final Award subject) {
            return onAward.apply(subject);
          }
        };
      }
    }

    @AllArgsConstructor
    @Accessors(fluent = true)
    @Getter
    class Meeting implements Proposal {

      private final long origin;

      private final long when;

      private final long duration;

      private final String where;

      private final String description;

      private final Employee author;

      private final List<Employee> participants;

      private final Employee employee;

      @Override
      public <T> T accept(final ProposableSubjectVisitor<T> visitor) {
        return visitor.visit(this);
      }

      @Override
      public <T> T accept(final SubjectVisitor<T> visitor) {
        return visitor.visit(this);
      }
    }

    @AllArgsConstructor
    @Accessors(fluent = true)
    @Getter
    class Vocation implements Proposal {
      private final long origin;

      private final long when;

      private final long duration;

      private final Employee employee;

      @Override
      public <T> T accept(final ProposableSubjectVisitor<T> visitor) {
        return visitor.visit(this);
      }

      @Override
      public <T> T accept(final SubjectVisitor<T> visitor) {
        return visitor.visit(this);
      }
    }

    @AllArgsConstructor
    @Accessors(fluent = true)
    @Getter
    class Award implements Proposal {
      private final long origin;

      private final long when;

      private final long amount;

      private final Employee employee;

      @Override
      public <T> T accept(final ProposableSubjectVisitor<T> visitor) {
        return visitor.visit(this);
      }

      @Override
      public <T> T accept(final SubjectVisitor<T> visitor) {
        return visitor.visit(this);
      }
    }
  }

  @AllArgsConstructor
  @Accessors(fluent = true)
  @Getter
  class OutOfOffice implements Subject {
    private final long origin;

    private final long when;

    private final long duration;

    private final String reason;

    private final Employee employee;

    @Override
    public <T> T accept(final SubjectVisitor<T> visitor) {
      return visitor.visit(this);
    }
  }

  @AllArgsConstructor
  @Accessors(fluent = true)
  @Getter
  class SpentTime implements Subject {
    private final long origin;

    private final long when;

    private final long duration;

    private final String description;

    private final Employee employee;

    @Override
    public <T> T accept(final SubjectVisitor<T> visitor) {
      return visitor.visit(this);
    }
  }

  @AllArgsConstructor
  @Accessors(fluent = true)
  @Getter
  class RegularPayment implements Subject {
    private final long origin;

    private final long when;

    private final long amount;

    private final Employee employee;

    @Override
    public <T> T accept(final SubjectVisitor<T> visitor) {
      return visitor.visit(this);
    }
  }
}