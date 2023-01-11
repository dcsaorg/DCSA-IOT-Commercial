package org.dcsa.iot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dcsa.iot.domain.persistence.entity.EventCache;
import org.dcsa.iot.domain.persistence.repository.EventCacheRepository;
import org.dcsa.iot.domain.persistence.repository.specification.EventCacheSpecification;
import org.dcsa.iot.domain.persistence.repository.specification.EventCacheSpecification.EventCacheFilters;
import org.dcsa.iot.service.domain.IoTDomainEvent;
import org.dcsa.iot.service.mapping.domain.IoTDomainEventMapper;
import org.dcsa.iot.service.mapping.transferobject.IoTEventTOMapper;
import org.dcsa.iot.transferobjects.IoTEventTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IoTEventService {
  private final EventCacheRepository eventCacheRepository;
  private final IoTEventTOMapper ioTEventTOMapper;
  private final IoTDomainEventMapper ioTDomainEventMapper;
  private final ObjectMapper objectMapper;

  @Transactional
  public IoTEventTO findEvent(UUID eventID) {
    return ioTEventTOMapper.toDTO(
      deserializeEvent(eventCacheRepository.findById(eventID)
        .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No IoT events found for id = " + eventID))));
  }

  @Transactional
  public PagedResult<IoTEventTO> findEvents(PageRequest pageRequest, EventCacheFilters filters) {
    return new PagedResult<>(
      eventCacheRepository.findAll(EventCacheSpecification.withFilters(filters), pageRequest),
      eventCache -> ioTEventTOMapper.toDTO(deserializeEvent(eventCache)));
  }

  @Transactional
  public IoTDomainEvent findDomainEvent(UUID eventID) {
    throw ConcreteRequestErrorMessageException.notFound("No IoT events found for id = " + eventID);
    // TODO fetch event from db and convert it to IoTDomainEvent (not from cache)
    // return ioTDomainEventMapper.toDomain(ioTEventRepository.findById(eventId)
    //    .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No IoT events found for id = " + eventID))));
  }

  @Transactional
  public List<IoTDomainEvent> findDomainEvents() {
    return Collections.emptyList();
    // TODO fetch events from db and convert them to IoTDomainEvent (not from cache)
    // return ioTEventRepository.findAll().stream()
    //    .map(ioTDomainEventMapper::toDomain)
    //    .toList();
  }

  @SneakyThrows
  private IoTDomainEvent deserializeEvent(EventCache event) {
    return objectMapper.readValue(event.getContent(), IoTDomainEvent.class);
  }
}
