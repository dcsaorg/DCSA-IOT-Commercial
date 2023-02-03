package org.dcsa.iot.commercial.transferobjects.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IoTEventCode {
  DRO  ("Door Opened");

  private final String value;
}
