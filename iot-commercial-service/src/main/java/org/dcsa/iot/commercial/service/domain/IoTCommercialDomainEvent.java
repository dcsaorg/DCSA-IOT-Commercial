package org.dcsa.iot.commercial.service.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.dcsa.iot.commercial.transferobjects.DocumentReferenceTO;
import org.dcsa.iot.commercial.transferobjects.PartyTO;
import org.dcsa.iot.commercial.transferobjects.enums.*;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.dcsa.skernel.infrastructure.validation.RestrictLocationTO;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record IoTCommercialDomainEvent(
  @NotNull UUID eventID,
  @NotNull OffsetDateTime eventCreatedDateTime,
  UUID retractedEventID,
  @NotNull @Valid PartyTO publisher,
  @NotNull PublisherRole publisherRole,
  EventType eventType,
  @NotNull EventClassifierCode eventClassifierCode,
  @NotNull OffsetDateTime eventDateTime,
  @NotNull IoTEventTypeCode iotEventTypeCode,
  @NotNull IoTEventCode iotEventCode,
  @RestrictLocationTO({LocationTO.LocationType.GEO})
  @Valid LocationTO geoLocation,
  @NotBlank String equipmentReference,
  List<@Valid DocumentReferenceTO> relatedDocumentReferences
) {
  @Builder(toBuilder = true)
  public IoTCommercialDomainEvent { }
}
