package org.dcsa.iot.commercial.transferobjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.dcsa.iot.commercial.transferobjects.enums.CarrierCodeListProvider;

public record PartyTO(
  @Size(max = 100)
  String partyName,

  @NotBlank @Size(max = 4)
  String carrierCode,

  @NotEmpty CarrierCodeListProvider carrierCodeListProvider
) {
  @Builder(toBuilder = true)
  public PartyTO { }
}
