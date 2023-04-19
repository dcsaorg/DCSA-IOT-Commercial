package org.dcsa.iot.commercial.domain.persistence.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.dcsa.iot.commercial.domain.valueobjects.enums.IoTEventTypeCode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "iot_commercial_event")
public class IoTCommercialEvent {
  @Id
  @Column(name = "event_id", nullable = false, length = 100)
  private String eventID;

  @Type(JsonBinaryType.class)
  @Column(name = "content", columnDefinition = "jsonb", nullable = false)
  private org.dcsa.iot.commercial.domain.valueobjects.IoTCommercialEvent content;

  @CreatedDate
  @Column(name = "event_created_date_time", nullable = false)
  private OffsetDateTime eventCreatedDateTime;

  @Column(name = "event_date_time")
  private OffsetDateTime eventDateTime;

  @Formula("content->>'iotEventTypeCodes'")
  @Enumerated(EnumType.STRING)
  private IoTEventTypeCode iotEventTypeCode;

  @Formula("content->>'equipmentReference'")
  private String equipmentReference;

  @Formula("content->''->>'carrierBR'")
  private String carrierBR;

  @PrePersist
  void setIdIfMissing() {
    if (eventID == null) {
      eventID = String.valueOf(UUID.randomUUID());
    }
  }
}
