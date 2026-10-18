package com.java.springreactivemongo.mapper;

import com.java.springreactivemongo.domain.Beer;
import com.java.springreactivemongo.model.BeerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO beerDto);

    BeerDTO beerToBeerDto(Beer beer);
}
