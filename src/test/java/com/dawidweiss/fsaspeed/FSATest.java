package com.dawidweiss.fsaspeed;

import static org.junit.Assume.assumeNotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Base class orchestrating the speed test.
 */
public abstract class FSATest<T> extends AbstractBenchmark
{
    private FSAProvider<T> provider;
    private FSATraversal<T> traversal;

    /**
     * @see FSATest#fsas
     */
    private static class Cache<T>
    {
        /**
         * FSA from the monster terms file.
         */
        private T allTermsFSA;

        /**
         * Small extract of the monster terms file.
         */
        private T smallFSA;
    }

    /**
     * This trickery is needed because I don't know how to force JUnit to have a test data
     * initialized per all tests in a single class and NOT have it in static fields.
     */
    private static HashMap<Class<?>, Cache<?>> fsas = Maps.newHashMap();

    /**
     * Cache of preloaded automata.
     */
    private Cache<T> cache;

    /**
     * Seek terms from <tt>seek-terms-unit2-20110115.txt</tt>.
     */
    private static ArrayList<byte []> seekTerms;

    /**
     * 
     */
    public FSATest(FSAProvider<T> provider, FSATraversal<T> traversal)
    {
        this.provider = provider;
        this.traversal = traversal;
    }

    /**
     * 
     */
    @BeforeClass
    public static void prepareSeekTerms() throws Exception
    {
        seekTerms = linesFromFile(new File("data-sets/seek-terms-unit2-20110115.txt"));
    }

    /**
     * Compile the inputs into an FSA.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void prepare() throws Exception
    {
        if (!fsas.containsKey(this.getClass())) {
            fsas.put(this.getClass(), cache = new Cache<T>());
            cache.allTermsFSA = provider.compile(new File("data-sets/allterms-20110115.txt"));
            cache.smallFSA = provider.compile(new File("data-sets/small.txt"));
        }
        cache = (Cache<T>) fsas.get(this.getClass());
    }

    /**
     * Run traversal speed test using <tt>seek-terms-unit2-20110115.txt</tt>.
     */
    @BenchmarkOptions(callgc = false, warmupRounds = 3, benchmarkRounds = 20)
    @Test
    public void seekTermsTraversal() throws Exception
    {
        assumeNotNull(cache.smallFSA);
        traversal.seek(cache.smallFSA, seekTerms);
    }

    /**
     * Run long test using <tt>allterms-20110115.txt</tt>.
     */
    @BenchmarkOptions(callgc = false, warmupRounds = 3, benchmarkRounds = 20)
    @Test
    public void allTermsTraversal() throws Exception
    {
        assumeNotNull(cache.allTermsFSA);
        traversal.seek(cache.allTermsFSA, seekTerms);
    }

    /**
     * Read all lines from a given file.
     */
    public static ArrayList<byte []> linesFromFile(File file) throws IOException
    {
        final InputStream is = new BufferedInputStream(new FileInputStream(file));
        final ArrayList<byte []> result = Lists.newArrayList();

        byte [] buffer = new byte [0];
        int lines = 0, b, pos = 0;
        while ((b = is.read()) != -1)
        {
            if (b == '\n')
            {
                if (pos > 0)
                {
                    result.add(Arrays.copyOf(buffer, pos));
                    pos = 0;
                }
            }
            else
            {
                if (pos >= buffer.length)
                {
                    buffer = java.util.Arrays.copyOf(buffer, buffer.length + 10);
                }
                buffer[pos++] = (byte) b;
            }
        }

        if (pos > 0)
        {
            result.add(Arrays.copyOf(buffer, pos));
            lines++;
        }

        return result;
    }
}
