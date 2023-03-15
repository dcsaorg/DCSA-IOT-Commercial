package org.dcsa.iot.commercial.service.mapping.transferobject;

import org.dcsa.iot.commercial.service.domain.IoTCommercialDomainEvent;
import org.dcsa.iot.commercial.transferobjects.EventMetadataTO;
import org.dcsa.iot.commercial.transferobjects.EventPayloadTO;
import org.dcsa.iot.commercial.transferobjects.IoTCommercialEventTO;
import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IoTCommercialEventTOMapper {

  default IoTCommercialEventTO toDTO(IoTCommercialDomainEvent event) {
    return IoTCommercialEventTO.builder()
        .metadata(toMetadataTO(event))
        .payload(toPayloadTO(event))
        .build();
  }

  EventMetadataTO toMetadataTO(IoTCommercialDomainEvent event);

  EventPayloadTO toPayloadTO(IoTCommercialDomainEvent ioTCommercialDomainEvent);
  @InheritInverseConfiguration(name = "toPayloadTO")
  IoTCommercialDomainEvent fromPayloadTO(EventPayloadTO eventPayloadTO);

  @InheritInverseConfiguration(name = "toPayloadTO")
  IoTCommercialDomainEvent fromMetadataTO(EventPayloadTO eventPayloadTO);

  @Mapping(source = "metadata", target = ".", qualifiedByName = "fromMetadataTO")
  @Mapping(source = "payload", target = ".", qualifiedByName = "fromPayloadTO")
  IoTCommercialDomainEvent fromDTO(IoTCommercialEventTO ioTCommercialEventTO
    , @Context EventPayloadTO eventPayloadTO, @Context EventMetadataTO eventMetadataTO);
}
