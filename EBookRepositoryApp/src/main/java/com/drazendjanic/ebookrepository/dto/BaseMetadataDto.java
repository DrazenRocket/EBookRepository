package com.drazendjanic.ebookrepository.dto;

public class BaseMetadataDto {

    private String title;

    private String author;

    private String keywords;

    private String filename;

    public BaseMetadataDto() {

    }

    public BaseMetadataDto(String title, String author, String keywords, String filename) {
        this.title = title;
        this.author = author;
        this.keywords = keywords;
        this.filename = filename;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
