package org.dcsa.iot.commercial.transferobjects;

import jakarta.validation.Valid;
import lombok.Builder;
import org.dcsa.iot.commercial.transferobjects.enums.DocumentReferenceType;

public record DocumentReferenceTO(

  @Valid DocumentReferenceType type,
  String value
) {
  @Builder
  public DocumentReferenceTO {}
}
