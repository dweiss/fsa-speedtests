package com.dawidweiss.fsaspeed;

import java.util.List;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.automaton.fst.FST;
import org.apache.lucene.util.automaton.fst.Util;

public final class LuceneFSATraversal implements FSATraversal<FST<Object>>
{
    public int seek(FST<Object> t, List<byte []> sequences) throws Exception
    {
        final FST<Object> fst = t;

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