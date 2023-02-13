package org.dcsa.iot.commercial.transferobjects.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IoTEventTypeCode{
  DETC ("Detected");

  private final String value;
}
