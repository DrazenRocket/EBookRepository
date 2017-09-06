package com.drazendjanic.ebookrepository.ir.analyzer.filter;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

public class LCFilter extends TokenFilter {

    private CharTermAttribute termAttribute;

    public LCFilter(TokenStream input) {
        super(input);

        termAttribute = (CharTermAttribute)input.addAttribute(CharTermAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (input.incrementToken()) {
            String text = termAttribute.toString();

            termAttribute.setEmpty();
            termAttribute.append(text.toLowerCase());

            return true;
        }

        return false;
    }

}
