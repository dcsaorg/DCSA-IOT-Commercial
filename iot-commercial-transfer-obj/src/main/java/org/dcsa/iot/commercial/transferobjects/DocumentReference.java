package org.dcsa.iot.commercial.transferobjects;

import lombok.Builder;
import org.dcsa.iot.commercial.transferobjects.enums.DocumentReferenceType;

public record DocumentReference(
  DocumentReferenceType type,
  String value
) {
  @Builder
  public DocumentReference {}
}
