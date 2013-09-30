/**
 * Location 30.09.2013
 *
 * @author Philipp Haussleiter
 *
 */
package com.innoq.kantinenbot.model;

import java.util.Set;
import java.util.TreeSet;

public class Location implements Comparable<Location> {

    public String name;
    public Set<String> entries = new TreeSet<String>();

    public Location(String name) {
        this.name = name.trim();
    }

    public int compareTo(Location o) {
        return this.name.compareTo(o.name);
    }
}
