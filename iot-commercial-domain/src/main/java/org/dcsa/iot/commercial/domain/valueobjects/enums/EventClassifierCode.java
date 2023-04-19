package org.dcsa.iot.commercial.domain.valueobjects.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventClassifierCode {
  ACT("Actual");

  private final String value;
}
