package com.library.ServiceCatalog.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "books")
public class Book {


    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "book_id")
    private UUID bookId;

    @Column(name = "book_name")
    @NotEmpty(message = "Book name must not be empty.")
    private String bookName;

    @Column(name = "book_author")
    @NotEmpty(message = "Author name must not be empty.")
    private String bookAuthor;

    @Column(name = "book_year")
    @Min(value = 1000, message = "The year of publication of the book must be greater than 1000.")
    private int bookYear;

    @Column(name = "book_publication")
    @NotEmpty(message = "The Publication must not be empty.")
    private String bookPublication;

    @Column(name = "book_language")
    @NotEmpty(message = "The language of the book cannot be empty.")
    private String bookLanguage;

    @Column(name = "book_pieces")
    private int bookPieces;

    @Column(name = "book_image")
    private String bookImage;

    @Column(name = "book_genre")
    @NotEmpty(message = "Book genre cannot be empty.")
    private String bookGenre;

    @Column(name = "book_added_at", updatable = false)
    private LocalDateTime bookAddedAt;

    public Book(String bookName, String bookAuthor, int bookYear,
                String bookPublication, String bookLanguage, int bookPieces,
                String bookImage, String bookGenre, LocalDateTime bookAddedAt) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookYear = bookYear;
        this.bookPublication = bookPublication;
        this.bookLanguage = bookLanguage;
        this.bookPieces = bookPieces;
        this.bookImage = bookImage;
        this.bookGenre = bookGenre;
        this.bookAddedAt = bookAddedAt;
    }

    public Book() {}

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public int getBookYear() {
        return bookYear;
    }

    public void setBookYear(int bookYear) {
        this.bookYear = bookYear;
    }

    public String getBookPublication() {
        return bookPublication;
    }

    public void setBookPublication(String bookPublication) {
        this.bookPublication = bookPublication;
    }

    public String getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(String bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    public int getBookPieces() {
        return bookPieces;
    }

    public void setBookPieces(int bookPieces) {
        this.bookPieces = bookPieces;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(String bookGenre) {
        this.bookGenre = bookGenre;
    }

    public LocalDateTime getBookAddedAt() {
        return bookAddedAt;
    }

    public void setBookAddedAt(LocalDateTime bookAddedAt) {
        this.bookAddedAt = bookAddedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId && bookYear == book.bookYear &&
                Objects.equals(bookName, book.bookName) &&
                Objects.equals(bookAuthor, book.bookAuthor) &&
                Objects.equals(bookPublication, book.bookPublication) &&
                Objects.equals(bookLanguage, book.bookLanguage) &&
                Objects.equals(bookGenre, book.bookGenre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, bookName, bookAuthor, bookYear,
                bookPublication, bookLanguage, bookGenre);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", bookYear=" + bookYear +
                ", bookPublication='" + bookPublication + '\'' +
                ", bookLanguage='" + bookLanguage + '\'' +
                ", bookPieces=" + bookPieces +
                ", bookImage='" + bookImage + '\'' +
                ", bookGenre='" + bookGenre + '\'' +
                ", bookAddedAt=" + bookAddedAt +
                '}';
    }
}
