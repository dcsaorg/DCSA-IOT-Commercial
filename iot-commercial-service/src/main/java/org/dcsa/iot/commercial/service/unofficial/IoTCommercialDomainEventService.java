package org.dcsa.iot.commercial.service.unofficial;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dcsa.iot.commercial.domain.persistence.entity.DocumentReference;
import org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent;
import org.dcsa.iot.commercial.domain.persistence.entity.Party;
import org.dcsa.iot.commercial.domain.persistence.repository.IoTCommercialEventRepository;
import org.dcsa.iot.commercial.domain.persistence.repository.unofficial.DocumentReferenceRepository;
import org.dcsa.iot.commercial.domain.persistence.repository.unofficial.PartyRepository;
import org.dcsa.iot.commercial.service.domain.IoTCommercialDomainEvent;
import org.dcsa.iot.commercial.service.mapping.domain.DocumentReferenceMapper;
import org.dcsa.iot.commercial.service.mapping.domain.IoTCommercialDomainEventMapper;
import org.dcsa.iot.commercial.service.mapping.transferobject.IoTCommercialEventTOMapper;
import org.dcsa.iot.commercial.transferobjects.DocumentReferenceTO;
import org.dcsa.iot.commercial.transferobjects.IoTCommercialEventTO;
import org.dcsa.iot.commercial.transferobjects.PartyTO;
import org.dcsa.iot.commercial.transferobjects.enums.CarrierCodeListProvider;
import org.dcsa.skernel.domain.persistence.entity.Carrier;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.domain.persistence.repository.CarrierRepository;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.services.LocationService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class IoTCommercialDomainEventService {
  private final IoTCommercialEventTOMapper ioTCommercialEventTOMapper;
  private final IoTCommercialDomainEventMapper ioTCommercialDomainEventMapper;
  private final IoTCommercialEventRepository ioTCommercialEventRepository;
  private final LocationService locationService;
  private final CarrierRepository carrierRepository;
  private final PartyRepository partyRepository;
  private final DocumentReferenceRepository documentReferenceRepository;

  private final DocumentReferenceMapper documentReferenceMapper;

  @Transactional
  public IoTCommercialDomainEvent findDomainEvent(UUID eventID) {
    return this.toDomain(
        ioTCommercialEventRepository
            .findById(eventID)
            .orElseThrow(
                () ->
                    ConcreteRequestErrorMessageException.notFound(
                        "No IoT-Commercial events found for id = " + eventID)));
  }

  private IoTCommercialDomainEvent toDomain(IoTCommercialEvent event) {
    IoTCommercialDomainEvent ioTCommercialDomainEvent = ioTCommercialDomainEventMapper.toDomain(event);
    return ioTCommercialDomainEvent.toBuilder()
      .relatedDocumentReferences(findFor(ioTCommercialDomainEvent.eventID()))
      .build();
  }

  private List<@Valid DocumentReferenceTO> findFor(UUID eventID) {
    return documentReferenceRepository.findAllByEventID(eventID)
      .stream()
      .map(documentReferenceMapper::toDTO)
      .toList();
  }
  @Transactional
  public void createEvent(IoTCommercialEventTO ioTCommercialEventTO) {
    IoTCommercialDomainEvent ioTCommercialDomainEvent =
        ioTCommercialEventTOMapper.fromDTO(
            ioTCommercialEventTO, ioTCommercialEventTO.payload(), ioTCommercialEventTO.metadata());

    Location eventLocation =
        locationService.ensureResolvable(ioTCommercialDomainEvent.geoLocation());

    Party publisher = savePublisher(ioTCommercialDomainEvent.publisher());

    IoTCommercialEvent ioTCommercialEvent =
        ioTCommercialDomainEventMapper.fromDomain(ioTCommercialDomainEvent);
    ioTCommercialEvent =
        ioTCommercialEvent.toBuilder()
            .eventCreatedDateTime(
                ioTCommercialEvent.getEventCreatedDateTime() != null
                    ? ioTCommercialEvent.getEventCreatedDateTime()
                    : OffsetDateTime.now())
            .eventLocation(eventLocation)
            .publisher(publisher)
            .build();

    var savedIoTEvent = ioTCommercialEventRepository.save(ioTCommercialEvent);
    saveDocumentReferences(ioTCommercialEvent.getRelatedDocumentReferences(), savedIoTEvent.getEventID());
  }

  @Transactional
  public List<IoTCommercialDomainEvent> findDomainEvents() {
    return ioTCommercialEventRepository.findAll().stream()
        .map(this::toDomain)
        .toList();
  }

  @Transactional(Transactional.TxType.MANDATORY)
  public void saveDocumentReferences(
      List<DocumentReference> documentReferences, UUID eventID) {
    if (documentReferences != null && !documentReferences.isEmpty()) {
      documentReferences.forEach(
          documentReference -> {
            applyIfNotNull(
                documentReference.toBuilder().eventID(eventID).build(),
                documentReferenceRepository::save);
          });
    }
  }

  private Party savePublisher(PartyTO partyTO) {
    return applyIfNotNull(
        partyTO,
        pTO ->
            partyRepository.save(
                Party.builder()
                    .partyName(partyTO.partyName())
                    .carrier(resolveCarrier(partyTO))
                    .build()));
  }

  private static <T, D> D applyIfNotNull(T entity, Function<T, D> function) {
    if (entity != null) {
      return function.apply(entity);
    }
    return null;
  }

  private Carrier resolveCarrier(PartyTO partyTO) {
    Carrier carrier = null;
    if (CarrierCodeListProvider.SMDG.equals(partyTO.carrierCodeListProvider())) {
      carrier = carrierRepository.findBySmdgCode(partyTO.carrierCode());
    } else {
      carrier = carrierRepository.findByNmftaCode(partyTO.carrierCode());
    }
    if (null == carrier) {
      throw ConcreteRequestErrorMessageException.invalidInput(
          "Cannot find any vessel operator with carrier code: " + partyTO.carrierCode());
    }
    return carrier;
  }
}
