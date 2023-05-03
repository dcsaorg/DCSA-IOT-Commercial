package org.dcsa.iot.commercial.service.unofficial;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dcsa.iot.commercial.domain.persistence.entity.DocumentReference;
import org.dcsa.iot.commercial.domain.persistence.repository.IoTCommercialEventRepository;
import org.dcsa.iot.commercial.domain.persistence.repository.unofficial.DocumentReferenceRepository;
import org.dcsa.iot.commercial.domain.valueobjects.EventPayload;
import org.dcsa.iot.commercial.service.mapping.UnofficialIoTCommercialEventMapper;
import org.dcsa.iot.commercial.transferobjects.EventMetadataTO;
import org.dcsa.iot.commercial.transferobjects.EventPayloadTO;
import org.dcsa.iot.commercial.transferobjects.IoTCommercialEventTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class UnofficialIoTCommercialEventService {
  private final UnofficialIoTCommercialEventMapper unofficialIoTCommercialEventMapper;
  private final IoTCommercialEventRepository ioTCommercialEventRepository;
  private final DocumentReferenceRepository documentReferenceRepository;

  @Transactional
  public void createEvent(IoTCommercialEventTO ioTCommercialEventTO) {

    EventMetadataTO metadata = ioTCommercialEventTO.metadata();
    EventPayloadTO payload = ioTCommercialEventTO.payload();

    if (metadata.retractedEventID() == null && payload == null) {
      throw ConcreteRequestErrorMessageException.invalidInput("event should either have a retractedEventID or a payload");
    }
    if (metadata.retractedEventID() != null && payload != null) {
      throw ConcreteRequestErrorMessageException.invalidInput("event should not have both a retractedEventID or a payload");
    }
    org.dcsa.iot.commercial.domain.valueobjects.IoTCommercialEvent newEvent =
            unofficialIoTCommercialEventMapper.toDomain(ioTCommercialEventTO);
    var savedEvent =
        ioTCommercialEventRepository.save(unofficialIoTCommercialEventMapper.toEntity(newEvent));
    if (newEvent instanceof EventPayload payloadEvent) {
      saveDocumentReferences(payloadEvent.getRelatedDocumentReferences(), savedEvent.getEventID());
    }
    ioTCommercialEventRepository.flush();
  }

  @Transactional(Transactional.TxType.MANDATORY)
  private void saveDocumentReferences(
          Set<DocumentReference> documentReferences, String eventID) {
    if (documentReferences != null && !documentReferences.isEmpty()) {
      documentReferences.forEach(
          documentReference -> {
            applyIfNotNull(
                documentReference.toBuilder().eventID(eventID).build(),
                documentReferenceRepository::save);
          });
    }
  }

  private static <T, D> D applyIfNotNull(T entity, Function<T, D> function) {
    if (entity != null) {
      return function.apply(entity);
    }
    return null;
  }

}
