package com.drazendjanic.ebookrepository.ir.indexer.handler;

import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.exception.IncompleteIndexDocumentException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfHandler extends DocumentHandler {

    @Value("${e-book-repository.file-repository.base-path}")
    private String repositoryPath;

    @Override
    public Document getDocument(EBook eBook) throws IncompleteIndexDocumentException {      // TODO Try to add version with File as parameter type and don't use repositoryPath field
        Document document = new Document();

        document.add(new StringField("id", eBook.getId().toString(), Field.Store.YES));
        document.add(new TextField("filename", eBook.getFilename(), Field.Store.YES));
        document.add(new TextField("language", eBook.getLanguage().getName(), Field.Store.YES));
        document.add(new TextField("title", eBook.getTitle(), Field.Store.YES));

        if (eBook.getAuthor() != null && !eBook.getAuthor().trim().equals("")) {
            document.add(new TextField("author", eBook.getAuthor(), Field.Store.YES));
        }

        if (eBook.getKeywords() != null && !eBook.getKeywords().trim().equals("")) {
            document.add(new TextField("keywords", eBook.getAuthor(), Field.Store.YES));
        }

        try {
            File file = new File(repositoryPath + eBook.getFilename());
            String textContent = getTextContentFromPdf(file);

            document.add(new TextField("textContent", textContent, Field.Store.NO));
        }
        catch (IOException e) {
            throw new IncompleteIndexDocumentException();
        }

        return document;
    }

    private String getTextContentFromPdf(File file) throws IOException {
        String textContent = "";
        PDFParser pdfParser = new PDFParser(new FileInputStream(file));
        pdfParser.parse();
        PDDocument pdf = pdfParser.getPDDocument();
        PDFTextStripper pdfTextStripper = new PDFTextStripper("UTF-8");

        textContent = pdfTextStripper.getText(pdf);
        pdf.close();

        return textContent;
    }

}
