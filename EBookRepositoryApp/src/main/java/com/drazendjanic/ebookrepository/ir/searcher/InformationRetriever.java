package com.drazendjanic.ebookrepository.ir.searcher;

import com.drazendjanic.ebookrepository.ir.analyzer.SerbianAnalyzer;
import com.drazendjanic.ebookrepository.ir.searcher.model.HitEBook;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InformationRetriever {

    private static final Version matchVersion = Version.LUCENE_4_9;

    private Analyzer analyzer = new SerbianAnalyzer(matchVersion);

    private String rawDirectoryPath;

    private String indexDirectoryPath;

    private int maxHits;

    public InformationRetriever(String rawDirectoryPath, String indexDirectoryPath) {
        this(rawDirectoryPath, indexDirectoryPath, 10);
    }

    public InformationRetriever(String rawDirectoryPath, String indexDirectoryPath, int maxHits) {
        this.rawDirectoryPath = rawDirectoryPath;
        this.indexDirectoryPath = indexDirectoryPath;
        this.maxHits = maxHits;
    }

    public String getRawDirectoryPath() {
        return rawDirectoryPath;
    }

    public void setRawDirectoryPath(String rawDirectoryPath) {
        this.rawDirectoryPath = rawDirectoryPath;
    }

    public String getIndexDirectoryPath() {
        return indexDirectoryPath;
    }

    public void setIndexDirectoryPath(String indexDirectoryPath) {
        this.indexDirectoryPath = indexDirectoryPath;
    }

    public int getMaxHits() {
        return maxHits;
    }

    public void setMaxHits(int maxHits) {
        this.maxHits = maxHits;
    }

    public List<HitEBook> getEBooks(Query query, List<String> namesOfFields) {
        List<HitEBook> hitEBooks = new ArrayList<HitEBook>();
        DocumentRetriever documentRetriever = new DocumentRetriever(indexDirectoryPath, maxHits);
        List<Document> hitDocuments = documentRetriever.getDocuments(query, true);  // TODO if query null

        for (Document hitDocument : hitDocuments) {
            HitEBook hitEBook = createHitEBookFromDocument(hitDocument);
            StringBuilder highlightsStringBuilder = new StringBuilder("");

            if (namesOfFields != null) {
                for (String fieldName : namesOfFields) {
                    try (Directory directory = new SimpleFSDirectory(new File(indexDirectoryPath));
                         DirectoryReader directoryReader = DirectoryReader.open(directory)) {
                        Highlighter highlighter = new Highlighter(new QueryScorer(query, directoryReader, fieldName));
                        String fieldValue = hitDocument.get(fieldName);
                        String highlight = highlighter.getBestFragment(analyzer, fieldName, fieldValue);

                        if (highlight != null) {
                            highlightsStringBuilder
                                    .append(fieldName)
                                    .append(": ")
                                    .append(highlight.trim())
                                    .append("...");
                        }
                    }
                    catch (InvalidTokenOffsetsException|IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            hitEBook.setKeywords(highlightsStringBuilder.toString());
            hitEBooks.add(hitEBook);
        }

        return hitEBooks;
    }

    private HitEBook createHitEBookFromDocument(Document document) {
        HitEBook hitEBook = new HitEBook();

        hitEBook.setId(new Long(document.get("id")));
        hitEBook.setTitle(document.get("title"));
        hitEBook.setAuthor(document.get("author"));
        hitEBook.setKeywords(document.get("keywords"));
        hitEBook.setLanguageName(document.get("language"));
        hitEBook.setFilename(document.get("filename"));

        return hitEBook;
    }

}
