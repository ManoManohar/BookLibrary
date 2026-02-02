// java
package com.example.demo;

import com.example.demo.Controller.BookController;
import com.example.demo.Model.Book;
import com.example.demo.Service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class BookControllerTest {
    @Test
    void getAllBooksReturnsListWhenBooksExist() {
        BookService bookService = Mockito.mock(BookService.class);
        BookController controller = new BookController(bookService);

        Book book = Mockito.mock(Book.class);
        List<Book> books = Arrays.asList(book);
        Mockito.when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<Book>> response = controller.getAllBooks();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(books, response.getBody());
    }

    @Test
    void getAllBooksReturnsEmptyListWhenNoBooks() {
        BookService bookService = Mockito.mock(BookService.class);
        BookController controller = new BookController(bookService);
        Mockito.when(bookService.getAllBooks()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Book>> response = controller.getAllBooks();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getBookByIdReturnsBookWhenFound() {
        BookService bookService = Mockito.mock(BookService.class);
        BookController controller = new BookController(bookService);

        Book book = Mockito.mock(Book.class);
        Mockito.when(bookService.getBookById(1)).thenReturn(book);

        ResponseEntity<Book> response = controller.getBookById(1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(book, response.getBody());
    }

    @Test
    void getBookByIdPropagatesWhenServiceThrowsNotFound() {
        BookService bookService = Mockito.mock(BookService.class);
        BookController controller = new BookController(bookService);
        Mockito.when(bookService.getBookById(999)).thenThrow(new NoSuchElementException("not found"));

        Assertions.assertThrows(NoSuchElementException.class, () -> controller.getBookById(999));
    }

    @Test
    void addBookCreatesAndReturnsCreatedBook() {
        BookService bookService = Mockito.mock(BookService.class);
        BookController controller = new BookController(bookService);

        Book input = Mockito.mock(Book.class);
        Book saved = Mockito.mock(Book.class);
        Mockito.when(bookService.addBook(input)).thenReturn(saved);

        ResponseEntity<Book> response = controller.addBook(input);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(saved, response.getBody());
    }

    @Test
    void deleteBookReturnsNoContentWhenBookExists() {
        BookService bookService = Mockito.mock(BookService.class);
        BookController controller = new BookController(bookService);

        Mockito.when(bookService.getBookById(1))
                .thenReturn(Mockito.mock(Book.class));   // 👈 mock object

        ResponseEntity<Void> response = controller.deleteBook(1);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());

        Mockito.verify(bookService).deleteBookById(1);
    }



    @Test
    void deleteBookReturnsNotFoundWhenBookMissing() {
        BookService bookService = Mockito.mock(BookService.class);
        BookController controller = new BookController(bookService);

        Mockito.when(bookService.getBookById(999))
                .thenReturn(null);

        ResponseEntity<Void> response = controller.deleteBook(999);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Mockito.verify(bookService, Mockito.never())
                .deleteBookById(Mockito.anyInt());
    }


    @Test
    void updateBookByIdReturnsUpdatedBookWhenExists() {
        BookService bookService = Mockito.mock(BookService.class);
        BookController controller = new BookController(bookService);

        Book input = Mockito.mock(Book.class);
        Book updated = Mockito.mock(Book.class);
        Mockito.when(bookService.updateBookById(1, input)).thenReturn(updated);

        ResponseEntity<Book> response = controller.updateBookById(1, input);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(updated, response.getBody());
    }

    @Test
    void updateBookByIdPropagatesWhenServiceThrowsNotFound() {
        BookService bookService = Mockito.mock(BookService.class);
        BookController controller = new BookController(bookService);

        Book input = Mockito.mock(Book.class);
        Mockito.when(bookService.updateBookById(999, input)).thenThrow(new NoSuchElementException("not found"));

        Assertions.assertThrows(NoSuchElementException.class, () -> controller.updateBookById(999, input));
    }

}
