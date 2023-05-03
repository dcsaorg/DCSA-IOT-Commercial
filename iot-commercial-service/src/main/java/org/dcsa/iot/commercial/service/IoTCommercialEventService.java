package org.dcsa.iot.commercial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent;
import org.dcsa.iot.commercial.domain.persistence.repository.IoTCommercialEventRepository;
import org.dcsa.iot.commercial.domain.persistence.repository.specification.IoTCommercialEventSpecification;
import org.dcsa.iot.commercial.domain.persistence.repository.specification.IoTCommercialEventSpecification.IoTCommercialEventFilters;
import org.dcsa.iot.commercial.service.mapping.IoTCommercialEventTOMapper;
import org.dcsa.iot.commercial.transferobjects.IoTCommercialEventTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IoTCommercialEventService {
  private final IoTCommercialEventRepository ioTCommercialEventRepository;
  private final IoTCommercialEventTOMapper ioTCommercialEventTOMapper;

  @Transactional
  public IoTCommercialEventTO findEvent(String eventID) {
    return  ioTCommercialEventRepository.findById(eventID)
            .map(IoTCommercialEvent::getContent)
            .map(ioTCommercialEventTOMapper::toDTO)
        .orElseThrow(() ->
                ConcreteRequestErrorMessageException.notFound("No IoT-Commercial events found for id = " + eventID));
  }

  @Transactional
  public PagedResult<IoTCommercialEventTO> findEvents(PageRequest pageRequest, IoTCommercialEventFilters filters) {
    return new PagedResult<>(
            ioTCommercialEventRepository.findAll(IoTCommercialEventSpecification.withFilters(filters), pageRequest),
      eventCache -> ioTCommercialEventTOMapper.toDTO((eventCache).getContent()));
  }

}
