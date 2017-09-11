package com.drazendjanic.ebookrepository.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SingleFieldSearchDto {

    @NotNull(message = "error.fieldName.NotNull")
    private String fieldName;

    @NotNull(message = "error.fieldValue.NotNull")
    private String fieldValue;

    @NotNull(message = "error.fieldValue.NotNull")
    @Pattern(regexp = "^(STANDARD|PHRASE|FUZZY)$", message = "error.queryType.Pattern")
    private String queryType;

    public SingleFieldSearchDto() {

    }

    public SingleFieldSearchDto(String fieldName, String fieldValue, String queryType) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.queryType = queryType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

}
