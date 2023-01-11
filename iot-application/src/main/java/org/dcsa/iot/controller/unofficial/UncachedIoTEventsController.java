package org.dcsa.iot.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.iot.service.IoTEventService;
import org.dcsa.iot.service.domain.IoTDomainEvent;
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
public class UncachedIoTEventsController {
  private final IoTEventService ioTEventService;

  @GetMapping(path = "/events/{eventID}")
  @ResponseStatus(HttpStatus.OK)
  public IoTDomainEvent findEvent(@PathVariable("eventID") UUID eventID) {
    return ioTEventService.findDomainEvent(eventID);
  }

  @GetMapping(path = "/events")
  @ResponseStatus(HttpStatus.OK)
  public List<IoTDomainEvent> findEvents() {
    return ioTEventService.findDomainEvents();
  }
}
