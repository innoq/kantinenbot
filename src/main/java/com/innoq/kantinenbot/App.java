package com.innoq.kantinenbot;

import com.innoq.kantinenbot.reader.BraasReader;
import com.innoq.kantinenbot.model.Day;
import com.innoq.kantinenbot.writer.JSONWriter;
import com.innoq.kantinenbot.writer.KantinenWriter;
import com.innoq.kantinenbot.writer.MDWriter;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Hello Bot!
 *
 */
public class App {

    private final static String EXCEL_PATH = "excel_path";
    private final static String JSON_PATH = "json_path";
    private final static String OUT_PATH = "out_path";
    private Date entryDate = new Date();
    private Map<String, String> paras;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("kantinenbot.jar <dd.mm.yyyy> *.xls -o <outputFolder>");
        } else {
            App app = new App();
            app.paras = app.getParameters(args);
            String path = app.paras.get(EXCEL_PATH);
            File file = new File(path);
            if (file.exists()) {
                app.parse(file);
            }
        }
    }

    private Map<String, String> getParameters(String[] args) {
        int i = 0;
        Map<String, String> localParas = new TreeMap<String, String>();
        for (String p : args) {
            if (p.endsWith("xls")) {
                localParas.put(EXCEL_PATH, p.trim());

            } else if (p.endsWith("json")) {
                localParas.put(JSON_PATH, p.trim());
            } else if ("-o".equals(p.toLowerCase()) && args.length > i) {
                localParas.put(OUT_PATH, args[i + 1]);
            } else {
                try {
                    entryDate = sdf.parse(p);
                } catch (ParseException ex) {
                }
            }
            i++;
        }
        return localParas;
    }

    private void parse(File file) {
        System.out.println("parsing " + file.getAbsolutePath());
        BraasReader conv = new BraasReader(getListing());
        conv.read(file);

        String outputPath = paras.get(OUT_PATH);
        File output = new File(outputPath);
        if (outputPath != null && output.canWrite()) {
            System.out.println("writing to " + outputPath);
            KantinenWriter w1 = new JSONWriter(output);
            w1.write(conv.getDayListing());
            KantinenWriter w2 = new MDWriter(output);
            w2.write(conv.getDayListing());
        }
    }

    private Map<String, Day> getListing() {
        Map<String, Day> dayListing = new TreeMap<String, Day>();
        for (String day : Constants.DAYS) {
            Day d = new Day(day);
            d.date = getNextDate();
            dayListing.put(day, d);
        }
        return dayListing;
    }

    private String getNextDate() {
        String dt;
        Calendar c = Calendar.getInstance();
        c.setTime(entryDate);
        dt = sdf.format(c.getTime());
        c.add(Calendar.DATE, 1);  // number of days to add
        entryDate = c.getTime();
        return dt;
    }
}
