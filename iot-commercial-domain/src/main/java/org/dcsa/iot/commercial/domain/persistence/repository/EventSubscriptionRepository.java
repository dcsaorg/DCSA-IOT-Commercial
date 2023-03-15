package org.dcsa.iot.commercial.domain.persistence.repository;

import org.dcsa.iot.commercial.domain.persistence.entity.EventSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventSubscriptionRepository extends JpaRepository<EventSubscription, UUID> {}
