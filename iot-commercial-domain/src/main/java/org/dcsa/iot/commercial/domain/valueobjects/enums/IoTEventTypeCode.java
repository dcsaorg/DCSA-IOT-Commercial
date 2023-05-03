package org.dcsa.iot.commercial.domain.valueobjects.enums;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum IoTEventTypeCode{
  DETC ("DETC");

  private final String description;
}
