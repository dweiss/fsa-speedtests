package com.dawidweiss.fsaspeed;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import morfologik.fsa.FSA;

import com.dawidweiss.fsaspeed.lucene.LuceneExactMatcher;
import com.dawidweiss.fsaspeed.lucene.LuceneFSA;
import com.dawidweiss.fsaspeed.morfologik.MorfologikExactMatcher;
import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;

/**
 * A benchmark comparing seek times for exact sequence matches.
 */
public class SeekExactMatchesBenchmark extends SimpleBenchmark
{
    /**
     * Implementations. Use command-line for filtering.
     */
    @Param
    private FSTImplementations implementation;

    /**
     * Data set names (compiled). Use command-line to override.
     */
    @Param({"allterms-20110115"})
    private String dataSet;

    /**
     * Test sequence files.
     */
    @Param({"data-sets/seek-terms-unit2-20110115.txt"})
    private String testFile;
    
    /**
     * Test sequences.
     */
    private List<byte[]> testSequences;
    
    /**
     * Matcher used for the benchmark.
     */
    private FSAExactMatcher matcher;

    private enum FSTImplementations
    {
        FSA5
        {
            @Override
            public FSAExactMatcher createInstance(String dataSet) throws IOException
            {
                return new MorfologikExactMatcher(FSA.read(new FileInputStream(
                    new File(DataSets.compiledFolder(dataSet), "morfologik.fsa5"))));
            }
        },

        CFSA2
        {
            @Override
            public FSAExactMatcher createInstance(String dataSet) throws IOException
            {
                return new MorfologikExactMatcher(FSA.read(new FileInputStream(
                    new File(DataSets.compiledFolder(dataSet), "morfologik.cfsa2"))));
            }
        },

        LUCENE
        {
            @Override
            public FSAExactMatcher createInstance(String dataSet) throws IOException
            {
                return new LuceneExactMatcher(LuceneFSA.read(
                    DataSets.compiledFolder(dataSet)));
            }
        };

        public abstract FSAExactMatcher createInstance(String dataSet) throws IOException;
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        matcher = implementation.createInstance(dataSet);
        testSequences = DataSets.readLines(new File(testFile));
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
        matcher = null;
    }

    /**
     * Run the benchmark.
     */
    public int timeExactSeeks(int reps) throws Exception
    {
        int seeks = 0;
        for (int i = 0; i < 10 * reps; i++)
        {
            seeks += matcher.seek(testSequences);
        }
        return seeks;
    }

    public static void main(String [] args) throws Exception
    {
        Runner.main(SeekExactMatchesBenchmark.class, new String [] {
            "-Jserver=-server",
            "--timeUnit", "ms",
            // "--debug", "--debug-reps", "1"
        });
    }
}
