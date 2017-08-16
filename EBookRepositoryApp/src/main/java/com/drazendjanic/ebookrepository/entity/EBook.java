package com.drazendjanic.ebookrepository.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "e_book")
public class EBook implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "e_book_id")
    private Long id;

    @Column(name = "e_book_title", nullable = false)
    private String title;

    @Column(name = "e_book_author")
    private String author;

    @Column(name = "e_book_keywords")
    private String keywords;

    @Column(name = "e_book_publication_year")
    private Integer publicationYear;

    @Column(name = "e_book_filename", nullable = false)
    private String filename;

    @Column(name = "e_book_mime")
    private String mime;

    @ManyToOne
    @JoinColumn(name = "e_book_language", referencedColumnName = "language_id", nullable = false)
    private Language language;

    @ManyToOne
    @JoinColumn(name = "e_book_category", referencedColumnName = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "e_book_cataloguer", referencedColumnName = "user_id", nullable = false)
    private User cataloguer;

    public EBook() {

    }

    public EBook(String title, String author, String keywords, Integer publicationYear, String filename, String mime,
                 Language language, Category category, User cataloguer) {
        this.title = title;
        this.author = author;
        this.keywords = keywords;
        this.publicationYear = publicationYear;
        this.filename = filename;
        this.mime = mime;
        this.language = language;
        this.category = category;
        this.cataloguer = cataloguer;
    }

    public EBook(Long id, String title, String author, String keywords, Integer publicationYear, String filename, String mime,
                 Language language, Category category, User cataloguer) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.keywords = keywords;
        this.publicationYear = publicationYear;
        this.filename = filename;
        this.mime = mime;
        this.language = language;
        this.category = category;
        this.cataloguer = cataloguer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getCataloguer() {
        return cataloguer;
    }

    public void setCataloguer(User cataloguer) {
        this.cataloguer = cataloguer;
    }
}
