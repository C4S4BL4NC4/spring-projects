package com.backend.springrestmvc.mapper;

import com.backend.springrestmvc.entity.Customer;
import com.backend.springrestmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDTO(Customer customer);
}

