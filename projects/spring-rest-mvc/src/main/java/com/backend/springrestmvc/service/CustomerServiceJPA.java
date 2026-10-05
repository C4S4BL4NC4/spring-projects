package com.backend.springrestmvc.service;

import com.backend.springrestmvc.mapper.CustomerMapper;
import com.backend.springrestmvc.model.CustomerDTO;
import com.backend.springrestmvc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {

        return Optional.ofNullable(customerMapper.customerToCustomerDTO(customerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        var customer = customerMapper.customerDTOToCustomer(customerDTO);
        var now = LocalDateTime.now();
        customer.setCreatedDate(now);
        customer.setLastModifiedDate(now);
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customerDTO) {
        return customerRepository.findById(id).map(customer -> {
            customer.setName(customerDTO.getName());
            customer.setLastModifiedDate(LocalDateTime.now());
            return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
        });
    }

    @Override
    public boolean deleteCustomer(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
