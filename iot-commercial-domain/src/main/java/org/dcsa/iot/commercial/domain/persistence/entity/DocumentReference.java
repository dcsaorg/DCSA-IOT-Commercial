package org.dcsa.iot.commercial.domain.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dcsa.iot.commercial.domain.persistence.entity.enums.DocumentReferenceType;

import java.util.UUID;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "document_reference")
public class DocumentReference {

  @GeneratedValue @Id UUID id;

  @Column(name = "event_id")
  private UUID eventID;

  @Enumerated(EnumType.STRING)
  @Column(name = "document_reference_type", nullable = false)
  private DocumentReferenceType documentReferenceType;

  @Column(name = "document_reference_value", nullable = false)
  private String documentReferenceValue;
}
