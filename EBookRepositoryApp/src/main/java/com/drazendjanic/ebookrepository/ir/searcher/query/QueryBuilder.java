package com.drazendjanic.ebookrepository.ir.searcher.query;

import com.drazendjanic.ebookrepository.ir.analyzer.SerbianAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import java.util.StringTokenizer;

public class QueryBuilder {

    private final static Version matchVersion = Version.LUCENE_4_9;

    private static Analyzer analyzer = new SerbianAnalyzer(matchVersion);

    public static Query buildQuery(QueryType queryType, String fieldName, String fieldValue) throws ParseException {
        Query query = null;
        QueryParser queryParser = new QueryParser(matchVersion, fieldName, analyzer);

        fieldName = fieldName.trim();
        fieldValue = fieldValue.trim();

        if (queryType.equals(QueryType.STANDARD)) {
            query = queryParser.parse(fieldValue);
        }
        else if (queryType.equals(QueryType.FUZZY)) {
            Term term = new Term(fieldName, fieldValue);

            query = new FuzzyQuery(term);
        }
        else if (queryType.equals(QueryType.PHRASE)) {
            StringTokenizer tokenizedFieldValue = new StringTokenizer(fieldValue);

            query = new PhraseQuery();

            while (tokenizedFieldValue.hasMoreTokens()) {
                Term term = new Term(fieldName, tokenizedFieldValue.nextToken());

                ((PhraseQuery)query).add(term);
            }
        }

        return query;
    }

}
