package com.dawidweiss.fsaspeed;

import java.io.File;

/**
 * Finite state automata (FSA) compiler provider.
 */
public interface FSACompiler
{
    /**
     * Compile a given input file into an FSA and save the result into folder
     * <code>outputDir</code>. The input sequences are '\n'-delimited UTF-8, but <b>no
     * byte can be ignored (including zeros, malformed UTF-8 or other byte values other
     * than \n)</b>.
     */
    void compile(File file, File outputDir) throws Exception;
}
