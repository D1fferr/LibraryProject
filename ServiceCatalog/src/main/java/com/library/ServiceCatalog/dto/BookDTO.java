package com.library.ServiceCatalog.dto;

import jakarta.validation.constraints.Size;

import java.util.Objects;

public class BookDTO {
    @Size(min = 1, message = "Book name must not be empty.")
    private String bookName;

    @Size(min = 1, message = "Author name must not be empty.")
    private String bookAuthor;

    private int bookYear;

    @Size(min = 1, message = "The Publication must not be empty.")
    private String bookPublication;

    @Size(min = 1, message = "The language of the book cannot be empty.")
    private String bookLanguage;

    private int bookPieces;

    private String bookImage;

    @Size(min = 1, message = "Book genre cannot be empty.")
    private String bookGenre;

    public BookDTO(String bookName, String bookAuthor, int bookYear, String bookPublication,
                   String bookLanguage, int bookPieces, String bookImage, String bookGenre) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookYear = bookYear;
        this.bookPublication = bookPublication;
        this.bookLanguage = bookLanguage;
        this.bookPieces = bookPieces;
        this.bookImage = bookImage;
        this.bookGenre = bookGenre;
    }
    public BookDTO(){}

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return bookYear == bookDTO.bookYear && bookPieces == bookDTO.bookPieces && Objects.equals(bookName, bookDTO.bookName) && Objects.equals(bookAuthor, bookDTO.bookAuthor) && Objects.equals(bookPublication, bookDTO.bookPublication) && Objects.equals(bookLanguage, bookDTO.bookLanguage) && Objects.equals(bookImage, bookDTO.bookImage) && Objects.equals(bookGenre, bookDTO.bookGenre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookName, bookAuthor, bookYear, bookPublication, bookLanguage, bookPieces, bookImage, bookGenre);
    }
}
