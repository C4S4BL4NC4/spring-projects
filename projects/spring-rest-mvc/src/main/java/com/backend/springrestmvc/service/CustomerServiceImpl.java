package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        var riri = CustomerDTO.builder()
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

        this.customerMap.put(riri.getId(), riri);
        this.customerMap.put(ibra.getId(), ibra);
        this.customerMap.put(carm.getId(), carm);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.debug("getAllCustomers() -  in customer service");
        return new ArrayList<>(this.customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        log.debug("getCustomerById() - in customer service");
        return Optional.of(customerMap.get(id));
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        log.debug("createCustomer() - in customerDTO service");
        var newCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name(customerDTO.getName())
                .version(0)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        this.customerMap.put(newCustomer.getId(), newCustomer);
        return newCustomer;
    }

    @Override
    public void updateCustomer(UUID id, CustomerDTO customerDTO) {
        log.debug("updateCustomer() - in customerDTO service");
        var existing = this.customerMap.get(id);
        // Skip checking
        existing.setName(customerDTO.getName());
        existing.setVersion(existing.getVersion() + 1);
        existing.setLastModifiedDate(LocalDateTime.now());
        this.customerMap.put(existing.getId(), existing);
    }

    @Override
    public void deleteCustomer(UUID customerId) {
        // Skip checking
        this.customerMap.remove(customerId);
    }
}
