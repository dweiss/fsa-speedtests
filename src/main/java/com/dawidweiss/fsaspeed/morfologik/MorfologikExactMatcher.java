package com.dawidweiss.fsaspeed.morfologik;

import java.util.List;

import morfologik.fsa.FSA;

import com.dawidweiss.fsaspeed.FSAExactMatcher;

/**
 * Exact matcher using Morfologik's FSA.
 */
public class MorfologikExactMatcher implements FSAExactMatcher
{
    private final FSA fsa;
    
    public MorfologikExactMatcher(FSA fsa)
    {
        this.fsa = fsa;
    }
    
    @Override
    public int seek(List<byte []> sequences) throws Exception
    {
        final FSA f = fsa;
        int matches = 0;
        for (byte [] seq : sequences)
        {
            if (isExactMatch(f, seq)) {
                matches++;
            }
        }

        return matches;
    }

    private static boolean isExactMatch(FSA fsa, byte [] seq)
    {
        int node = fsa.getRootNode();
        final int end = seq.length;
        for (int i = 0; i < end; i++) {
            final int arc = fsa.getArc(node, seq[i]);
            if (arc != 0) {
                if (i + 1 == end && fsa.isArcFinal(arc)) {
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
