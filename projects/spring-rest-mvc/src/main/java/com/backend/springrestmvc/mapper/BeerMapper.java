package com.backend.springrestmvc.mapper;

import com.backend.springrestmvc.entity.Beer;
import com.backend.springrestmvc.entity.BeerAudit;
import com.backend.springrestmvc.model.BeerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BeerMapper {
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "beerOrderLines", ignore = true)
    @Mapping(target = "createdDate", source = "createdAt")
    @Mapping(target = "updateDate", source = "updatedAt")
    Beer beerDTOToBeer(BeerDTO dto);

    @Mapping(target = "createdAt", source = "createdDate")
    @Mapping(target = "updatedAt", source = "updateDate")
    BeerDTO beerToBeerDTO(Beer beer);

    @Mapping(target = "createdDateAudit", ignore = true)
    @Mapping(target = "auditId", ignore = true)
    @Mapping(target = "auditEventType", ignore = true)
    @Mapping(target = "principalName", ignore = true)
    BeerAudit beerToBeerAudit(Beer beer);
}
