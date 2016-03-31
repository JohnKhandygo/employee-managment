package com.kspt.khandygo.persistence.attributes;

import com.avaje.ebean.annotation.EnumValue;
import com.kspt.khandygo.persistence.AttributesEntry;
import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "time_holders")
@Accessors(fluent = true)
@Getter
public class TimeHolderAttributesEntry extends AttributesEntry {

  private final TimeHolderType type;

  private final long duration;

  @Lob
  private final String description;

  public TimeHolderAttributesEntry(
      final Integer id,
      final long when,
      final TimeHolderType type,
      final long duration,
      final String description) {
    super(id, when);
    this.type = type;
    this.duration = duration;
    this.description = description;
  }

  public enum TimeHolderType {
    @EnumValue("M")
    MEETING,

    @EnumValue("O")
    OUT_OF_OFFICE,

    @EnumValue("S")
    SPENT_TIME,

    @EnumValue("V")
    VOCATION
  }
}
