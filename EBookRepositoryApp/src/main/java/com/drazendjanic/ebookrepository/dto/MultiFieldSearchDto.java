package com.drazendjanic.ebookrepository.dto;

public class MultiFieldSearchDto {

    private String title;

    private String author;

    private String keywords;

    private String content;

    private String language;

    private String queryType;

    private String queryOperator;

    public MultiFieldSearchDto() {

    }

    public MultiFieldSearchDto(String title, String author, String keywords, String content, String language,
                               String queryType, String queryOperator) {
        this.title = title;
        this.author = author;
        this.keywords = keywords;
        this.content = content;
        this.language = language;
        this.queryType = queryType;
        this.queryOperator = queryOperator;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getQueryOperator() {
        return queryOperator;
    }

    public void setQueryOperator(String queryOperator) {
        this.queryOperator = queryOperator;
    }

}
