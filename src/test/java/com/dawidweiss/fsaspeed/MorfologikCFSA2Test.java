package com.dawidweiss.fsaspeed;

import morfologik.fsa.FSA;


/**
 * FSA implementation in <a href="http://morfologik.blogspot.com/">morfologik-stemming</a>
 * (CFSA2 format).
 */
public class MorfologikCFSA2Test extends FSATest<FSA>
{
    public MorfologikCFSA2Test()
    {
        super(new MorfologikCFSA2(), new MorfologikCFSA2());
    }
}