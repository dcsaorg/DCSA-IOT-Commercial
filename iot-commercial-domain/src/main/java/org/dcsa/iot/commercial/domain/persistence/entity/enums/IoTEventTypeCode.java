package org.dcsa.iot.commercial.domain.persistence.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IoTEventTypeCode{
  DETC ("Detected");

  private final String value;
}
