package org.dcsa.iot.commercial.transferobjects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.dcsa.iot.commercial.transferobjects.enums.EventClassifierCode;
import org.dcsa.iot.commercial.transferobjects.enums.IoTEventCode;
import org.dcsa.iot.commercial.transferobjects.enums.IoTEventTypeCode;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.dcsa.skernel.infrastructure.validation.RestrictLocationTO;

import java.time.OffsetDateTime;
import java.util.List;

public record EventPayloadTO(
  @NotNull EventClassifierCode eventClassifierCode,
  @NotNull OffsetDateTime eventDateTime,
  @NotNull IoTEventTypeCode iotEventTypeCode,
  @NotNull IoTEventCode iotEventCode,
  @RestrictLocationTO({LocationTO.LocationType.GEO})
  @Valid LocationTO geoLocation,
  @NotBlank String equipmentReference,
  List<@Valid DocumentReferenceTO> relatedDocumentReferences){
@Builder(toBuilder = true)
public EventPayloadTO {}
  }
