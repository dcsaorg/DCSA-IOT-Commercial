package org.dcsa.iot.service.domain;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * TODO real domain event when we have it
 * domain event is the format that we serialize to when caching in the event cache table.
 */
public record IoTDomainEvent(
  UUID eventId,
  OffsetDateTime eventCreatedDateTime,
  OffsetDateTime eventDateTime
) {
  @Builder
  public IoTDomainEvent { }
}
