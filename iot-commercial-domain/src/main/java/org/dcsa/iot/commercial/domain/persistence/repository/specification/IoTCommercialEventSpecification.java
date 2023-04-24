package org.dcsa.iot.commercial.domain.persistence.repository.specification;

import jakarta.persistence.criteria.*;
import lombok.Builder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.iot.commercial.domain.persistence.entity.DocumentReference;
import org.dcsa.iot.commercial.domain.persistence.entity.DocumentReference_;
import org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent;
import org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent_;
import org.dcsa.iot.commercial.domain.valueobjects.enums.DocumentReferenceType;
import org.dcsa.iot.commercial.domain.valueobjects.enums.IoTEventTypeCode;
import org.dcsa.skernel.infrastructure.http.queryparams.ParsedQueryParameter;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.dcsa.iot.commercial.domain.valueobjects.enums.DocumentReferenceType.BKG;
import static org.dcsa.iot.commercial.domain.valueobjects.enums.DocumentReferenceType.CBR;

@Slf4j
@UtilityClass
public class IoTCommercialEventSpecification {

  public static final Set<DocumentReferenceType> CARRIER_BOOKING_REF_TYPES = Set.of(BKG, CBR);

  public record IoTCommercialEventFilters(
    List<ParsedQueryParameter<OffsetDateTime>> eventCreatedDateTime,
    List<ParsedQueryParameter<OffsetDateTime>> eventDateTime,
    Set<IoTEventTypeCode> iotEventTypeCodes,
    String carrierBookingReference,
    String equipmentReference
  ) {
    @Builder
    public IoTCommercialEventFilters { }
  }

  public static Specification<IoTCommercialEvent> withFilters(final IoTCommercialEventFilters filters) {
    log.debug("Searching based on {}", filters);

    return (Root<IoTCommercialEvent> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      handleParsedQueryParameter(
        predicates,
        builder,
        root.get(IoTCommercialEvent_.EVENT_CREATED_DATE_TIME),
        filters.eventCreatedDateTime
      );

      handleParsedQueryParameter(
        predicates,
        builder,
        root.get(IoTCommercialEvent_.EVENT_DATE_TIME),
        filters.eventDateTime
      );

      if (filters.iotEventTypeCodes != null && !filters.iotEventTypeCodes.isEmpty()) {
        predicates.add(
                root.get(IoTCommercialEvent_.IOT_EVENT_TYPE_CODE).in(filters.iotEventTypeCodes.stream().map(Enum::name).toList()));

      }

      if (filters.equipmentReference != null) {
        predicates.add(builder.equal(root.get(IoTCommercialEvent_.EQUIPMENT_REFERENCE), filters.equipmentReference));
      }

      handleDocumentReference(root, query, builder, predicates, CARRIER_BOOKING_REF_TYPES, filters.carrierBookingReference);

      return builder.and(predicates.toArray(Predicate[]::new));
    };
  }

  private static void handleDocumentReference(
          Root<IoTCommercialEvent> root,
          CriteriaQuery<?> query,
          CriteriaBuilder builder,
          List<Predicate> predicates,
          Set<DocumentReferenceType> types,
          String reference
  ) {
    if (reference != null) {
      Subquery<DocumentReference> subQuery = query.subquery(DocumentReference.class);
      Root<DocumentReference> subRoot = subQuery.from(DocumentReference.class);
      subQuery.select(subRoot).where(
              builder.equal(root.get(DocumentReference_.EVENT_ID), subRoot.get(DocumentReference_.EVENT_ID)),
              subRoot.get(DocumentReference_.DOCUMENT_REFERENCE_TYPE).in(types),
              builder.equal(subRoot.get(DocumentReference_.DOCUMENT_REFERENCE_VALUE), reference)
      );
      predicates.add(builder.exists(subQuery));
    }
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

}
