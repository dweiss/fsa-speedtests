package com.dawidweiss.fsaspeed.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.automaton.fst.FST;
import org.apache.lucene.util.automaton.fst.NoOutputs;

public class LuceneFSA
{
    /**
     * Reads FST file from a Lucene index.
     */
    public static FST<Object> read(File fsaFile) throws IOException
    {
        Directory dir = FSDirectory.open(new File(fsaFile, "lucene"));
        IndexInput in = dir.openInput("fst.bin");
        try
        {
            return new FST<Object>(in, NoOutputs.getSingleton());
        }
        finally
        {
            in.close();
        }
    }
}
