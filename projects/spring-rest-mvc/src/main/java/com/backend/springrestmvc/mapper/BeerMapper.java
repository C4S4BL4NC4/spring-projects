package com.backend.springrestmvc.mapper;

import com.backend.springrestmvc.entity.Beer;
import com.backend.springrestmvc.model.BeerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BeerMapper {
    Beer beerDTOToBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDTO(Beer beer);
}
