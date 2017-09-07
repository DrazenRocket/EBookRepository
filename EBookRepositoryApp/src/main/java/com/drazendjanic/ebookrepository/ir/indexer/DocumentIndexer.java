package com.drazendjanic.ebookrepository.ir.indexer;

import com.drazendjanic.ebookrepository.ir.analyzer.SerbianAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

public class DocumentIndexer {

    private static final Version matchVersion = Version.LUCENE_4_9;

    private Analyzer analyzer = new SerbianAnalyzer(matchVersion);

    private IndexWriterConfig indexWriterConfig = new IndexWriterConfig(matchVersion, analyzer);

    private IndexWriter indexWriter;

    private Directory indexDirectory;

    public DocumentIndexer(String path) {
        this(path, false);
    }

    public DocumentIndexer(String path, boolean restart) {
        try {
            indexDirectory = new SimpleFSDirectory(new File(path));

            if (restart) {
                indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
                indexWriter.commit();
                indexWriter.close();
            }
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Invalid path.");
        }
    }

    public IndexWriter getIndexWriter() {
        return indexWriter;
    }

    public Directory getIndexDirectory() {
        return indexDirectory;
    }

    private void openIndexWriter() throws IOException {
        indexWriterConfig = new IndexWriterConfig(matchVersion, analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
    }

    public boolean addDocument(Document document) {
        boolean successful = false;

        try {
            openIndexWriter();
            indexWriter.addDocument(document);
            indexWriter.commit();
            indexWriter.close();

            successful = true;
        }
        catch (IOException e) {
            successful = false;
        }

        return successful;
    }

    public boolean updateDocument(int id, IndexableField... fields) {
        boolean successful = false;

        try {
            DirectoryReader directoryReader = DirectoryReader.open(indexDirectory);
            Document document = directoryReader.document(id);

            successful = updateDocument(document, fields);
            directoryReader.close(); // TODO Check this
        }
        catch (IOException e) {
            successful = true;
        }

        return successful;
    }

    public boolean updateDocument(Document document, IndexableField... fields) {
        boolean successful = false;
        String documentId = document.get("id");

        replaceFields(document, fields);

        try {
            synchronized (this) {
                openIndexWriter();
                indexWriter.updateDocument(new Term("id", documentId), document);
                indexWriter.forceMergeDeletes();
                indexWriter.deleteUnusedFiles();
                indexWriter.commit();
                indexWriter.close();
            }

            successful = true;
        }
        catch (IOException e) {
            successful = false;
        }

        return successful;
    }

    public void replaceFields(Document document, IndexableField... fields) {
        for (IndexableField field : fields) {
            document.removeFields(field.name());
        }
        for (IndexableField field : fields) {
            document.add(field);
        }
    }

    public boolean deleteDocument(int id) {
        boolean successful = false;

        try {
            DirectoryReader directoryReader = DirectoryReader.open(indexDirectory);
            Document document = directoryReader.document(id);

            successful = deleteDocument(document);
            directoryReader.close(); // TODO Check this
        }
        catch (IOException e) {
            successful = false;
        }

        return successful;
    }

    public boolean deleteDocument(Document document) {
        boolean successful = false;

        if (document != null) {
            successful = deleteDocument("id", document.get("id"));
        }

        return successful;
    }

    public boolean deleteDocumentByIdField(String fieldValue) {
        boolean successful = false;

        successful = deleteDocument("id", fieldValue);

        return successful;
    }

    public boolean deleteDocument(String fieldName, String fieldValue) {
        boolean successful = false;
        Term term = new Term(fieldName, fieldValue);

        successful = deleteDocuments(term);

        return successful;
    }

    public boolean deleteDocuments(Term... terms) {
        boolean successful = false;

        try {
            synchronized (this) {
                openIndexWriter();
                indexWriter.deleteDocuments(terms);
                indexWriter.deleteUnusedFiles();
                indexWriter.forceMergeDeletes();
                indexWriter.commit();
                indexWriter.close();
            }

            successful = true;
        }
        catch (IOException e) {
            successful = false;
        }

        return successful;
    }

    public Document[] getAllDocuments() {
        Document[] documents = null;

        try {
            DirectoryReader directoryReader = DirectoryReader.open(indexDirectory);

            documents = new Document[directoryReader.maxDoc()];
            for (int i = 0; i < directoryReader.maxDoc(); i++) {
                documents[i] = directoryReader.document(i);
            }

            directoryReader.close(); // TODO Check this
        }
        catch (IOException e) {
            documents = null;
        }

        return documents;
    }

}
