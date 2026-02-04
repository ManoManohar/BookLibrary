package com.example.demo;

import com.example.demo.ExceptionHandling.ResourceNotFoundException;
import com.example.demo.Model.Book;
import com.example.demo.Repository.BookRepository;
import com.example.demo.Service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        Book book1 = new Book(1, "Book 1", "Author 1", 10.0);
        Book book2 = new Book(2, "Book 2", "Author 2", 15.0);
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        // Act
        List<Book> books = bookService.getAllBooks();

        // Assert
        assertNotNull(books);
        assertEquals(2, books.size());
        verify(bookRepository, times(1)).findAll();
    }
    @Test
    void testGetAllBooks_EmptyList() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Book> books = bookService.getAllBooks();

        // Assert
        assertNotNull(books);
        assertTrue(books.isEmpty());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testAddBook_NullBook() {
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            bookService.addBook(null);
        });
        assertEquals("book must not be null", exception.getMessage());
    }

    @Test
    void testUpdateBookById_PartialUpdate() {
        // Arrange
        int bookId = 1;
        Book existingBook = new Book(bookId, "Old Title", "Old Author", 30.0);
        Book updatedBook = new Book(bookId, "Updated Title", null, 0.0);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        // Act
        Book result = bookService.updateBookById(bookId, updatedBook);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Old Author", result.getAuthor()); // Ensure author is not updated
        assertEquals(30.0, result.getPrice()); // Ensure price is not updated
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(existingBook);
    }
    @Test
    void testGetBookById_BookExists() {
        // Arrange
        int bookId = 1;
        Book book = new Book(bookId, "Test Book", "Test Author", 20.0);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        Book result = bookService.getBookById(bookId);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testGetBookById_BookNotFound() {
        // Arrange
        int bookId = 1;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(bookId);
        });
        assertEquals("Book not found with id:" + bookId, exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testAddBook() {
        // Arrange
        Book book = new Book(1, "New Book", "New Author", 25.0);
        when(bookRepository.save(book)).thenReturn(book);

        // Act
        Book result = bookService.addBook(book);

        // Assert
        assertNotNull(result);
        assertEquals("New Book", result.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testDeleteBookById_BookExists() {
        // Arrange
        int bookId = 1;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        // Act
        bookService.deleteBookById(bookId);

        // Assert
        verify(bookRepository, times(1)).existsById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteBookById_BookNotFound() {
        // Arrange
        int bookId = 1;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBookById(bookId);
        });
        assertEquals("Book Not found with this id:" + bookId, exception.getMessage());
        verify(bookRepository, times(1)).existsById(bookId);
    }

    @Test
    void testUpdateBookById_BookExists() {
        // Arrange
        int bookId = 1;
        Book existingBook = new Book(bookId, "Old Title", "Old Author", 30.0);
        Book updatedBook = new Book(bookId, "Updated Title", "Updated Author", 35.0);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);

        // Act
        Book result = bookService.updateBookById(bookId, updatedBook);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Author", result.getAuthor());
        assertEquals(35.0, result.getPrice());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void testUpdateBookById_BookNotFound() {
        // Arrange
        int bookId = 1;
        Book updatedBook = new Book(bookId, "Updated Title", "Updated Author", 35.0);
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.updateBookById(bookId, updatedBook);
        });
        assertEquals("Book not found with id: " + bookId, exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
    }
}
