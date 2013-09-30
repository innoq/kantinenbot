/**
 * BraasReader 30.09.2013
 *
 * @author Philipp Haussleiter
 *
 */
package com.innoq.kantinenbot.reader;

import com.innoq.kantinenbot.Constants;
import com.innoq.kantinenbot.model.Day;
import com.innoq.kantinenbot.model.Location;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class BraasReader implements KantinenReader {

    private static final Logger LOGGER = Logger.getLogger(BraasReader.class.getName());
    private Map<String, Day> dayListing = new HashMap<String, Day>();
    private final static String MAPPING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private Properties properties;

    public BraasReader(Map<String, Day> dayListing) {
        this.dayListing = dayListing;
        this.properties = getProperties();
    }

    public static Properties getProperties() {
        Properties props = new Properties();
        props.setProperty(Constants.NAME, "Braas Kantine");
        props.setProperty(Constants.MENU_1, "19");
        props.setProperty(Constants.MENU_2, "25");
        props.setProperty(Constants.MONTAG, "B");
        props.setProperty(Constants.DIENSTAG, "D");
        props.setProperty(Constants.MITTWOCH, "F");
        props.setProperty(Constants.DONNERSTAG, "H");
        props.setProperty(Constants.FREITAG, "J");
        return props;
    }

    public void read(File file) {
        InputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheet("ESS Culinaire");
            String[] menues = {Constants.MENU_1, Constants.MENU_2};
            Day day;
            Location l;
            Row row;
            String value;
            String cellChar;
            int rowIndex, cellIndex;
            for (String key : Constants.DAYS) {
                cellChar = properties.getProperty(key);
                cellIndex = sToI(cellChar);
                day = dayListing.get(key);
                l = new Location(properties.getProperty(Constants.NAME));
                for (String menu : menues) {
                    rowIndex = Integer.parseInt(properties.getProperty(menu, "0")) - 1;
                    row = sheet.getRow(rowIndex);
                    //System.out.println("\t cell: " + cellChar + (cellIndex + 1) + " (" + rowIndex + ":" + cellIndex + ")");
                    value = getValue(row.getCell(cellIndex)).trim();
                    //System.out.println("v: " + value + " " + row.getCell(cellIndex).toString());
                    value += " " + getValue(row.getCell(cellIndex + 1)).trim();
                    value = value.replace("\t", " ").replace("  ", " ").trim();
                    if (value.length() > 0) {
                        value += " EUR";
                    }
                    l.entries.add(value);
                }
                day.locations.add(l);
                dayListing.put(key, day);
            }
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    private int sToI(String value) {
        return MAPPING.indexOf(value.toUpperCase());
    }

    private String getValue(Cell cell) {
        if (cell != null) {
            try {
                return cell.getStringCellValue();
            } catch (java.lang.IllegalStateException ex1) {
                try {
                    DecimalFormat df = new DecimalFormat("###,##0.00");
                    return df.format(cell.getNumericCellValue());
                } catch (java.lang.IllegalStateException ex2) {
                }
            }
        }
        return "";
    }

    public Map<String, Day> getDayListing() {
        return dayListing;
    }
}
