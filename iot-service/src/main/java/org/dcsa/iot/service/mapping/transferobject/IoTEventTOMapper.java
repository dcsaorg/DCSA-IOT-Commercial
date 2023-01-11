package org.dcsa.iot.service.mapping.transferobject;

import org.dcsa.iot.service.domain.IoTDomainEvent;
import org.dcsa.iot.transferobjects.IoTEventTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IoTEventTOMapper {
  IoTEventTO toDTO(IoTDomainEvent ioTDomainEvent);
}
