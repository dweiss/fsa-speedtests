package com.dawidweiss.fsaspeed;

import org.apache.lucene.util.automaton.fst.FST;


/**
 * FSA implementation in <a href="http://morfologik.blogspot.com/">morfologik-stemming</a>
 * (CFSA2 format).
 */
public class LuceneFSATest extends FSATest<FST<Object>>
{
    public LuceneFSATest()
    {
        super(new LuceneFSAProvider(), new LuceneFSATraversal());
    }
}