package com.challenge.day6;

/**
 * This class represents an orbit, with two objects: the orbiting object and the orbited object.
 * Orbiting object: the object that orbits around the orbited object
 * Orbited object: the object that is orbited by the orbiting object
 *
 * The orbiting object moves around the orbited object
 */
public class Orbit {
    private String orbitingObject;
    private String orbitedObject;

    public Orbit(String orbitingObject, String orbitedObject) {
        this.orbitingObject = orbitingObject;
        this.orbitedObject = orbitedObject;
    }

    public String getOrbitingObject() {
        return orbitingObject;
    }

    public String getOrbitedObject() {
        return orbitedObject;
    }
}
