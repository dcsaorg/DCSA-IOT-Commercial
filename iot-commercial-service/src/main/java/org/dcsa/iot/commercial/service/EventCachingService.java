package org.dcsa.iot.commercial.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PessimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.iot.commercial.domain.persistence.entity.EventCache;
import org.dcsa.iot.commercial.domain.persistence.entity.EventCacheQueue;
import org.dcsa.iot.commercial.domain.persistence.entity.EventCacheQueueDead;
import org.dcsa.iot.commercial.domain.persistence.entity.enums.EventType;
import org.dcsa.iot.commercial.domain.persistence.repository.EventCacheQueueDeadRepository;
import org.dcsa.iot.commercial.domain.persistence.repository.EventCacheQueueRepository;
import org.dcsa.iot.commercial.domain.persistence.repository.EventCacheRepository;
import org.dcsa.iot.commercial.service.domain.IoTCommercialDomainEvent;
import org.dcsa.iot.commercial.service.unofficial.IoTCommercialDomainEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventCachingService {
  @Value("${dcsa.iot-commercial.cache.max-queue-results}")
  private int maxQueueResults = 100;

  private final TransactionTemplate transactionTemplate;
  private final IoTCommercialDomainEventService ioTCommercialDomainEventService;
  private final ObjectMapper objectMapper;

  private final EventCacheRepository eventCacheRepository;
  private final EventCacheQueueRepository eventCacheQueueRepository;
  private final EventCacheQueueDeadRepository eventCacheQueueDeadRepository;

  @Scheduled(initialDelayString = "${dcsa.iot-commercial.cache.initial-delay}", fixedDelayString = "${dcsa.iot-commercial.cache.delay}")
  public void scheduledEventCaching() {
    log.debug("ScheduledEventCaching");
    cacheEvents();
  }

  public void cacheEvents() {
    List<EventCacheQueue> eventsToCache = eventCacheQueueRepository.findByEventType(EventType.IOT_COMMERCIAL, PageRequest.of(0, maxQueueResults));
    log.debug("Found {} events to cache", eventsToCache.size());
    eventsToCache.forEach(eventCacheQueue ->
      transactionTemplate.executeWithoutResult(transaction -> cacheEvent(eventCacheQueue))
    );
  }

  private void cacheEvent(EventCacheQueue event) {
    log.debug("Attempting to cache {}", event);
    try {
      eventCacheQueueRepository.findAndLockByEventID(event.getEventID()).ifPresent(lockedEventCacheQueue -> {
        eventCacheRepository.save(buildEventCache(ioTCommercialDomainEventService.findDomainEvent(event.getEventID())));
        eventCacheQueueRepository.delete(lockedEventCacheQueue);
      });
    } catch (PessimisticLockException e) {
      log.info("Unable to obtain lock on {} - skipping for now", event);
    } catch (Exception e) {
      log.error("Failed to cache {}", event, e);
      eventCacheQueueDeadRepository.save(EventCacheQueueDead.from(event, e));
      eventCacheQueueRepository.delete(event);
    }
  }

  @SneakyThrows
  private EventCache buildEventCache(IoTCommercialDomainEvent domainEvent) {
    return EventCache.builder()
      .eventID(domainEvent.eventID())
      .eventDateTime(domainEvent.eventDateTime())
      .eventCreatedDateTime(domainEvent.eventCreatedDateTime())
      .eventType(EventType.IOT_COMMERCIAL)
      .content(objectMapper.writeValueAsString(domainEvent))
      .build();
  }
}
