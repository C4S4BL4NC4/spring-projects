package com.casablanca.spring_7_webapp.repositories;

import com.casablanca.spring_7_webapp.domain.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {}
