package com.backend.springreactiveexamples.repository;

import com.backend.springreactiveexamples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class PersonRepositoryImpl implements PersonRepository {


    Person john = new Person(0, "John", "Doe");
    Person jane = new Person(1, "Jane", "Doe");
    Person bob = new Person(2, "Bob", "Doe");


    @Override
    public Mono<Person> findById(final Integer id) {
        return findAll().filter(p -> p.id().equals(id)).next();
    }

    @Override
    public Flux<Person> findAll() {
        return
                Flux.just(john, jane, bob);
    }
}
