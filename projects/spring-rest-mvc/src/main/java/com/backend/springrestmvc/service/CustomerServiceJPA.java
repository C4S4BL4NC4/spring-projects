package com.backend.springrestmvc.service;

import com.backend.springrestmvc.mapper.CustomerMapper;
import com.backend.springrestmvc.model.CustomerDTO;
import com.backend.springrestmvc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

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

    @Cacheable(cacheNames = "customerListCache")
    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "customerCache", key = "#id")
    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {

        return Optional.ofNullable(customerMapper.customerToCustomerDTO(customerRepository.findById(id)
                .orElse(null)));
    }

    @CacheEvict(cacheNames = "customerListCache", allEntries = true)
    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        var customer = customerMapper.customerDTOToCustomer(customerDTO);
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "customerCache", key = "#id"),
            @CacheEvict(cacheNames = "customerListCache", allEntries = true)
    })
    @Override
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customerDTO) {
        return customerRepository.findById(id).map(customer -> {
            customer.setName(customerDTO.getName());
            return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
        });
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "customerCache", key = "#id"),
            @CacheEvict(cacheNames = "customerListCache", allEntries = true)
    })
    @Override
    public boolean deleteCustomer(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
