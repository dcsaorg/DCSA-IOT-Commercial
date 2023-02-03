package org.dcsa.iot.commercial.transferobjects;

import lombok.Builder;

public record IoTCommercialEventTO(
  EventMetadataTO metadata,
  EventPayloadTO payload
) {
  @Builder(toBuilder = true)
  public IoTCommercialEventTO { }
}
