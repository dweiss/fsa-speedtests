package com.dawidweiss.fsaspeed;

import java.util.List;

/**
 * Provider of FSA compiler and traversal.
 */
public interface FSATraversal<T>
{
    /**
     * Seek for all sequences in <code>t</code>, returning the number of hits. 
     */
    int seek(T t, List<byte[]> sequences) throws Exception;
}
