/**
 * Day 30.09.2013
 *
 * @author Philipp Haussleiter
 *
 */
package com.innoq.kantinenbot.model;

import java.util.Set;
import java.util.TreeSet;

public class Day {

    public String date;
    public String name;
    public Set<Location> locations = new TreeSet<Location>();

    public Day(String name) {
        this.name = name.trim();
    }
}
