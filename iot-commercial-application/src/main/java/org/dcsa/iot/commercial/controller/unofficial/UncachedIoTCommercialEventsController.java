package org.dcsa.iot.commercial.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.iot.commercial.service.IoTCommercialEventService;
import org.dcsa.iot.commercial.service.domain.IoTCommercialDomainEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
  private final IoTCommercialEventService ioTCommercialEventService;

  @GetMapping(path = "/events/{eventID}")
  @ResponseStatus(HttpStatus.OK)
  public IoTCommercialDomainEvent findEvent(@PathVariable("eventID") UUID eventID) {
    return ioTCommercialEventService.findDomainEvent(eventID);
  }

  @GetMapping(path = "/events")
  @ResponseStatus(HttpStatus.OK)
  public List<IoTCommercialDomainEvent> findEvents() {
    return ioTCommercialEventService.findDomainEvents();
  }
}
