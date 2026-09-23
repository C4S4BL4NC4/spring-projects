package com.casablanca.spring_7_webapp.bootstrap;

import com.casablanca.spring_7_webapp.domain.Author;
import com.casablanca.spring_7_webapp.domain.Book;
import com.casablanca.spring_7_webapp.domain.Publisher;
import com.casablanca.spring_7_webapp.repositories.AuthorRepository;
import com.casablanca.spring_7_webapp.repositories.BookRepository;
import com.casablanca.spring_7_webapp.repositories.PublisherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    public BootstrapData(
            AuthorRepository authorRepository, BookRepository bookRepository, PublisherRepository publisherRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");

        Book book = new Book();
        book.setTitle("Java Developer");
        book.setIsbn("123456789");

        var savedJohn = authorRepository.save(author);
        var savedJavaDeveloper = bookRepository.save(book);

        Author author2 = new Author();
        author2.setFirstName("Jane");
        author2.setLastName("Doe");

        Book book2 = new Book();
        book2.setTitle("Python Developer");
        book2.setIsbn("987654321");

        var savedJane = authorRepository.save(author2);
        var savedPythonDeveloper = bookRepository.save(book2);

        savedJohn.getBooks().add(book);
        savedJane.getBooks().add(book2);
        savedJavaDeveloper.getAuthors().add(savedJohn);
        savedPythonDeveloper.getAuthors().add(savedJane);

        Publisher penguin = new Publisher();
        penguin.setPublisherName("Penguin Publishing");
        penguin.setCity("London");
        penguin.setCountry("England");
        penguin.setZipCode("07770");

        var savedPublisher = publisherRepository.save(penguin);

        book.setPublisher(savedPublisher);
        book2.setPublisher(savedPublisher);

        authorRepository.save(savedJohn);
        bookRepository.save(savedJavaDeveloper);
        authorRepository.save(savedJane);
        bookRepository.save(savedPythonDeveloper);
        publisherRepository.save(savedPublisher);

        System.out.println("Bootstrapping...");
        System.out.println("Author count: " + authorRepository.count());
        System.out.println("Book count: " + bookRepository.count());
        System.out.println("Publisher count: " + publisherRepository.count());
    }
}
