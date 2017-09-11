package com.drazendjanic.ebookrepository.service;

import com.drazendjanic.ebookrepository.dto.HitEBookDto;
import com.drazendjanic.ebookrepository.dto.MultiFieldSearchDto;
import com.drazendjanic.ebookrepository.dto.SingleFieldSearchDto;
import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.exception.NotFoundException;
import com.drazendjanic.ebookrepository.ir.searcher.model.HitEBook;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IEBookService {

    EBook findEBookById(Long id);

    List<EBook> findAllEBooks();

    List<EBook> findAllEBooksByCategoryId(Long id);

    EBook saveEBook(EBook eBook);

    File createEBookFile() throws IOException;

    File saveEBookFile(byte[] bytes) throws IOException;

    Resource loadEBookFileByEBookId(Long id) throws NotFoundException, IOException;

    void indexEBook(EBook eBook);

    List<HitEBookDto> searchEBooks(SingleFieldSearchDto singleFieldSearchDto);

    List<HitEBook> searchEBooks(MultiFieldSearchDto multiFieldSearchDto);

}
