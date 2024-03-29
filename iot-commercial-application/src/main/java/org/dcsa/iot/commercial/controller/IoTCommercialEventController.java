package org.dcsa.iot.commercial.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent_;
import org.dcsa.iot.commercial.domain.persistence.repository.specification.IoTCommercialEventSpecification.IoTCommercialEventFilters;
import org.dcsa.iot.commercial.domain.valueobjects.enums.IoTEventTypeCode;
import org.dcsa.iot.commercial.service.IoTCommercialEventService;
import org.dcsa.iot.commercial.transferobjects.IoTCommercialEventTO;
import org.dcsa.skernel.infrastructure.http.queryparams.DCSAQueryParameterParser;
import org.dcsa.skernel.infrastructure.pagination.Pagination;
import org.dcsa.skernel.infrastructure.sorting.Sorter.SortableFields;
import org.dcsa.skernel.infrastructure.util.EnumUtil;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.*;


@Validated
@RestController
@RequestMapping("${spring.application.context-path}")
@RequiredArgsConstructor
public class IoTCommercialEventController {
  private final List<Order> defaultSort = List.of(new Sort.Order(Sort.Direction.ASC, IoTCommercialEvent_.EVENT_CREATED_DATE_TIME));
  private final SortableFields sortableFields = SortableFields.of(IoTCommercialEvent_.EVENT_CREATED_DATE_TIME, IoTCommercialEvent_.EVENT_DATE_TIME);
  private final IoTCommercialEventService ioTCommercialEventService;
  private final DCSAQueryParameterParser queryParameterParser;

  @GetMapping(path = "/events/{eventID}")
  @ResponseStatus(HttpStatus.OK)
  public IoTCommercialEventTO findEvent(@PathVariable("eventID") String eventID) {
    return ioTCommercialEventService.findEvent(eventID);
  }

  @GetMapping(path = "/events")
  @ResponseStatus(HttpStatus.OK)
  public List<IoTCommercialEventTO> findEvents(
    @RequestParam(value = Pagination.DCSA_PAGE_PARAM_NAME, defaultValue = "0", required = false) @Min(0)
    int page,
    @RequestParam(value = Pagination.DCSA_PAGESIZE_PARAM_NAME, defaultValue = "100", required = false) @Min(1)
    int pageSize,
    @RequestParam(value = Pagination.DCSA_SORT_PARAM_NAME, required = false)
    String sort,
    @RequestParam(value = "iotEventTypeCodes", required = false)
    String iotEventTypeCodes,
    @RequestParam(value = "equipmentReference", required = false) @Size(max = 11)
    String equipmentReference,
    @RequestParam(value = "carrierBookingReference", required = false) @Size(max = 35)
    String carrierBookingReference,
    @RequestParam
    Map<String, String> queryParams,
    HttpServletRequest request, HttpServletResponse response
  ) {
    List<IoTEventTypeCode> ioTEventTypeCodeList =
            Objects.requireNonNullElse(EnumUtil.toEnumList(iotEventTypeCodes, IoTEventTypeCode.class), Collections.emptyList());

    return Pagination.with(request, response, page, pageSize)
        .sortBy(sort, defaultSort, sortableFields)
        .paginate(
            pageRequest ->
                ioTCommercialEventService.findEvents(
                    pageRequest,
                    IoTCommercialEventFilters.builder()
                        .eventCreatedDateTime(
                            queryParameterParser.parseCustomQueryParameter(
                                queryParams, "eventCreatedDateTime", OffsetDateTime::parse))
                        .eventDateTime(
                            queryParameterParser.parseCustomQueryParameter(
                                queryParams, "eventDateTime", OffsetDateTime::parse))
                        .iotEventTypeCodes(Set.copyOf(ioTEventTypeCodeList))
                        .equipmentReference(equipmentReference)
                        .carrierBookingReference(carrierBookingReference)
                        .build()));
  }
}
