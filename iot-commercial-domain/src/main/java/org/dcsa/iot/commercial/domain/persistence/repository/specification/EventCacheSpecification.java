package org.dcsa.iot.commercial.domain.persistence.repository.specification;

import jakarta.persistence.criteria.*;
import lombok.Builder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.iot.commercial.domain.persistence.entity.DocumentReference_;
import org.dcsa.iot.commercial.domain.persistence.entity.EventCache;
import org.dcsa.iot.commercial.domain.persistence.entity.EventCache_;
import org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent_;
import org.dcsa.iot.commercial.domain.persistence.entity.enums.EventType;
import org.dcsa.iot.commercial.domain.persistence.entity.enums.IoTEventTypeCode;
import org.dcsa.skernel.infrastructure.http.queryparams.ParsedQueryParameter;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class EventCacheSpecification {

  public record EventCacheFilters(
    List<ParsedQueryParameter<OffsetDateTime>> eventCreatedDateTime,
    List<ParsedQueryParameter<OffsetDateTime>> eventDateTime,
    List<IoTEventTypeCode> iotEventTypeCodes,
    String carrierBookingReference,
    String equipmentReference
  ) {
    @Builder
    public EventCacheFilters { }
  }

  public static Specification<EventCache> withFilters(final EventCacheFilters filters) {
    log.debug("Searching based on {}", filters);

    return (Root<EventCache> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      JsonPathExpressionBuilder jsonPath = new JsonPathExpressionBuilder(root, builder, EventCache_.CONTENT);
      predicates.add(builder.equal(root.get(EventCache_.EVENT_TYPE), EventType.IOT_COMMERCIAL));

      handleParsedQueryParameter(
        predicates,
        builder,
        root.get(EventCache_.EVENT_CREATED_DATE_TIME),
        filters.eventCreatedDateTime
      );

      handleParsedQueryParameter(
        predicates,
        builder,
        root.get(EventCache_.EVENT_DATE_TIME),
        filters.eventDateTime
      );

      if (filters.iotEventTypeCodes != null && !filters.iotEventTypeCodes.isEmpty()) {
        predicates.add(
          jsonPath
            .of(IoTCommercialEvent_.IOT_EVENT_TYPE_CODE)
            .in(filters.iotEventTypeCodes.stream().map(Enum::name).toList()));
      }

      conditionallyAddEqualsFilter(
        predicates,
        builder,
        jsonPath.of(IoTCommercialEvent_.RELATED_DOCUMENT_REFERENCES, DocumentReference_.DOCUMENT_REFERENCE_VALUE),
        filters.carrierBookingReference
      );

      conditionallyAddEqualsFilter(
        predicates,
        builder,
        jsonPath.of(IoTCommercialEvent_.EQUIPMENT_REFERENCE),
        filters.equipmentReference
      );

      return builder.and(predicates.toArray(Predicate[]::new));
    };
  }

  private static <T extends Comparable<T>> void handleParsedQueryParameter(
    List<Predicate> predicates,
    CriteriaBuilder builder,
    Expression<T> field,
    List<ParsedQueryParameter<T>> filterValues
  ) {
    if (filterValues!= null && !filterValues.isEmpty()) {
      predicates.add(builder.or(filterValues.stream()
        .map(pqp -> processParsedQueryParameter(builder, field, pqp))
        .toArray(Predicate[]::new)
      ));
    }
  }

  private static <T extends Comparable<T>> Predicate processParsedQueryParameter(CriteriaBuilder builder,
                                                                                 Expression<T> field,
                                                                                 ParsedQueryParameter<T> parsedQueryParameter) {
    final T value = parsedQueryParameter.value();
    return switch (parsedQueryParameter.comparisonType()) {
      case EQ -> builder.equal(field, value);
      case GTE -> builder.greaterThanOrEqualTo(field, value);
      case GT -> builder.greaterThan(field, value);
      case LTE -> builder.lessThanOrEqualTo(field, value);
      case LT -> builder.lessThan(field, value);
    };
  }
  private static <T> void conditionallyAddEqualsFilter(List<Predicate> predicates,
                                                       CriteriaBuilder builder,
                                                       Expression<?> field,
                                                       T filterValue) {
    if (filterValue != null) {
      predicates.add(
        builder.equal(
          field,
          filterValue
        )
      );
    }
  }
    private record JsonPathExpressionBuilder(Root<EventCache> root, CriteriaBuilder builder, String jsonFieldName) {
      public Expression<String> of(String... jsonPathElements) {
        List<Expression<?>> expressions = new ArrayList<>();
        expressions.add(root.get(jsonFieldName));
        for (String e : jsonPathElements) {
          expressions.add(builder.literal(e));
        }
        return builder.function("jsonb_extract_path_text", String.class, expressions.toArray(Expression[]::new));
      }
    }
}
