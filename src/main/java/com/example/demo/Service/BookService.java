package com.example.demo.Service;

import com.example.demo.ExceptionHandling.ResourceNotFoundException;
import com.example.demo.Model.Book;
import com.example.demo.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id:" + id));
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBookById(int id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book Not found with this id:" + id);
        }
        bookRepository.deleteById(id);
    }

    public Book updateBookById(int id, Book book) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Book not found with id: " + id));

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());

        return bookRepository.save(existingBook);
    }
}
