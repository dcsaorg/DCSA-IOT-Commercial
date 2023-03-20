package org.dcsa.iot.commercial.transferobjects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSubscriptionSecretTO {
  public static final int MIN_SECRET_SIZE = 32;
  public static final int MAX_SECRET_SIZE = 64;

  @NotNull
  @Size(min = MIN_SECRET_SIZE, max = MAX_SECRET_SIZE)
  @ToString.Exclude
  private byte[] secret;
}
