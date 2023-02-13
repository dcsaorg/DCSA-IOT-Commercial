package org.dcsa.iot.commercial.transferobjects.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PublisherRole {
  CA("Carrier"),
  AG("Carrier local agent"),
  VSP("Visibility Service Provider"),
  SVP("Other service provider");

  private final String value;
}
