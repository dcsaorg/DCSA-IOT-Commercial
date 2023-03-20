package org.dcsa.iot.commercial.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dcsa.iot.commercial.domain.persistence.entity.EventSubscription_;
import org.dcsa.iot.commercial.service.EventSubscriptionService;
import org.dcsa.iot.commercial.transferobjects.EventSubscriptionWithIdTO;
import org.dcsa.iot.commercial.transferobjects.EventSubscriptionWithSecretTO;
import org.dcsa.skernel.infrastructure.pagination.Pagination;
import org.dcsa.skernel.infrastructure.sorting.Sorter;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("${spring.application.context-path}")
@RequiredArgsConstructor
public class EventSubscriptionController {
  private final EventSubscriptionService eventSubscriptionService;
  private final List<Sort.Order> DEFAULT_SORT =
      List.of(
          new Sort.Order(Sort.Direction.ASC, EventSubscription_.SUBSCRIPTION_CREATED_DATE_TIME));
  private final Sorter.SortableFields SORTABLE_FIELDS =
      Sorter.SortableFields.of(EventSubscription_.SUBSCRIPTION_CREATED_DATE_TIME);

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/event-subscriptions")
  public List<EventSubscriptionWithIdTO> getSubscriptions(
      @RequestParam(value = Pagination.DCSA_PAGE_PARAM_NAME, defaultValue = "0", required = false)
          @Min(0)
          int page,
      @RequestParam(
              value = Pagination.DCSA_PAGESIZE_PARAM_NAME,
              defaultValue = "100",
              required = false)
          @Min(1)
          int pageSize,
      @RequestParam(value = Pagination.DCSA_SORT_PARAM_NAME, required = false) String sort,
      HttpServletRequest request,
      HttpServletResponse response) {
    return Pagination.with(request, response, page, pageSize)
        .sortBy(sort, DEFAULT_SORT, SORTABLE_FIELDS)
        .paginate(eventSubscriptionService::findAll);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/event-subscriptions/{subscriptionID}")
  public EventSubscriptionWithIdTO getSubscription(
      @PathVariable("subscriptionID") UUID subscriptionID) {
    return eventSubscriptionService.getSubscription(subscriptionID);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = "/event-subscriptions")
  public EventSubscriptionWithIdTO createSubscription(@Valid @RequestBody EventSubscriptionWithSecretTO eventSubscription) {
    return eventSubscriptionService.createSubscription(eventSubscription);
  }

  @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
  @DeleteMapping(path = "/event-subscriptions/{subscriptionID}")
  public void deleteSubscription() {}

  @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
  @PutMapping(path = "/event-subscriptions/{subscriptionID}")
  public void updateSubscription() {}

  @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
  @PutMapping(path = "/event-subscriptions/{subscriptionID}/secret")
  public void updateSecret() {}
}
