package org.dcsa.iot.commercial.controller.unofficial;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dcsa.iot.commercial.service.unofficial.UnofficialIoTCommercialEventService;
import org.dcsa.iot.commercial.transferobjects.IoTCommercialEventTO;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Just for testing and local development.
 */
@Profile({"test", "dev"})
@RestController
@RequestMapping("${spring.application.context-path}/unofficial")
@RequiredArgsConstructor
public class UnofficialCommercialEventsController {
  private final UnofficialIoTCommercialEventService unofficialIoTCommercialEventService;
  @PostMapping(path = "/events")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void createEvents(@Valid @RequestBody IoTCommercialEventTO ioTCommercialEventTO) {
   unofficialIoTCommercialEventService.createEvent(ioTCommercialEventTO);
  }
}
