package org.dcsa.iot.commercial.domain.persistence.repository;

import org.dcsa.iot.commercial.domain.persistence.entity.EventSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventSubscriptionRepository extends JpaRepository<EventSubscription, UUID> {
  @Modifying
  @Query("UPDATE EventSubscription SET secret = :secret WHERE subscriptionID = :subscriptionID")
  void updateSecret(UUID subscriptionID, byte[] secret);
}
