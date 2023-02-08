package org.dcsa.iot.commercial.service.mapping.transferobject;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.iot.commercial.service.domain.Location;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class LocationTOMapper {

  public LocationTO toDTO(Location location) {
    if (location == null) {
      return null;
    }

    return LocationTO.builder()
        .locationName(location.locationName())
        .latitude(location.latitude())
        .longitude(location.longitude())
        .build();
  }
}
