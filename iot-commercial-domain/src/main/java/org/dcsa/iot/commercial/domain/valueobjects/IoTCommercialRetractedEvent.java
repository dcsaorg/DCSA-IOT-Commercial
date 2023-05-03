package org.dcsa.iot.commercial.domain.valueobjects;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
final public class IoTCommercialRetractedEvent extends IoTCommercialEvent {
  @Size(max = 100)
  private String retractedEventID;
}
