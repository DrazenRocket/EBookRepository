package com.drazendjanic.ebookrepository.ir.searcher;

import com.drazendjanic.ebookrepository.ir.analyzer.SerbianAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentRetriever {

    private static final Version matchVersion = Version.LUCENE_4_9;

    private Analyzer analyzer = new SerbianAnalyzer(matchVersion);

    private String indexDirectoryPath;

    private int maxHits;

    public DocumentRetriever(String indexDirectoryPath) {
        this(indexDirectoryPath, 10);
    }

    public DocumentRetriever(String indexDirectoryPath, int maxHits) {
        this.indexDirectoryPath = indexDirectoryPath;
        this.maxHits = maxHits;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
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

    public List<Document> getDocuments(Query query, boolean analyzeQuery) {
        List<Document> documents = getDocuments(query, analyzeQuery, Sort.INDEXORDER);

        return documents;
    }

    public List<Document> getDocuments(Query query, boolean analyzeQuery, Sort sort) {
        List<Document> documents = new ArrayList<Document>();

        if (query != null) {
            try (Directory indexDirectory = new SimpleFSDirectory(new File(indexDirectoryPath));
                 DirectoryReader directoryReader = DirectoryReader.open(indexDirectory)) {
                IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
                ScoreDoc[] scoreDocs = null;

                if (analyzeQuery) {
                    QueryParser queryParser = new QueryParser(matchVersion, "", analyzer);

                    query = queryParser.parse(query.toString());
                }

                if (sort == null) {
                    sort = Sort.INDEXORDER;
                }

                scoreDocs = indexSearcher.search(query, maxHits, sort).scoreDocs;
                for (ScoreDoc scoreDoc : scoreDocs) {
                    documents.add(indexSearcher.doc(scoreDoc.doc));
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return documents;
    }

}
