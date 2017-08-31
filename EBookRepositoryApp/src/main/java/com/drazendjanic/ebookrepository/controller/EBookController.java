package com.drazendjanic.ebookrepository.controller;

import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.entity.User;
import com.drazendjanic.ebookrepository.exception.NotFoundException;
import com.drazendjanic.ebookrepository.service.IEBookService;
import com.drazendjanic.ebookrepository.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        ResponseEntity responseEntity = null;
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

                    responseEntity = ResponseEntity.ok()
                            .contentLength(resource.contentLength())
                            .contentType(MediaType.parseMediaType(eBook.getMime()))
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
