package com.challenge.day6;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class OrbitDistanceCalculator {
    private LinkedHashMap<String, String> localOrbits;
    private String origin, destination;

    private OrbitDistanceCalculator(LinkedHashMap<String, String> localOrbits, String origin, String destination) {
        this.localOrbits = localOrbits;
        this.origin = origin;
        this.destination = destination;
    }

    public int calculate() {
        PathToOrigin pathFromOrigin = getPathToOriginObject(origin);
        PathToOrigin pathFromDestination = getPathToOriginObject(destination);

        String nco = getNearestCommonObject(pathFromOrigin, pathFromDestination);

        return pathFromOrigin.getDistanceToObject(nco) + pathFromDestination.getDistanceToObject(nco);
    }

    private PathToOrigin getPathToOriginObject(String object) {
        PathToOrigin pathToOrigin = new PathToOrigin();
        String orbited = localOrbits.get(object);

        while (orbited != null) {
            pathToOrigin.addObject(orbited);
            orbited = localOrbits.get(orbited);
        }

        return pathToOrigin;
    }

    private String getNearestCommonObject(PathToOrigin path1, PathToOrigin path2) {
        Iterator<String> path1Iterator = path1.getIterator();

        while(path1Iterator.hasNext()) {
            String object = path1Iterator.next();
            if (path2.objectExists(object)) return object;
        }

        assert (false) : "No common object found!";
        return null;
    }


    public static class Builder {
        private LinkedHashMap<String, String> localOrbits;
        private String origin, destination;

        private Builder(LinkedHashMap<String, String> localOrbits) {
            this.localOrbits = localOrbits;
        }

        public static OrbitDistanceCalculator.Builder createOrbitDistanceCalculator(LinkedHashMap<String, String> localOrbits) {
            return new OrbitDistanceCalculator.Builder(localOrbits);
        }

        public OrbitDistanceCalculator.Builder withOriginAndDestination(String origin, String destination) {
            this.origin = origin;
            this.destination = destination;

            return this;
        }

        public OrbitDistanceCalculator build() {
            return new OrbitDistanceCalculator(localOrbits, origin, destination);
        }
    }
}
