package com.drazendjanic.ebookrepository.ir.searcher.model;

public class HitEBook {

    private Long id;

    private String title;

    private String author;

    private String keywords;

    private String languageName;

    private String filename;

    private String highlights;

    public HitEBook() {

    }

    public HitEBook(Long id, String title, String author, String keywords, String languageName, String filename, String highlights) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.keywords = keywords;
        this.languageName = languageName;
        this.filename = filename;
        this.highlights = highlights;
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

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

}
