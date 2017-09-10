package com.drazendjanic.ebookrepository.controller;

import com.drazendjanic.ebookrepository.assembler.EBookAssembler;
import com.drazendjanic.ebookrepository.dto.BaseMetadataDto;
import com.drazendjanic.ebookrepository.dto.EditedEBookDto;
import com.drazendjanic.ebookrepository.dto.NewEBookDto;
import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.entity.User;
import com.drazendjanic.ebookrepository.exception.NotFoundException;
import com.drazendjanic.ebookrepository.service.IEBookService;
import com.drazendjanic.ebookrepository.service.IUserService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ebooks")
public class EBookController {

    @Autowired
    private IEBookService eBookService;

    @Autowired
    private IUserService userService;

    @GetMapping("")
    public ResponseEntity<List<EBook>> findAllEBooks() {
        ResponseEntity<List<EBook>> responseEntity = null;
        List<EBook> eBooks = eBookService.findAllEBooks();

        eBooks.forEach((eBook) -> eBook.getCataloguer().setPassword(null));
        responseEntity = new ResponseEntity<List<EBook>>(eBooks, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("/{eBookId}")
    public ResponseEntity<EBook> findOneEBookById(@PathVariable Long eBookId) {
        ResponseEntity<EBook> responseEntity = null;
        EBook eBook = eBookService.findEBookById(eBookId);

        if (eBook != null) {
            eBook.getCataloguer().setPassword(null);
            responseEntity = new ResponseEntity<EBook>(eBook, HttpStatus.OK);
        }
        else {
            responseEntity = new ResponseEntity<EBook>(HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    public ResponseEntity<EBook> addEBook(@AuthenticationPrincipal Long authenticatedUserId,
                                          @Validated @RequestBody NewEBookDto newEBookDto) {
        ResponseEntity<EBook> responseEntity = null;
        EBook savedEBook = null;
        EBook newEBook = EBookAssembler.toEBook(newEBookDto);
        User authenticatedUser = new User();

        authenticatedUser.setId(authenticatedUserId);
        newEBook.setCataloguer(authenticatedUser);

        savedEBook = eBookService.saveEBook(newEBook);
        savedEBook.getCataloguer().setPassword(null);
        responseEntity = new ResponseEntity<EBook>(savedEBook, HttpStatus.OK);

        return responseEntity;
    }

    @PutMapping("/{eBookId}")
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> editEBook(@PathVariable Long eBookId,
                                         @Validated @RequestBody EditedEBookDto editedEBookDto) {
        ResponseEntity<Void> responseEntity = null;
        EBook editedEBook = EBookAssembler.toEBook(editedEBookDto);
        EBook eBook = eBookService.findEBookById(eBookId);

        if (eBook != null) {
            eBook.setCategory(editedEBook.getCategory());
            eBook.setLanguage(editedEBook.getLanguage());
            eBook.setTitle(editedEBook.getTitle());
            eBook.setAuthor(editedEBook.getAuthor());
            eBook.setKeywords(editedEBook.getKeywords());
            eBook.setPublicationYear(editedEBook.getPublicationYear());
            eBook.setFilename(editedEBook.getFilename());
            eBook.setMime(editedEBook.getMime());
            eBookService.saveEBook(eBook);

            responseEntity = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        else {
            responseEntity = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    @PostMapping("/files")
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    public ResponseEntity<BaseMetadataDto> addEBookFile(@RequestParam("file") MultipartFile file) {
        ResponseEntity<BaseMetadataDto> responseEntity = null;

        if (!file.isEmpty()) {
            try {
                File eBookFile = eBookService.saveEBookFile(file.getBytes());
                PDDocument document = PDDocument.load(eBookFile);
                PDDocumentInformation documentInformation = document.getDocumentInformation();
                BaseMetadataDto baseMetadataDto = new BaseMetadataDto();

                baseMetadataDto.setTitle(documentInformation.getTitle());
                baseMetadataDto.setAuthor(documentInformation.getAuthor());
                baseMetadataDto.setKeywords(documentInformation.getKeywords());
                baseMetadataDto.setFilename(eBookFile.getName());
                document.close();

                responseEntity = new ResponseEntity<BaseMetadataDto>(baseMetadataDto, HttpStatus.OK);
            }
            catch (IOException e) {
                responseEntity = new ResponseEntity<BaseMetadataDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            responseEntity = new ResponseEntity<BaseMetadataDto>(HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @GetMapping("/{eBookId}/file")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ROLE_ADMIN', 'ROLE_SUBSCRIBER')")
    public ResponseEntity<Resource> downloadEBookFileByEBookId(@AuthenticationPrincipal Long authenticatedUserId,
                                                               @PathVariable Long eBookId) {
        ResponseEntity<Resource> responseEntity = null;
        EBook eBook = eBookService.findEBookById(eBookId);
        User user = userService.findUserById(authenticatedUserId);

        if (eBook != null) {
            if (user.getType().equals("ROLE_ADMIN") || (user.getType().equals("ROLE_SUBSCRIBER") && (user.getCategory() == null || user.getCategory().getId() == eBook.getCategory().getId()))) {
                try {
                    Resource resource = eBookService.loadEBookFileByEBookId(eBookId);
                    String mime = eBook.getMime();

                    if (eBook.getMime() != null) {
                        mime = "application/pdf";
                    }

                    responseEntity = ResponseEntity.ok()
                            .contentLength(resource.contentLength())
                            .contentType(MediaType.parseMediaType(mime))
                            .body(resource);
                }
                catch (NotFoundException e) {
                    responseEntity = new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
                }
                catch (IOException e) {
                    responseEntity = new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            else {
                responseEntity = new ResponseEntity<Resource>(HttpStatus.FORBIDDEN);
            }
        }
        else {
            responseEntity = new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

}
