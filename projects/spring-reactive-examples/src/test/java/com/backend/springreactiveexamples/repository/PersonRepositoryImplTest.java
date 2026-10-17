package com.backend.springreactiveexamples.repository;

import com.backend.springreactiveexamples.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void testMonoBlock() {
        Mono<Person> personMono = personRepository.findById(1);
        // Blocking
        Person person = personMono.block();
        System.out.println(person.toString());
    }

    @Test
    void testMonoSubscribe() {
        Mono<Person> personMono = personRepository.findById(1);
        //Non-Blocking
        personMono.subscribe(System.out::println);
    }

    @Test
    void testFluxBlock() {
        Flux<Person> personFlux = personRepository.findAll();
        Person person = personFlux.blockFirst();
        System.out.println(person.toString());
    }

    @Test
    void testFluxSubscribe() {
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.subscribe(System.out::println);
    }

    @Test
    void testFluxMap() {
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.map(Person::firstName).subscribe(System.out::println);
    }

    @Test
    void testFilterOnName() {
        personRepository.findAll()
                .filter(p -> p.firstName().equals("John"))
                .subscribe(System.out::println);
    }

    @Test
    void testGetById() {
        Mono<Person> personMono = personRepository
                .findAll()
                .filter(p -> p.id() == 1)
                .next();

        personMono.subscribe(System.out::println);
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 8;
        Mono<Person> personMono = personFlux.filter(p -> p.id() == id).single()
                .doOnError(throwable -> {
                    System.out.println("Error At Flux: " + throwable.getMessage());
                });

        personMono.subscribe(p -> {
            System.out.println(p.toString());
        }, throwable -> {
            System.out.println("Error At Mono: " + throwable.getMessage());
        });
    }

    @Test
    void testGetByIdFound() {
        Mono<Person> personMono = personRepository.findById(2);
        Person person = personMono.block();
        System.out.println(person);
        assertTrue(personMono.hasElement().block());
    }

    @Test
    void testGetByIdNotFound() {
        Mono<Person> personMono = personRepository.findById(6);
        Person person = personMono.block();
        System.out.println(person);
        assertFalse(personMono.hasElement().block());
    }
}