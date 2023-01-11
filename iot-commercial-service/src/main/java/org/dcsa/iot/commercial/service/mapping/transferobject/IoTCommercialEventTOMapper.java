package org.dcsa.iot.commercial.service.mapping.transferobject;

import org.dcsa.iot.commercial.service.domain.IoTCommercialDomainEvent;
import org.dcsa.iot.commercial.transferobjects.IoTCommercialEventTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IoTCommercialEventTOMapper {
  IoTCommercialEventTO toDTO(IoTCommercialDomainEvent ioTCommercialDomainEvent);
}
