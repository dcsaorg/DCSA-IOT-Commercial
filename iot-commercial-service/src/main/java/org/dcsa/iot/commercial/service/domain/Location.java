package org.dcsa.iot.commercial.service.domain;

import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

public record Location(
    @Size(max = 100) String locationName,
    @Size(max = 10) String latitude,
    @Size(max = 11) String longitude
) {
  @Builder(toBuilder = true)
  public Location {}
}
