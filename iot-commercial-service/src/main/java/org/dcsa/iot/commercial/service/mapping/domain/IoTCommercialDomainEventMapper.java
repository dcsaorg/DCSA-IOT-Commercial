package org.dcsa.iot.commercial.service.mapping.domain;

import org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent;
import org.dcsa.iot.commercial.domain.persistence.entity.Party;
import org.dcsa.iot.commercial.service.domain.IoTCommercialDomainEvent;
import org.dcsa.iot.commercial.transferobjects.PartyTO;
import org.dcsa.iot.commercial.transferobjects.enums.CarrierCodeListProvider;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IoTCommercialDomainEventMapper {

  @Mapping(source = "eventLocation", target = "geoLocation")
  IoTCommercialDomainEvent toDomain(IoTCommercialEvent ioTCommercialEvent);
  @AfterMapping
  default void mapPublisherCarrier(
    Party party, @MappingTarget PartyTO.PartyTOBuilder partyTOBuilder) {
    if (party.getCarrier() == null) {
      return;
    }
    String nMFTACode = party.getCarrier().getNmftaCode();
    String sMDGCode = party.getCarrier().getSmdgCode();
    if (sMDGCode != null) {
      partyTOBuilder.carrierCodeListProvider(CarrierCodeListProvider.SMDG);
      partyTOBuilder.carrierCode(sMDGCode);
    } else if (nMFTACode != null) {
      partyTOBuilder.carrierCodeListProvider(CarrierCodeListProvider.NMFTA);
      partyTOBuilder.carrierCode(nMFTACode);
    }
  }
}
