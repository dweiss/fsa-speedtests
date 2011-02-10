package com.dawidweiss.fsaspeed;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import morfologik.fsa.CFSA2;
import morfologik.fsa.FSA;
import morfologik.tools.FSABuildTool;

import org.apache.commons.io.FilenameUtils;

/**
 * Morfologik-stemming CFSA2 format.
 */
public class MorfologikCFSA2 implements FSAProvider<FSA>, FSATraversal<FSA>
{
    @Override
    public FSA compile(File file) throws Exception
    {
        File fsaFile = new File(FilenameUtils.removeExtension(file.getAbsolutePath())
            + ".cfsa2");

        if (!fsaFile.exists())
        {
            FSABuildTool.main(new String [] {
                "--format", "cfsa2",
                "--progress",
                "--sorted", 
                "-i", file.getAbsolutePath(), 
                "-o", fsaFile.getAbsolutePath()});
        }

        return FSA.read(new FileInputStream(fsaFile));
    }

    @Override
    public int seek(FSA t, List<byte []> sequences)
    {
        final CFSA2 cfsa2 = (CFSA2) t;

        int matches = 0;
        for (byte [] seq : sequences)
        {
            if (isExactMatch(cfsa2, seq)) {
                matches++;
            }
        }
        return matches;
    }

    private static boolean isExactMatch(CFSA2 fsa, byte [] seq)
    {
        int node = fsa.getRootNode();
        final int end = seq.length;
        for (int i = 0; i < end; i++) {
            final int arc = fsa.getArc(node, seq[i]);
            if (arc != 0) {
                if (fsa.isArcFinal(arc) && i + 1 == end) {
                    /* The automaton has an exact match of the input sequence. */
                    return true;
                }

                if (fsa.isArcTerminal(arc)) {
                    /* The automaton contains a prefix of the input sequence. */
                    break;
                }

                // Make a transition along the arc.
                node = fsa.getEndNode(arc);
            } else {
                // no match.
                break;
            }
        }
        
        return false;
    }

}
