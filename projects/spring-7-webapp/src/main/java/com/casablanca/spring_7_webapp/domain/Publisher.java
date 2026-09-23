package com.casablanca.spring_7_webapp.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String publisherName;

    private String city;
    private String country;
    private String zipCode;

    @OneToMany(mappedBy = "publisher")
    private Set<Book> books = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Publisher publisher)) return false;

        return Objects.equals(id, publisher.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Publisher{" + "id="
                + id + ", publisherName='"
                + publisherName + '\'' + ", city='"
                + city + '\'' + ", country='"
                + country + '\'' + ", zipCode="
                + zipCode + ", books="
                + books + '}';
    }
}
