package com.example.demo.Service;

import com.example.demo.ExceptionHandling.ResourceNotFoundException;
import com.example.demo.Model.Book;
import com.example.demo.Repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        log.info("Fetching all books...");
        List<Book> books = bookRepository.findAll();
        log.info("Retrieved all books. Total books: {}", books.size());
        return books;
    }
    @Cacheable("books")
    public Book getBookById(int id) {
        log.info("Fetching book with id: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with id: {}", id);
                    return new ResourceNotFoundException("Book not found with id:" + id);
                });
    }

    public Book addBook(Book book) {
        log.info("Adding new book: {}", book);
        Book savedBook = bookRepository.save(book);
        log.info("Added new book with ID: {}", savedBook.getId());
        return savedBook;
    }
    @CacheEvict(value = "books", key = "#id")
    public void deleteBookById(int id) {
        if (!bookRepository.existsById(id)) {
            log.warn("Attempt to delete non-existent book with id: {}", id);
            throw new ResourceNotFoundException("Book Not found with this id:" + id);
        }
        log.info("Deleting book with id: {}", id);
        bookRepository.deleteById(id);
        log.info("Deleted book with id: {}", id);
    }
    @CacheEvict(value = "books", key = "#id")
    public Book updateBookById(int id, Book book) {
        log.info("Updating book with id: {}", id);
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with id: {}", id);
                    return new ResourceNotFoundException("Book not found with id: " + id);
                });

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());

        Book updatedBook = bookRepository.save(existingBook);
        log.info("Updated book with id: {} successfully", id);
        return updatedBook;
    }
}
