package org.dcsa.iot.commercial.service.mapping;

import org.dcsa.iot.commercial.domain.valueobjects.EventPayload;
import org.dcsa.iot.commercial.domain.valueobjects.IoTCommercialEvent;
import org.dcsa.iot.commercial.domain.valueobjects.IoTCommercialRetractedEvent;
import org.dcsa.iot.commercial.transferobjects.EventMetadataTO;
import org.dcsa.iot.commercial.transferobjects.EventPayloadTO;
import org.dcsa.iot.commercial.transferobjects.IoTCommercialEventTO;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    uses = {DocumentReferenceMapper.class})
public interface UnofficialIoTCommercialEventMapper {
  default org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent toEntity(
          IoTCommercialEvent domainEvent) {

    var builder =
            org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent.builder()
                    .eventID(domainEvent.getEventID())
                    .content(domainEvent)
                    .eventCreatedDateTime(domainEvent.getEventCreatedDateTime());

    if (domainEvent instanceof EventPayload payloadEvent) {
      builder
              .eventDateTime(payloadEvent.getEventDateTime())
              .equipmentReference(payloadEvent.getEquipmentReference())
              .iotEventTypeCode(payloadEvent.getIotEventTypeCode().name());
    }

    return builder.build();
  }
  default IoTCommercialEvent toDomain(IoTCommercialEventTO eventTO) {
    return eventTO.metadata().retractedEventID() != null
            ? toRetractedEvent(eventTO.metadata()) : toPayloadEvent(eventTO.metadata(), eventTO.payload());
  }


  @InheritInverseConfiguration(name = "toDTO")
  IoTCommercialRetractedEvent toRetractedEvent(EventMetadataTO event);

  @InheritInverseConfiguration(name = "toDTO")
  EventPayload toPayloadEvent(EventMetadataTO eventMetadataTO,EventPayloadTO eventPayloadTO);


/*

 */
}
