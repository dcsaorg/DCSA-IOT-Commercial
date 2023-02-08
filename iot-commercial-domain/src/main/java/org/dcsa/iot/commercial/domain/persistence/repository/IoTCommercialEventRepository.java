package org.dcsa.iot.commercial.domain.persistence.repository;

import org.dcsa.iot.commercial.domain.persistence.entity.IoTCommercialEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IoTCommercialEventRepository extends JpaRepository<IoTCommercialEvent, UUID> { }
