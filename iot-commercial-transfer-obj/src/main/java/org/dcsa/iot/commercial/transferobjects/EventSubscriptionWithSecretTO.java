package org.dcsa.iot.commercial.transferobjects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EventSubscriptionWithSecretTO extends EventSubscriptionTO {
  @NotNull
  @Size(
      min = EventSubscriptionSecretTO.MIN_SECRET_SIZE,
      max = EventSubscriptionSecretTO.MAX_SECRET_SIZE)
  @ToString.Exclude
  private byte[] secret;
}
