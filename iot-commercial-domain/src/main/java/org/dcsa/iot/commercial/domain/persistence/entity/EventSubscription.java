package org.dcsa.iot.commercial.domain.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dcsa.iot.commercial.domain.valueobjects.enums.IoTEventTypeCode;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "event_subscription")
public class EventSubscription {
  @Id
  @GeneratedValue
  @Column(name = "subscription_id", nullable = false)
  private UUID subscriptionID;

  @Column(name = "callback_url", columnDefinition = "text", nullable = false)
  private String callbackUrl;

  @ToString.Exclude
  @Column(name = "secret", columnDefinition = "bytea", nullable = false)
  private byte[] secret;

  @CreatedDate
  @Column(name = "created_date_time", nullable = false)
  private OffsetDateTime subscriptionCreatedDateTime;

  @Column(name = "updated_date_time", nullable = false)
  private OffsetDateTime subscriptionUpdatedDateTime;

  @Column(name = "carrier_booking_reference", length = 35)
  private String carrierBookingReference;

  @Column(name = "equipment_reference", length = 11)
  private String equipmentReference;

  @Enumerated(EnumType.STRING)
  @Column(name = "iot_event_type_code")
  private IoTEventTypeCode ioTEventTypeCode;

  @PrePersist
  void setIdIfMissing() {
    if (subscriptionID == null) {
      subscriptionID = UUID.randomUUID();
    }
  }
}
