package com.drazendjanic.ebookrepository.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditedEBookDto {

    @NotNull(message = "error.categoryId.NotNull")
    private Long categoryId;

    @NotNull(message = "error.languageId.NotNull")
    private Long languageId;

    @NotNull(message = "error.title.NotNull")
    @Size(min = 1, message = "error.title.Size")
    private String title;

    private String author;

    private String keywords;

    private Integer publicationYear;

    @NotNull(message = "error.filename.NotNull")
    @Size(min = 1, message = "error.filename.Size")
    private String filename;

    private String mime;

    public EditedEBookDto() {

    }

    public EditedEBookDto(Long categoryId, Long languageId, String title, String author, String keywords, Integer publicationYear,
                          String filename, String mime) {
        this.categoryId = categoryId;
        this.languageId = languageId;
        this.title = title;
        this.author = author;
        this.keywords = keywords;
        this.publicationYear = publicationYear;
        this.filename = filename;
        this.mime = mime;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
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

}
