package com.demo.services.impl;

import com.demo.constants.GlobalConstants;
import com.demo.entities.Author;
import com.demo.repositories.AuthorRepository;
import com.demo.services.AuthorService;
import com.demo.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

import static com.demo.constants.GlobalConstants.*;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final FileUtil fileUtil;
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(FileUtil fileUtil, AuthorRepository authorRepository) {
        this.fileUtil = fileUtil;
        this.authorRepository = authorRepository;
    }

    @Override
    public void seedAuthors() throws IOException {
        String[] authorsArray = fileUtil.readFileContent(AUTHOR_FILE_PATH);
            Arrays.stream(authorsArray).forEach(authorName->{
                String[] name = authorName.split("\\s+");
                String firstName= name[0];
                String lastName= name[1];
                Author author = new Author(firstName,lastName);
                authorRepository.save(author);
            });

    }

    @Override
    public long getAuthorsCount() {
        return this.authorRepository.count();
    }

    @Override
    public Author findById(long id) {
        return this.authorRepository.findById(id);
    }
}
