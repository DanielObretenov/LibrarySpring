package com.demo.services.impl;

import com.demo.entities.*;
import com.demo.repositories.BookRepository;
import com.demo.services.AuthorService;
import com.demo.services.BookService;
import com.demo.services.CategoryService;
import com.demo.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.demo.constants.GlobalConstants.*;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final FileUtil fileUtil;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, FileUtil fileUtil, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.fileUtil = fileUtil;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }
    @Override
    public void seedBooks() throws IOException {
        String[] fileContent = this.fileUtil.readFileContent(BOOKS_FILE_PATH);
        for (String bookInfo : fileContent) {
            String[] tokens = bookInfo.split("\\s+");
            Author author = getRandomAuthor();
            EditionType editionType = EditionType.values()[Integer.parseInt(tokens[0])];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDateTime localDate = LocalDateTime.parse(tokens[1],formatter);
            int copies  = Integer.parseInt(tokens[2]);
            BigDecimal price = new BigDecimal(tokens[3]);
            AgeRestriction ageRestriction = AgeRestriction
                    .values()[Integer.parseInt(tokens[4])];
            String title = getTitle(tokens);
            Set<Category> categories = getRandomCategories();
            Book book = new Book(title,editionType,price,copies,localDate,ageRestriction,author,categories);
            this.bookRepository.saveAndFlush(book);
        }
    }

    @Override
    public List<Book> getAllBooksAfter(int year) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDateTime localDateTime = LocalDateTime.parse("31/12/"+year,formatter);
        return this.bookRepository.findAllByReleaseDateAfter(localDateTime);
    }

    private Set<Category> getRandomCategories() {
        HashSet<Category> categories = new HashSet<>();
        Random random =  new Random();
        int categoriesForBookCount = random.nextInt(3) +1;
        for (int i = 0; i < categoriesForBookCount; i++) {
            int randomCategoryId = random
                    .nextInt(this.categoryService.getCategoryCount()) +1;
            try {
                categories.add(this.categoryService.getCategoryById((long) randomCategoryId));
            }catch (EntityNotFoundException e){
                //ignore
            }
        }
        return categories;
    }

    private String getTitle(String[] tokens) {

        StringBuilder sb = new StringBuilder();

        for (int i = 5; i < tokens.length; i++) {
            sb.append(tokens[i])
                    .append(" ");
        }
        return sb.toString();
    }

    private Author getRandomAuthor() {
        Random random = new Random();
        int authorCount = random.nextInt((int) this.authorService.getAuthorsCount()) + 1;
        return this.authorService.findById(authorCount);
    }
}
