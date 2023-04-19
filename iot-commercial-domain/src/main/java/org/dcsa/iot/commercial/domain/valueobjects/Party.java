package org.dcsa.iot.commercial.domain.valueobjects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.dcsa.iot.commercial.domain.valueobjects.enums.CarrierCodeListProvider;

public record Party(
  @Size(max = 100)
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
  String partyName,

  @NotNull @Size(max = 4)
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
  String carrierCode,

  @NotNull
  CarrierCodeListProvider carrierCodeListProvider
) {
  @Builder(toBuilder = true)
  public Party { }
}
