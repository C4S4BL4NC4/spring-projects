package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        var riri = Customer.builder()
                .id(UUID.randomUUID())
                .name("Riki Maro")
                .version(0)
                .createdDate(LocalDateTime.now().minusDays(30))
                .lastModifiedDate(LocalDateTime.now().minusHours(30))
                .build();
        var ibra = Customer.builder()
                .id(UUID.randomUUID())
                .name("Ibrahim Tatlises")
                .version(0)
                .createdDate(LocalDateTime.now().minusDays(60))
                .lastModifiedDate(LocalDateTime.now().minusDays(2))
                .build();

        var carm = Customer.builder()
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
    public List<Customer> getAllCustomers() {
        log.debug("getAllCustomers() -  in customer service");
        return new ArrayList<>(this.customerMap.values());
    }

    @Override
    public Customer getCustomerById(UUID id) {
        log.debug("getCustomerById() - in customer service");
        return customerMap.get(id);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        log.debug("createCustomer() - in customer service");
        var newCustomer =  Customer.builder()
                .id(UUID.randomUUID())
                .name(customer.getName())
                .version(0)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        this.customerMap.put(newCustomer.getId(), newCustomer);
        return newCustomer;
    }

    @Override
    public void updateCustomer(UUID id, Customer customer) {
        log.debug("updateCustomer() - in customer service");
        var existing =  this.customerMap.get(id);
        // Skip checking
        existing.setName(customer.getName());
        existing.setVersion(existing.getVersion() + 1);
        existing.setLastModifiedDate(LocalDateTime.now());
        this.customerMap.put(existing.getId(), existing);
    }
}
