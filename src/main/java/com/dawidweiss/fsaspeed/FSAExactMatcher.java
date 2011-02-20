package com.dawidweiss.fsaspeed;

import java.util.List;

/**
 * Exact FSA matcher.
 */
public interface FSAExactMatcher
{
    /**
     * Seek for all sequences in <code>t</code>, returning the number of hits.
     * 
     * @return Returns the number of exact matches in <code>sequences</code> found in the
     *         FSA.
     */
    int seek(List<byte []> sequences) throws Exception;
}
