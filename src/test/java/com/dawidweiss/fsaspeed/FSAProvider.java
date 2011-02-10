package com.dawidweiss.fsaspeed;

import java.io.File;

/**
 * Provider of FSA compiler and traversal.
 */
public interface FSAProvider<T>
{
    /**
     * Compile a given input file into an FSA. The sequences are '\n'-delimited, but
     * no byte can be ignored (including zero or \r).
     * 
     * <p><b>The provider can (and should) save the automaton in the same folder
     * as the input file, using some provider-specific file suffix and then
     * reuse the existing compiled binary instead of recompiling from scratch.</b></p>
     */
    T compile(File file) throws Exception;
}
