package org.dcsa.iot.commercial.domain.persistence.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventClassifierCode {
  ACT("Actual");

  private final String value;
}
