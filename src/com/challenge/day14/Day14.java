package com.challenge.day14;

import com.challenge.day14.exception.NanofactoryException;
import com.challenge.day14.exception.ReactionReaderException;

import static com.challenge.day14.ReactionReader.readReactionsFromFile;
import static com.challenge.day14.Nanofactory.Builder.createNanofactory;

public class Day14 {
    public static void main(String[] args) {
        try {
            long cost = runDay14Part1();
            System.out.println("Cost = " + cost);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static long runDay14Part1() throws ReactionReaderException, NanofactoryException {
        var nanofactory = createNanofactory()
                .withReactions(readReactionsFromFile("input2.txt"))
                .build();

        return nanofactory.calculateMinimumAmountOfOREToProduce1Fuel();
    }
}
