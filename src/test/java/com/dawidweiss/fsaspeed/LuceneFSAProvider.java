package com.dawidweiss.fsaspeed;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.store.*;
import org.apache.lucene.util.automaton.fst.*;
import org.apache.lucene.util.BytesRef;

public final class LuceneFSAProvider implements FSAProvider<FST<Object>>
{
    @Override
    public FST<Object> compile(File file) throws Exception
    {
        File fsaFile = new File(FilenameUtils.removeExtension(file.getAbsolutePath())
            + ".lucene");

        if (!fsaFile.exists())
        {
            Directory dir = FSDirectory.open(fsaFile);
            IndexOutput out = dir.createOutput("fst.bin");
            FST<Object> fst = compile0(file);
            fst.save(out);
            out.close();
            dir.close();
        }

        Directory dir = FSDirectory.open(fsaFile);
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