package org.dcsa.iot.commercial.domain.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dcsa.skernel.domain.persistence.entity.Carrier;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "party")
public class Party {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "party_name", length = 100)
  private String partyName;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "carrier_id")
  private Carrier carrier;
}
