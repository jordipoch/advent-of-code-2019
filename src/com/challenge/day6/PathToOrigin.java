package com.challenge.day6;

import java.util.*;

public class PathToOrigin {
    private List<String> list = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();

    public void addObject(String object) {
        list.add(object);
        map.put(object, object);
    }

    public boolean objectExists(String object) {
        return map.containsKey(object);
    }

    public Iterator<String> getIterator() {
        return list.iterator();
    }

    public int getDistanceToObject(String object) {
        int distance = 0;
        for (String o : list) {
            if (o.equals(object)) break;
            else distance++;
        }

        return distance;
    }
}
