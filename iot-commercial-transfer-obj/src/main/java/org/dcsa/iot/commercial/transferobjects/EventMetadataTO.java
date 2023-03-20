package org.dcsa.iot.commercial.transferobjects;

import jakarta.persistence.GeneratedValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.dcsa.iot.commercial.transferobjects.enums.EventType;
import org.dcsa.iot.commercial.transferobjects.enums.PublisherRole;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EventMetadataTO(
    UUID eventID,
    OffsetDateTime eventCreatedDateTime,
    UUID retractedEventID,
    @NotNull @Valid PartyTO publisher,
    @NotNull PublisherRole publisherRole,
    EventType eventType) {
  @Builder(toBuilder = true)
  public EventMetadataTO {}
}
