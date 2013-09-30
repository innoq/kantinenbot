/**
 * KantinenWriter 30.09.2013
 *
 * @author Philipp Haussleiter
 *
 */
package com.innoq.kantinenbot.writer;

import com.innoq.kantinenbot.model.Day;
import java.util.Map;

/**
 *
 * @author Philipp Haußleiter
 */
public interface KantinenWriter {
    public void write(Map<String, Day> days);
}
