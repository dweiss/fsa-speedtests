package com.dawidweiss.fsaspeed.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.automaton.fst.Builder;
import org.apache.lucene.util.automaton.fst.FST;
import org.apache.lucene.util.automaton.fst.NoOutputs;

import com.dawidweiss.fsaspeed.DataSets;
import com.dawidweiss.fsaspeed.FSACompiler;

/**
 * 
 */
public final class LuceneFSACompiler implements FSACompiler
{
    public static void main(String [] args) throws Exception
    {
        File output = new File(DataSets.compiledFolder(FilenameUtils.getBaseName(args[0])), "lucene");
        new LuceneFSACompiler().compile(new File(args[0]), output);
    }

    @Override
    public void compile(File input, File fsaFile) throws Exception
    {
        if (fsaFile.exists())
            throw new IOException("Output already exist: " + fsaFile);

        Directory dir = FSDirectory.open(fsaFile);
        IndexOutput out = dir.createOutput("fst.bin");
        FST<Object> fst = compile0(input);
        fst.save(out);
        out.close();
        dir.close();
    }

    public FST<Object> compile0(File file) throws IOException
    {
        final NoOutputs outputs = NoOutputs.getSingleton();
        final Object nothing = outputs.getNoOutput();
        final Builder<Object> b = new Builder<Object>(FST.INPUT_TYPE.BYTE1, 0, 0, true,
            outputs);

        // TODO: this will actually stop and ignore \r, does it occur by itself anywhere?
        int line = 0;
        final BufferedReader is = new BufferedReader(new InputStreamReader(
            new FileInputStream(file), "UTF-8"), 65536);
        final BytesRef term = new BytesRef();
        while (true)
        {
            String w = is.readLine();
            if (w == null)
            {
                break;
            }
            term.copy(w);
            b.add(term, nothing);
            
            if ((line++ % 1000000) == 0)
                System.out.println("Line: " + line);
        }
        is.close();

        return b.finish();
    }
}