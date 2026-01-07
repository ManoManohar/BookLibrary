package com.example.demo.Controller;

import com.example.demo.Model.Book;
import com.example.demo.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
    @GetMapping("/getBookById/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id){
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }
    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book savedBook = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }
    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id){
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/updateBook/{id}")
    public ResponseEntity<Book> updateBookById(@RequestBody Book book,@PathVariable int id){
        Book existingBook = bookService.getBookById(id);
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setTitle(book.getTitle());
        Book savedBook = bookService.addBook(existingBook);
        return ResponseEntity.ok(savedBook);
    }
}
