package org.dcsa.iot.commercial.domain.persistence.repository.unofficial;

import org.dcsa.iot.commercial.domain.persistence.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PartyRepository extends JpaRepository<Party, UUID> {}
