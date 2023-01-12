package org.dcsa.iot.commercial.service.mapping.domain;

import org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent;
import org.dcsa.iot.commercial.service.domain.IoTCommercialDomainEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IoTCommercialDomainEventMapper {
  IoTCommercialDomainEvent toDomain(IoTCommercialEvent ioTCommercialEvent);
}
