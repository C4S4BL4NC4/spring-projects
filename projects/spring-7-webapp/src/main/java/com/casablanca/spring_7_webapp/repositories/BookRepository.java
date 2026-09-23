package com.casablanca.spring_7_webapp.repositories;

import com.casablanca.spring_7_webapp.domain.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {}
