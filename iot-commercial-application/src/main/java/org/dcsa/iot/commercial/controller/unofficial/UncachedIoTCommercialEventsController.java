package org.dcsa.iot.commercial.controller.unofficial;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dcsa.iot.commercial.service.unofficial.IoTCommercialDomainEventService;
import org.dcsa.iot.commercial.service.domain.IoTCommercialDomainEvent;
import org.dcsa.iot.commercial.transferobjects.IoTCommercialEventTO;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Just for testing and local development.
 */
@Profile({"test", "dev"})
@RestController
@RequestMapping("${spring.application.context-path}/unofficial/uncached-domain")
@RequiredArgsConstructor
public class UncachedIoTCommercialEventsController {
  private final IoTCommercialDomainEventService ioTCommercialDomainEventService;

  @GetMapping(path = "/events/{eventID}")
  @ResponseStatus(HttpStatus.OK)
  public IoTCommercialDomainEvent findEvent(@PathVariable("eventID") UUID eventID) {
    return ioTCommercialDomainEventService.findDomainEvent(eventID);
  }

  @GetMapping(path = "/events")
  @ResponseStatus(HttpStatus.OK)
  public List<IoTCommercialDomainEvent> findEvents() {
    return ioTCommercialDomainEventService.findDomainEvents();
  }

  @PostMapping(path = "/events")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void createEvents(@Valid @RequestBody IoTCommercialEventTO ioTCommercialEventTO) {
   ioTCommercialDomainEventService.createEvent(ioTCommercialEventTO);
  }
}
