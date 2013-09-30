/**
 * JSONWriter 30.09.2013
 *
 * @author Philipp Haussleiter
 *
 */
package com.innoq.kantinenbot.writer;

import com.innoq.kantinenbot.Constants;
import com.innoq.kantinenbot.model.Day;
import com.innoq.kantinenbot.model.Location;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Philipp Haußleiter
 */
public class JSONWriter implements KantinenWriter {

    private static final Logger LOGGER = Logger.getLogger(JSONWriter.class.getName());
    private File output;

    public JSONWriter(File outputFolder) {
        File outputFile = new File(outputFolder.getAbsolutePath() + File.separator + "README.json");
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();

            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        this.output = outputFile;
    }

    public void write(Map<String, Day> days) {
        int dayCount = 0;
        int locCount;
        int entCount;
        Day d;
        StringBuilder sb = new StringBuilder("\n{");
        for (String day : Constants.DAYS) {
            locCount = 0;
            d = days.get(day);
            sb.append("\n\t\"").append(day).append("\":{");
            sb.append("\n\t\t\"date\":\"").append(d.date).append("\",");
            for (Iterator<Location> it1 = d.locations.iterator(); it1.hasNext();) {
                entCount = 0;
                Location l = it1.next();
                sb.append("\n\t\t\"").append(l.name).append("\":[");
                for (Iterator<String> it2 = l.entries.iterator(); it2.hasNext();) {
                    String entry = it2.next();
                    sb.append("\n\t\t\t\"").append(entry).append("\"");
                    entCount++;
                    if (entCount < l.entries.size()) {
                        sb.append(",");
                    }
                }
                sb.append("\n\t\t]");
                locCount++;
                if (locCount < d.locations.size()) {
                    sb.append(",");
                }
            }
            sb.append("\n\t}");
            dayCount++;
            if (dayCount < days.size()) {
                sb.append(",");
            }
        }
        sb.append("\n}");
        writeContent(sb.toString());
    }

    private void writeContent(String content) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output.getAbsolutePath()), "UTF-8"));
            writer.write(content);
            System.out.println("\twrote "+output.getAbsolutePath());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }
}
