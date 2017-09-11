package com.drazendjanic.ebookrepository.assembler;

import com.drazendjanic.ebookrepository.dto.HitEBookDto;
import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.ir.searcher.model.HitEBook;

public class HitEBookAssembler {

    public static HitEBookDto toHitEBookDto(HitEBook hitEBook, EBook eBook) {
        HitEBookDto hitEBookDto = new HitEBookDto();

        hitEBookDto.setId(eBook.getId());
        hitEBookDto.setTitle(eBook.getTitle());
        hitEBookDto.setAuthor(eBook.getAuthor());
        hitEBookDto.setKeywords(eBook.getKeywords());
        hitEBookDto.setHighlights(hitEBook.getHighlights());
        hitEBookDto.setCategoryId(eBook.getCategory().getId());

        return hitEBookDto;
    }

}
