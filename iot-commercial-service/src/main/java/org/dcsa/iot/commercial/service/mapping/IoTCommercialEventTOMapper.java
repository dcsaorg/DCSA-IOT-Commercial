package org.dcsa.iot.commercial.service.mapping;

import org.dcsa.iot.commercial.domain.valueobjects.EventPayload;
import org.dcsa.iot.commercial.domain.valueobjects.IoTCommercialEvent;
import org.dcsa.iot.commercial.domain.valueobjects.IoTCommercialRetractedEvent;
import org.dcsa.iot.commercial.transferobjects.EventMetadataTO;
import org.dcsa.iot.commercial.transferobjects.EventPayloadTO;
import org.dcsa.iot.commercial.transferobjects.IoTCommercialEventTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {DocumentReferenceMapper.class})
public interface IoTCommercialEventTOMapper {

  default IoTCommercialEventTO toDTO(IoTCommercialEvent event) {
    if (event instanceof EventPayload ioTCommercialEvent) {
      return IoTCommercialEventTO.builder()
              .metadata(toMetadataTO(ioTCommercialEvent))
              .payload(toPayloadTO(ioTCommercialEvent))
              .build();
    } else if (event instanceof IoTCommercialRetractedEvent ioTCommercialRetractedEvent) {
      return IoTCommercialEventTO.builder()
              .metadata(toRetractedEvent(ioTCommercialRetractedEvent))
              .build();
    }
    throw new IllegalStateException("Unknown type for IoTCommercialEvent: " + event.getClass().getName());
  }

  EventMetadataTO toMetadataTO(IoTCommercialEvent event);
  EventMetadataTO toRetractedEvent(IoTCommercialEvent event);
  EventPayloadTO toPayloadTO(EventPayload ioTCommercialDomainEvent);
}
