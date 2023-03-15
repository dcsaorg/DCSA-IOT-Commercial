package org.dcsa.iot.commercial.transferobjects;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EventSubscriptionWithIdTO extends EventSubscriptionTO {
  private UUID subscriptionID;
}
