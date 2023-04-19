package org.dcsa.iot.commercial.domain.valueobjects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dcsa.iot.commercial.domain.persistence.entity.DocumentReference;
import org.dcsa.iot.commercial.domain.valueobjects.enums.EventClassifierCode;
import org.dcsa.iot.commercial.domain.valueobjects.enums.IoTEventCode;
import org.dcsa.iot.commercial.domain.valueobjects.enums.IoTEventTypeCode;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.dcsa.skernel.infrastructure.validation.ISO6346EquipmentReference;
import org.dcsa.skernel.infrastructure.validation.RestrictLocationTO;

import java.time.OffsetDateTime;
import java.util.Set;

/**
 */
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
final public class EventPayload extends IoTCommercialEvent {
  @NotNull
  private EventClassifierCode eventClassifierCode;
  @NotNull OffsetDateTime eventDateTime;
  @NotNull IoTEventTypeCode iotEventTypeCode;
  @NotNull IoTEventCode iotEventCode;
  @RestrictLocationTO({LocationTO.LocationType.GEO})
  @Valid LocationTO geoLocation;
  @NotNull @ISO6346EquipmentReference
  private String equipmentReference;
  private Set<@Valid DocumentReference> relatedDocumentReferences;
}
