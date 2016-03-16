package com.kspt.khandygo.utils;

import java.time.Instant;

public class TimeUtils {

  public static long currentUTCMs() {
    return Instant.now().toEpochMilli();
  }
}
