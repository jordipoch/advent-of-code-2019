package com.challenge.day7;

import com.challenge.day7.exception.NoMoreSequencesException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SequencePermutator {
    private long[] sequence;
    private List<Permutation> permutations;
    private Iterator<Permutation> permutationIterator;

    private SequencePermutator(long[] sequence, List<Permutation> permutations) {
        this.sequence = sequence;
        this.permutations = permutations;

        permutationIterator = permutations.iterator();
    }

    public long[] getNextSequence() {
        if (!permutationIterator.hasNext())
            throw new NoMoreSequencesException();

        Permutation permutation = permutationIterator.next();
        ArrayUtils.swap(sequence, permutation.getIndex1(), permutation.getIndex2());
        return(Arrays.copyOf(sequence, sequence.length));
    }

    public boolean hasMoreSequences() {
        return permutationIterator.hasNext();
    }

    public static class Builder {
        long[] sequence;

        private Builder(long[] sequence) {
            this.sequence = sequence;
        }

        public static Builder createSequencePermutator(long[] sequence) {
            if (sequence == null)
                throw new NullPointerException("The sequence can't be a null reference");

            return new Builder(Arrays.copyOf(sequence, sequence.length));
        }

        public SequencePermutator build() {
            List<Permutation> permutations = new ArrayList<>();
            permutations.add(new Permutation(0, 0));

            generatePermutations(sequence.length, permutations);

            //System.out.println(String.format("Permutations list (%d): %s", permutations.size(), permutations));

            return new SequencePermutator(sequence, permutations);
        }

        private void generatePermutations(int k, List<Permutation> permutations) {
            if (k > 1) {
                generatePermutations(k - 1, permutations);

                for (int i = 0; i < k-1; i++) {
                    if (k % 2 == 0)
                        permutations.add(new Permutation(i, k-1));
                    else
                        permutations.add(new Permutation(0, k-1));

                    generatePermutations(k - 1, permutations);
                }
            }
        }
    }

    public static class Permutation {
        private int index1;
        private int index2;

        public Permutation(int index1, int index2) {
            this.index1 = index1;
            this.index2 = index2;
        }

        public int getIndex1() {
            return index1;
        }

        public int getIndex2() {
            return index2;
        }

        @Override
        public String toString() {
            return String.format("[%d, %d]", index1, index2);
        }
    }
}
