package org.dcsa.iot.commercial.domain.valueobjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.dcsa.iot.commercial.domain.valueobjects.enums.DocumentReferenceType;

public record DocumentReference(
  @NotNull
  DocumentReferenceType type,

  @NotBlank @Size(max = 100)
  String value
) {
  @Builder(toBuilder = true)
  public DocumentReference { }
}
