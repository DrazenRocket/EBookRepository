package com.drazendjanic.ebookrepository.ir.analyzer;

import com.drazendjanic.ebookrepository.ir.analyzer.filter.CyrillicToLatinFilter;
import com.drazendjanic.ebookrepository.ir.analyzer.filter.LCFilter;
import com.drazendjanic.ebookrepository.ir.analyzer.stemmer.SimpleSerbianStemmer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;

public class SerbianAnalyzer extends Analyzer {

    Version version;

    private static final String[] SERBIAN_STOP_WORDS = {
            "biti", "ne",
            "jesam", "sam", "jesi", "si", "je", "jesmo", "smo", "jeste", "ste", "jesu", "su",
            "nijesam", "nisam", "nijesi", "nisi", "nije", "nijesmo", "nismo", "nijeste", "niste", "nijesu", "nisu",
            "budem", "budeš", "bude", "budemo", "budete", "budu",
            "budes",
            "bih",  "bi", "bismo", "biste", "biše",
            "bise",
            "bio", "bili", "budimo", "budite", "bila", "bilo", "bile",
            "ću", "ćeš", "će", "ćemo", "ćete",
            "neću", "nećeš", "neće", "nećemo", "nećete",
            "cu", "ces", "ce", "cemo", "cete",
            "necu", "neces", "nece", "necemo", "necete",
            "mogu", "možeš", "može", "možemo", "možete",
            "mozes", "moze", "mozemo", "mozete",
            "li", "da"};

    public SerbianAnalyzer(){
        version = Version.LUCENE_4_9;
    }

    public SerbianAnalyzer(Version version) {
        this.version = version;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        TokenStreamComponents tokenStreamComponents = null;
        Tokenizer tokenizer = new StandardTokenizer(version, reader);
        TokenStream tokenStream = new LCFilter(tokenizer);

        tokenStream = new CyrillicToLatinFilter(tokenStream);
        tokenStream = new StopFilter(version, tokenStream, StopFilter.makeStopSet(version, SERBIAN_STOP_WORDS));
        tokenStream = new SnowballFilter(tokenStream, new SimpleSerbianStemmer());
        tokenStream = new ASCIIFoldingFilter(tokenStream);
        tokenStreamComponents = new TokenStreamComponents(tokenizer, tokenStream);

        return tokenStreamComponents;
    }

}
