package com.dawidweiss.fsaspeed.lucene;

import java.util.List;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.automaton.fst.FST;
import org.apache.lucene.util.automaton.fst.Util;

import com.dawidweiss.fsaspeed.FSAExactMatcher;

public final class LuceneExactMatcher implements FSAExactMatcher
{
    private FST<Object> fst;

    public LuceneExactMatcher(FST<Object> fst)
    {
        this.fst = fst;
    }

    @Override
    public int seek(List<byte []> sequences) throws Exception
    {
        FST<Object> fst = this.fst;

        final BytesRef term = new BytesRef();
        int matches = 0;
        for (byte [] bytes : sequences)
        {
            term.bytes = bytes;
            term.length = bytes.length;
            if (Util.get(fst, term) != null)
            {
                matches++;
            }
        }

        return matches;
    }
}