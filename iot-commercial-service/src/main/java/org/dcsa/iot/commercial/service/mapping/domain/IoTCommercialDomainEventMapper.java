package org.dcsa.iot.commercial.service.mapping.domain;

import org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent;
import org.dcsa.iot.commercial.domain.persistence.entity.Party;
import org.dcsa.iot.commercial.service.domain.IoTCommercialDomainEvent;
import org.dcsa.iot.commercial.transferobjects.PartyTO;
import org.dcsa.iot.commercial.transferobjects.enums.CarrierCodeListProvider;
import org.dcsa.skernel.domain.persistence.entity.Carrier;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
  DocumentReferenceMapper.class
})
public interface IoTCommercialDomainEventMapper {

  @Mapping(source = "eventLocation", target = "geoLocation")
  IoTCommercialDomainEvent toDomain(IoTCommercialEvent ioTCommercialEvent);
  @InheritInverseConfiguration(name = "toDomain")
  IoTCommercialEvent fromDomain(IoTCommercialDomainEvent event);
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

  @AfterMapping
  default void mapPartyCarrier(PartyTO partyTO, @MappingTarget Party.PartyBuilder partyBuilder) {
    if (partyTO.carrierCode() == null) {
      return;
    }
    if (partyTO.carrierCodeListProvider() == CarrierCodeListProvider.SMDG) {
      partyBuilder.carrier(Carrier.builder().smdgCode(partyTO.carrierCode()).build());
    } else {
      partyBuilder.carrier(Carrier.builder().nmftaCode(partyTO.carrierCode()).build());
    }
  }

}
