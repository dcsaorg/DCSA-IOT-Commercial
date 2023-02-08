package org.dcsa.iot.commercial.service.mapping.transferobject;

import org.dcsa.iot.commercial.service.domain.IoTCommercialDomainEvent;
import org.dcsa.iot.commercial.transferobjects.EventMetadataTO;
import org.dcsa.iot.commercial.transferobjects.EventPayloadTO;
import org.dcsa.iot.commercial.transferobjects.IoTCommercialEventTO;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
      LocationTOMapper.class,
    })
public interface IoTCommercialEventTOMapper {

  default IoTCommercialEventTO toDTO(IoTCommercialDomainEvent event) {
    return IoTCommercialEventTO.builder()
        .metadata(toMetadataTO(event))
        .payload(toPayloadTO(event))
        .build();
  }

  EventMetadataTO toMetadataTO(IoTCommercialDomainEvent event);

  EventPayloadTO toPayloadTO(IoTCommercialDomainEvent ioTCommercialDomainEvent);
}
