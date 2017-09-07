package com.drazendjanic.ebookrepository.ir.indexer.handler;

import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.exception.IncompleteIndexDocumentException;
import org.apache.lucene.document.Document;

public abstract class DocumentHandler {

    public abstract Document getDocument(EBook eBook) throws IncompleteIndexDocumentException;

}
