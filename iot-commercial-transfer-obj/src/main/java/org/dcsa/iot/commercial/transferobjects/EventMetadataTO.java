package org.dcsa.iot.commercial.transferobjects;

import jakarta.persistence.GeneratedValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.dcsa.iot.commercial.transferobjects.enums.EventType;
import org.dcsa.iot.commercial.transferobjects.enums.PublisherRole;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EventMetadataTO(
    @NotBlank
    @Size(max = 100)
    String eventID,
    OffsetDateTime eventCreatedDateTime,
    @Size(max = 100)
    String retractedEventID,
    @NotNull @Valid PartyTO publisher,
    @NotNull PublisherRole publisherRole,
    EventType eventType) {
  @Builder(toBuilder = true)
  public EventMetadataTO {}
}
