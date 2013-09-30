/**
 * MDWriter 30.09.2013
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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MDWriter implements KantinenWriter {

    private static final Logger LOGGER = Logger.getLogger(MDWriter.class.getName());
    private File output;

    public MDWriter(File outputFolder) {
        File outputFile = new File(outputFolder.getAbsolutePath() + File.separator + "README.md");
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
        Day d;
        StringBuilder sb = new StringBuilder();
        for (String day : Constants.DAYS) {
            d = days.get(day);
            sb.append("\n# ").append(day).append(" (").append(d.date).append(")");
            for (Location l : d.locations) {
                sb.append("\n\n## ").append(l.name);
                for (String entry : l.entries) {
                    sb.append("\n * ").append(entry);
                }
            }
            sb.append("\n\n");
        }
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
