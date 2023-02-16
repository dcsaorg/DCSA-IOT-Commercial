package org.dcsa.iot.commercial.domain.persistence.repository.unofficial;

import org.dcsa.iot.commercial.domain.persistence.entity.DocumentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface DocumentReferenceRepository extends JpaRepository<DocumentReference, UUID> {
  List<DocumentReference> findAllByEventID(UUID eventID);
}
