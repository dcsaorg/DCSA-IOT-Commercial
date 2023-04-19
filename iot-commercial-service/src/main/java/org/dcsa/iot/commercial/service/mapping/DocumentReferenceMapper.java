package org.dcsa.iot.commercial.service.mapping;


import org.dcsa.iot.commercial.domain.persistence.entity.DocumentReference;
import org.dcsa.iot.commercial.transferobjects.DocumentReferenceTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentReferenceMapper {
  @Mapping(target = "type", source = "documentReferenceType")
  @Mapping(target = "value", source = "documentReferenceValue")
  DocumentReferenceTO toDTO(DocumentReference reference);

  @InheritInverseConfiguration(name = "toDTO")
  DocumentReference fromDTO(DocumentReferenceTO documentReferenceTO);

}

