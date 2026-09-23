package com.casablanca.spring_7_webapp.services;

import com.casablanca.spring_7_webapp.domain.Author;

public interface AuthorService {
    Iterable<Author> findAll();
}
