package org.dcsa.iot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PessimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.iot.domain.persistence.entity.EventCache;
import org.dcsa.iot.domain.persistence.entity.EventCacheQueue;
import org.dcsa.iot.domain.persistence.entity.EventCacheQueueDead;
import org.dcsa.iot.domain.persistence.entity.enums.EventType;
import org.dcsa.iot.domain.persistence.repository.EventCacheQueueDeadRepository;
import org.dcsa.iot.domain.persistence.repository.EventCacheQueueRepository;
import org.dcsa.iot.domain.persistence.repository.EventCacheRepository;
import org.dcsa.iot.service.domain.IoTDomainEvent;
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
  @Value("${dcsa.iot.cache.max-queue-results}")
  private int maxQueueResults = 100;

  private final TransactionTemplate transactionTemplate;
  private final IoTEventService ioTEventService;
  private final ObjectMapper objectMapper;

  private final EventCacheRepository eventCacheRepository;
  private final EventCacheQueueRepository eventCacheQueueRepository;
  private final EventCacheQueueDeadRepository eventCacheQueueDeadRepository;

  @Scheduled(initialDelayString = "${dcsa.iot.cache.initial-delay}", fixedDelayString = "${dcsa.iot.cache.delay}")
  public void scheduledEventCaching() {
    log.debug("ScheduledEventCaching");
    cacheEvents();
  }

  public void cacheEvents() {
    List<EventCacheQueue> eventsToCache = eventCacheQueueRepository.findByEventType(EventType.IOT, PageRequest.of(0, maxQueueResults));
    log.debug("Found {} events to cache", eventsToCache.size());
    eventsToCache.forEach(eventCacheQueue ->
      transactionTemplate.executeWithoutResult(transaction -> cacheEvent(eventCacheQueue))
    );
  }

  private void cacheEvent(EventCacheQueue event) {
    log.debug("Attempting to cache {}", event);
    try {
      eventCacheQueueRepository.findAndLockByEventID(event.getEventID()).ifPresent(lockedEventCacheQueue -> {
        eventCacheRepository.save(buildEventCache(ioTEventService.findDomainEvent(event.getEventID())));
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
  private EventCache buildEventCache(IoTDomainEvent domainEvent) {
    return EventCache.builder()
      .eventID(domainEvent.eventId())
      .eventDateTime(domainEvent.eventDateTime())
      .eventCreatedDateTime(domainEvent.eventCreatedDateTime())
      .eventType(EventType.IOT)
      .content(objectMapper.writeValueAsString(domainEvent))
      .build();
  }
}
