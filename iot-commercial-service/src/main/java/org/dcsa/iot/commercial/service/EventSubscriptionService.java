package org.dcsa.iot.commercial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.iot.commercial.domain.persistence.entity.EventSubscription;
import org.dcsa.iot.commercial.domain.persistence.entity.enums.IoTEventTypeCode;
import org.dcsa.iot.commercial.domain.persistence.repository.EventSubscriptionRepository;
import org.dcsa.iot.commercial.service.mapping.EventSubscriptionMapper;
import org.dcsa.iot.commercial.transferobjects.EventSubscriptionSecretTO;
import org.dcsa.iot.commercial.transferobjects.EventSubscriptionTO;
import org.dcsa.iot.commercial.transferobjects.EventSubscriptionWithIdTO;
import org.dcsa.iot.commercial.transferobjects.EventSubscriptionWithSecretTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventSubscriptionService {
  private final EventSubscriptionRepository eventSubscriptionRepository;
  private final EventSubscriptionMapper eventSubscriptionMapper;

  @Transactional
  public PagedResult<EventSubscriptionWithIdTO> findAll(final PageRequest pageRequest) {
    return new PagedResult<>(
        eventSubscriptionRepository.findAll(pageRequest), eventSubscriptionMapper::toDTO);
  }

  @Transactional
  public EventSubscriptionWithIdTO getSubscription(UUID subscriptionID) {
    return eventSubscriptionRepository
        .findById(subscriptionID)
        .map(eventSubscriptionMapper::toDTO)
        .orElseThrow(
            () ->
                ConcreteRequestErrorMessageException.notFound(
                    "No event-subscription found with id = " + subscriptionID));
  }

  @Transactional
  public EventSubscriptionWithIdTO createSubscription(EventSubscriptionWithSecretTO eventSubscriptionTo) {
    EventSubscription eventSubscription =
        eventSubscriptionRepository.save(
            eventSubscriptionMapper.toDAO(eventSubscriptionTo).toBuilder()
                .subscriptionCreatedDateTime(OffsetDateTime.now())
                .build());
    UUID subscriptionID = eventSubscription.getSubscriptionID();
    return getSubscription(subscriptionID);
  }

  @Transactional
  public void updateSubscription(UUID subscriptionID, EventSubscriptionTO eventSubscription) {
    EventSubscription original = eventSubscriptionRepository.findById(subscriptionID)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound("No event-subscription found with id = " + subscriptionID));

    eventSubscriptionRepository.save(
      original.toBuilder()
        .subscriptionUpdatedDateTime(OffsetDateTime.now())
        .callbackUrl(eventSubscription.getCallbackUrl())
        .ioTEventTypeCode(IoTEventTypeCode.valueOf(eventSubscription.getIoTEventTypeCode().name()))
        .carrierBookingReference(eventSubscription.getCarrierBookingReference())
        .equipmentReference(eventSubscription.getEquipmentReference())
        .build()
    );
  }

  @Transactional
  public void updateSecret(UUID subscriptionID, EventSubscriptionSecretTO eventSubscriptionSecret) {
    eventSubscriptionRepository.updateSecret(subscriptionID, eventSubscriptionSecret.getSecret());
  }

  @Transactional
  public void deleteSubscription(UUID subscriptionID) {
    eventSubscriptionRepository.deleteById(subscriptionID);
  }
}
