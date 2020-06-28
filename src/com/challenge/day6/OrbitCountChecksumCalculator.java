package com.challenge.day6;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class OrbitCountChecksumCalculator {
    private LinkedHashMap<String, String> localOrbits;

    private OrbitCountChecksumCalculator(LinkedHashMap<String, String> localOrbits) {
        this.localOrbits = localOrbits;
    }

    public int calculateChecksum() {
        int checksum = 0;

        Set<String> keySet = localOrbits.keySet();
        for (String s : keySet) {
            checksum += countNumDirectAndIndirectOrbits(s);
        }

        return checksum;
    }

    private int countNumDirectAndIndirectOrbits(String initial) {
        int count = 0;
        String orbited = initial;

        do {
            orbited = localOrbits.get(orbited);
            if (orbited != null)
                count++;
        } while (orbited != null);

        System.out.println(String.format("Num of direct and indirect orbits for %s: %d", initial, count));
        return count;
    }

    public static class Builder {
        private LinkedHashMap<String, String> localOrbits;

        private Builder(LinkedHashMap<String, String> localOrbits) {
            this.localOrbits = localOrbits;
        }

        public static Builder createOrbitCountChecksumCalculator(LinkedHashMap<String, String> localOrbits) {
            return new Builder(localOrbits);
        }

        public OrbitCountChecksumCalculator build() {
            return new OrbitCountChecksumCalculator(localOrbits);
        }
    }
}
