package org.dcsa.iot.commercial.domain.persistence.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IoTEventCode {
  DRO  ("Door Opened");

  private final String value;
}
