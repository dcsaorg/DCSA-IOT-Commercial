package org.dcsa.iot.commercial.service.mapping;

import org.dcsa.iot.commercial.domain.persistence.entity.EventSubscription;
import org.dcsa.iot.commercial.transferobjects.EventSubscriptionWithIdTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventSubscriptionMapper {
  EventSubscriptionWithIdTO toDTO(EventSubscription eventSubscription);
}
