package com.casablanca.spring_7_webapp.services;

import com.casablanca.spring_7_webapp.domain.Book;

public interface BookService {
    Iterable<Book> findAll();
}
