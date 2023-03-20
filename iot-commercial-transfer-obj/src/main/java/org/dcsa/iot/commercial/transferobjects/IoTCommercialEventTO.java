package org.dcsa.iot.commercial.transferobjects;

import jakarta.validation.Valid;
import lombok.Builder;

public record IoTCommercialEventTO(
  @Valid EventMetadataTO metadata,
  @Valid EventPayloadTO payload
) {
  @Builder(toBuilder = true)
  public IoTCommercialEventTO { }
}
