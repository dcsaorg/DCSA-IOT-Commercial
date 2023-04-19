package org.dcsa.iot.commercial.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dcsa.iot.commercial.domain.valueobjects.enums.EventType;
import org.dcsa.iot.commercial.domain.valueobjects.enums.PublisherRole;

import java.time.OffsetDateTime;

@Getter
@ToString
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "class")
public abstract sealed class IoTCommercialEvent permits EventPayload, IoTCommercialRetractedEvent {
  @NotBlank
  @Size(max = 100)
  private String eventID;

  @NotNull private EventType eventType;

  @NotNull private OffsetDateTime eventCreatedDateTime;

  @NotNull @Valid private Party publisher;

  @NotNull private PublisherRole publisherRole;
}
