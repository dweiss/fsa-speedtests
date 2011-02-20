package com.dawidweiss.fsaspeed;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public class DataSets
{
    /**
     * Return a folder with compiled automata for a given data set.
     */
    public static File compiledFolder(String dataSet)
    {
        return new File("data-sets.compiled" + File.separator + dataSet).getAbsoluteFile();
    }

    /**
     * Read all lines as binary sequences.
     */
    public static List<byte []> readLines(File file) throws IOException
    {
        InputStream is = new BufferedInputStream(new FileInputStream(file));

        ArrayList<byte[]> lines = Lists.newArrayList();
        byte[] buffer = new byte[0];
        int b, pos = 0;
        while ((b = is.read()) != -1) {
            if (b == '\n') {
                if (pos > 0) {
                    lines.add(Arrays.copyOf(buffer, pos));
                    pos = 0;
                }
            } else {
                if (pos >= buffer.length) {
                    buffer = java.util.Arrays.copyOf(buffer, buffer.length + 10);
                }
                buffer[pos++] = (byte) b;
            }
        }

        lines.add(Arrays.copyOf(buffer, pos));
        return lines;
    }
}
