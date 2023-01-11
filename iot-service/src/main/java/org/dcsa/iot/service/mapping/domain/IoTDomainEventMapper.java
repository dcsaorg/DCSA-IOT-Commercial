package org.dcsa.iot.service.mapping.domain;

import org.dcsa.iot.domain.persistence.entity.IoTEvent;
import org.dcsa.iot.service.domain.IoTDomainEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IoTDomainEventMapper {
  IoTDomainEvent toDomain(IoTEvent ioTEvent);
}
