package com.drazendjanic.ebookrepository.dto;

public class HitEBookDto {

    private Long id;

    private String title;

    private String author;

    private String keywords;

    private String highlights;

    private Long categoryId;

    public HitEBookDto() {

    }

    public HitEBookDto(Long id, String title, String author, String keywords, String highlights, Long categoryId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.keywords = keywords;
        this.highlights = highlights;
        this.categoryId = categoryId;
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

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

}
