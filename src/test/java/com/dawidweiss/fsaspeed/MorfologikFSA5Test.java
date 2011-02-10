package com.dawidweiss.fsaspeed;

import morfologik.fsa.FSA;


/**
 * FSA implementation in <a href="http://morfologik.blogspot.com/">morfologik-stemming</a>
 * (CFSA2 format).
 */
public class MorfologikFSA5Test extends FSATest<FSA>
{
    public MorfologikFSA5Test()
    {
        super(new MorfologikFSA5(), new MorfologikFSA5());
    }
}