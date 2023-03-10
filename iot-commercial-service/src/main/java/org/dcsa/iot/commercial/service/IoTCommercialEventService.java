package org.dcsa.iot.commercial.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dcsa.iot.commercial.domain.persistence.entity.EventCache;
import org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent;
import org.dcsa.iot.commercial.domain.persistence.repository.EventCacheRepository;
import org.dcsa.iot.commercial.domain.persistence.repository.IoTCommercialEventRepository;
import org.dcsa.iot.commercial.domain.persistence.repository.specification.EventCacheSpecification;
import org.dcsa.iot.commercial.domain.persistence.repository.specification.EventCacheSpecification.EventCacheFilters;
import org.dcsa.iot.commercial.service.domain.IoTCommercialDomainEvent;
import org.dcsa.iot.commercial.service.mapping.domain.IoTCommercialDomainEventMapper;
import org.dcsa.iot.commercial.service.mapping.transferobject.IoTCommercialEventTOMapper;
import org.dcsa.iot.commercial.transferobjects.IoTCommercialEventTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IoTCommercialEventService {
  private final EventCacheRepository eventCacheRepository;
  private final IoTCommercialEventTOMapper ioTCommercialEventTOMapper;
  private final IoTCommercialDomainEventMapper ioTCommercialDomainEventMapper;
  private final ObjectMapper objectMapper;
  private final IoTCommercialEventRepository ioTCommercialEventRepository;

  @Transactional
  public IoTCommercialEventTO findEvent(UUID eventID) {
    return ioTCommercialEventTOMapper.toDTO(
      deserializeEvent(eventCacheRepository.findById(eventID)
        .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No IoT-Commercial events found for id = " + eventID))));
  }

  @Transactional
  public PagedResult<IoTCommercialEventTO> findEvents(PageRequest pageRequest, EventCacheFilters filters) {
    return new PagedResult<>(
      eventCacheRepository.findAll(EventCacheSpecification.withFilters(filters), pageRequest),
      eventCache -> ioTCommercialEventTOMapper.toDTO(deserializeEvent(eventCache)));
  }

  @Transactional
  public IoTCommercialDomainEvent findDomainEvent(UUID eventID) {
    return ioTCommercialDomainEventMapper.toDomain(
        ioTCommercialEventRepository
            .findById(eventID)
            .orElseThrow(
                () ->
                    ConcreteRequestErrorMessageException.notFound(
                        "No IoT-Commercial events found for id = " + eventID)));
  }

  @Transactional
  public List<IoTCommercialDomainEvent> findDomainEvents() {
    return ioTCommercialEventRepository.findAll().stream()
        .map(ioTCommercialDomainEventMapper::toDomain)
        .toList();
  }

  @SneakyThrows
  private IoTCommercialDomainEvent deserializeEvent(EventCache event) {
    return objectMapper.readValue(event.getContent(), IoTCommercialDomainEvent.class);
  }
}
