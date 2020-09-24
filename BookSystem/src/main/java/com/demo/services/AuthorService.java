package com.demo.services;

import com.demo.entities.Author;

import java.io.IOException;
import java.time.LocalDateTime;

public interface AuthorService {
    void seedAuthors() throws IOException;
    long getAuthorsCount();
    Author findById(long id);
}
