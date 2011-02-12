package com.dawidweiss.fsaspeed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.automaton.fst.Builder;
import org.apache.lucene.util.automaton.fst.FST;
import org.apache.lucene.util.automaton.fst.NoOutputs;

public class ParamTweak
{
    volatile static int guard;

    public static void main(String [] args) throws Exception
    {
        int [] steps =
        {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 35, 40, 45,
            50, 55, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190,
            200, 210, 220, 230, 240, 250, 256
        };

        // Load test data.
        List<byte []> seekTerms = FSATest.linesFromFile(new File(
            "data-sets/seek-terms-unit2-20110115.txt"));

        for (int s : steps)
        {
            FST.NUM_ARCS_FIXED_ARRAY = s;

            // Build the FST.
            System.out.print("#> " + s + ", ");
            System.out.flush();

            FST<Object> fst = compile0(new File("data-sets/allterms-20110115.txt"));
            long size = serialize(fst);

            LuceneFSATraversal t = new LuceneFSATraversal();
            for (int i = 0; i < 10; i++) {
                guard = t.seek(fst, seekTerms);
            }

            long start = System.currentTimeMillis();
            for (int i = 0; i < 50; i++) {
                guard = t.seek(fst, seekTerms);
            }
            long end = System.currentTimeMillis();

            System.out.println(size + ", " + (end - start) / 1000.0);
        }
    }

    private static long serialize(FST<Object> fst) throws Exception
    {
        Directory dir = new RAMDirectory();
        IndexOutput out = dir.createOutput("fst.bin");
        fst.save(out);
        out.close();

        long size = dir.fileLength("fst.bin");
        dir.close();
        return size;
    }

    public static FST<Object> compile0(File file) throws IOException
    {
        final NoOutputs outputs = NoOutputs.getSingleton();
        final Object nothing = outputs.getNoOutput();
        final Builder<Object> b = new Builder<Object>(FST.INPUT_TYPE.BYTE1, 0, 0, true,
            outputs);

        // TODO: this will actually stop and ignore \r, does it occur by itself anywhere?
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
        }
        is.close();

        return b.finish();
    }
}
