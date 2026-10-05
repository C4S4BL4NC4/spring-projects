package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, CustomerDTO> customerMap = new HashMap<>();

    public CustomerServiceImpl() {
        var riki = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Riki Maro")
                .version(0)
                .createdDate(LocalDateTime.now().minusDays(30))
                .lastModifiedDate(LocalDateTime.now().minusHours(30))
                .build();

        var ibra = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Ibrahim Tatlises")
                .version(0)
                .createdDate(LocalDateTime.now().minusDays(60))
                .lastModifiedDate(LocalDateTime.now().minusDays(2))
                .build();

        var carm = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Carmine Berzatto")
                .version(0)
                .createdDate(LocalDateTime.now().minusDays(30))
                .lastModifiedDate(LocalDateTime.now().minusDays(30))
                .build();

        customerMap.put(riki.getId(), riki);
        customerMap.put(ibra.getId(), ibra);
        customerMap.put(carm.getId(), carm);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.debug("getAllCustomers() - in customer service");
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        log.debug("getCustomerById() - in customer service, id = {}", id);
        return Optional.ofNullable(customerMap.get(id));
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        log.debug("createCustomer() - in customer service");
        var now = LocalDateTime.now();
        var newCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name(customerDTO.getName())
                .version(0)
                .createdDate(now)
                .lastModifiedDate(now)
                .build();
        customerMap.put(newCustomer.getId(), newCustomer);
        return newCustomer;
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customerDTO) {
        log.debug("updateCustomer() - in customer service, id = {}", id);
        var existing = customerMap.get(id);
        if (existing == null) {
            return Optional.empty();
        }
        existing.setName(customerDTO.getName());
        existing.setVersion(existing.getVersion() + 1);
        existing.setLastModifiedDate(LocalDateTime.now());
        return Optional.of(existing);
    }

    @Override
    public boolean deleteCustomer(UUID id) {
        log.debug("deleteCustomer() - in customer service, id = {}", id);
        return customerMap.remove(id) != null;
    }
}
