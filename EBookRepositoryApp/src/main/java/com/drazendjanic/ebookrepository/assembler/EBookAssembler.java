package com.drazendjanic.ebookrepository.assembler;

import com.drazendjanic.ebookrepository.dto.EditedEBookDto;
import com.drazendjanic.ebookrepository.dto.NewEBookDto;
import com.drazendjanic.ebookrepository.entity.Category;
import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.entity.Language;

public class EBookAssembler {

    public static EBook toEBook(NewEBookDto newEBookDto) {
        EBook eBook = new EBook();
        Category category = new Category();
        Language language = new Language();

        category.setId(newEBookDto.getCategoryId());
        language.setId(newEBookDto.getLanguageId());

        eBook.setCategory(category);
        eBook.setLanguage(language);
        eBook.setTitle(newEBookDto.getTitle());
        eBook.setAuthor(newEBookDto.getAuthor());
        eBook.setKeywords(newEBookDto.getKeywords());
        eBook.setPublicationYear(newEBookDto.getPublicationYear());
        eBook.setFilename(newEBookDto.getFilename());
        eBook.setMime(newEBookDto.getMime());

        return eBook;
    }

    public static EBook toEBook(EditedEBookDto editedEBookDto) {
        EBook eBook = new EBook();
        Category category = new Category();
        Language language = new Language();

        category.setId(editedEBookDto.getCategoryId());
        language.setId(editedEBookDto.getLanguageId());

        eBook.setCategory(category);
        eBook.setLanguage(language);
        eBook.setTitle(editedEBookDto.getTitle());
        eBook.setAuthor(editedEBookDto.getAuthor());
        eBook.setKeywords(editedEBookDto.getKeywords());
        eBook.setPublicationYear(editedEBookDto.getPublicationYear());
        eBook.setFilename(editedEBookDto.getFilename());
        eBook.setMime(editedEBookDto.getMime());

        return eBook;
    }

}
