package org.dcsa.iot.commercial.transferobjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dcsa.iot.commercial.transferobjects.enums.IoTEventTypeCode;

import java.time.OffsetDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EventSubscriptionTO {
  @NotBlank private String callbackUrl;

  @Size(max = 100)
  private String carrierBookingReference;

  @Size(max = 11)
  private String equipmentReference;

  private OffsetDateTime subscriptionCreatedDateTime;
  private OffsetDateTime subscriptionUpdatedDateTime;
  private IoTEventTypeCode ioTEventTypeCode;
}
