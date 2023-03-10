package org.dcsa.iot.commercial.domain.persistence.repository;

import org.dcsa.iot.commercial.domain.persistence.entity.EventCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventCacheRepository extends JpaRepository<EventCache, UUID>, JpaSpecificationExecutor<EventCache> { }
