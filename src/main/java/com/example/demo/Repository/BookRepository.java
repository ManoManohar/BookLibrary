package com.example.demo.Repository;

import com.example.demo.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    List<Book> findByAuthor(String author);

    List<Book> findByPriceLessThan(double price);

    List<Book> findByTitleContaining(String keyword);

    List<Book> findByAuthorAndPriceBetween(
            String author, double min, double max);
}
