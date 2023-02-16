package org.dcsa.iot.commercial.domain.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dcsa.iot.commercial.domain.persistence.entity.enums.EventClassifierCode;
import org.dcsa.iot.commercial.domain.persistence.entity.enums.IoTEventCode;
import org.dcsa.iot.commercial.domain.persistence.entity.enums.IoTEventTypeCode;
import org.dcsa.iot.commercial.domain.persistence.entity.enums.PublisherRole;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.List;
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
  @GeneratedValue
  @Column(name = "event_id", nullable = false)
  private UUID eventID;

  @Column(name = "event_date_time", nullable = false)
  private OffsetDateTime eventDateTime;

  @CreatedDate
  @Column(name = "event_created_date_time", nullable = false)
  private OffsetDateTime eventCreatedDateTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "event_classifier_code", nullable = false)
  private EventClassifierCode eventClassifierCode;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "publisher_id", nullable = false)
  private Party publisher;

  @Enumerated(EnumType.STRING)
  @Column(name = "publisher_role", nullable = false)
  private PublisherRole publisherRole;

  @Column(name = "equipment_reference")
  private String equipmentReference;

  @Enumerated(EnumType.STRING)
  @Column(name = "iot_event_type_code")
  private IoTEventTypeCode iotEventTypeCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "iot_event_code")
  private IoTEventCode iotEventCode;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "eventID", fetch = FetchType.EAGER)
  private List<DocumentReference> relatedDocumentReferences;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "event_location_id")
  private Location eventLocation;
}
