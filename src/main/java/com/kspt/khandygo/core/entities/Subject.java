package com.kspt.khandygo.core.entities;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.Subject.Proposal.Award;
import com.kspt.khandygo.core.entities.Subject.Proposal.Vocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.function.Function;

public interface Subject {
  Employee employee();

  <T> T accept(final SubjectVisitor<T> visitor);

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

  interface SubjectVisitor<T> {

    T visit(final OutOfOffice subject);

    T visit(final Vocation subject);

    T visit(final Award subject);

    static <T> SubjectVisitor<T> from(
        final Function<OutOfOffice, T> onOutOfOffice,
        final Function<Vocation, T> onVocation,
        final Function<Award, T> onAward) {
      return new SubjectVisitor<T>() {

        @Override
        public T visit(final OutOfOffice subject) {
          return onOutOfOffice.apply(subject);
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

  interface Proposal extends Subject {

    <T> T accept(final ProposableSubjectVisitor<T> visitor);

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

      private final long when;

      private final long amount;

      private final Employee employee;

      private final boolean approved;

      private final boolean rejected;

      public Award approve() {
        Preconditions.checkState(!approved);
        Preconditions.checkState(!rejected);
        return new Award(when, amount(), employee(), true, false);
      }

      public Award reject() {
        Preconditions.checkState(!approved);
        Preconditions.checkState(!rejected);
        return new Award(when, amount(), employee(), false, true);
      }

      @Override
      public <T> T accept(final ProposableSubjectVisitor<T> visitor) {
        return visitor.visit(this);
      }

      @Override
      public <T> T accept(final SubjectVisitor<T> visitor) {
        return visitor.visit(this);
      }
    }

    interface ProposableSubjectVisitor<T> {

      T visit(final Vocation proposable);

      T visit(final Award proposable);

      static <T> ProposableSubjectVisitor<T> from(

          final Function<Vocation, T> onVocation,
          final Function<Award, T> onAward) {
        return new ProposableSubjectVisitor<T>() {

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
  }
}