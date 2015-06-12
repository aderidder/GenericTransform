package shared;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Class for Date operations
public class DateOperations {

    // Convert a date to an ocDate
    public static LocalDate toOCDate(String aString){
        try{
            String origDate;
            // Split the String to an array, based on the dateSplitter
            String []splitString = aString.split(dateSplitter);
            // if the splitString has length one, it means only a year has been provided
            // as we're converting to a full OCdate, this means we have to add the day and month
            if(splitString.length==1) origDate = splitString[0]+"-1-1";
            // if the length is 3, we have a complete date, which we can simply convert
            else if(splitString.length==3) origDate = LocalDate.parse(aString, fullDateFormatter).format(ocDateFormatter);
            else {
                // if the length is 2, we first check which position contains the year and which position contains the
                // month, and then add the day
                if(splitString[0].length()<splitString[1].length()) origDate = splitString[1]+"-"+splitString[0]+"-1";
                else origDate = splitString[0]+"-"+splitString[1]+"-1";
            }
            // finally we convert the String to a LocalDate
            return LocalDate.parse(origDate, ocDateFormatter);
        } catch(Exception e){
//            if(aString.equalsIgnoreCase("")) log.log( Level.WARNING, "Problem parsing empty date. Returning 1900-01-01");
//            else log.log( Level.WARNING, "Problem parsing date: {0} returning 1900-01-01", aString);
            if(!aString.equalsIgnoreCase("")) log.log( Level.WARNING, "Problem parsing date: {0} returning 1900-01-01", aString);
            return LocalDate.parse("1900-01-01", ocDateFormatter);
        }
    }

    // store the dateSplitter, based on the dateSplitPattern
    // the dateSplitter is used by the toOCDate function
    private static void setDateSplitter(String format){
        Matcher matcher = dateSplitPattern.matcher(format);
        if(matcher.matches()){
            dateSplitter = matcher.group(1);
        }
    }

    // set the dateformat based on the settings provided by the user
    public static void setDateFormat(String format){
        fullDateFormatter = DateTimeFormatter.ofPattern(format);
        setDateSplitter(format);
    }


    public static String getDateFormat(){
        return fullDateFormatter.toString();
    }

    // current date
    public static String getNow(){
        LocalDateTime timePoint = LocalDateTime.now();
        return timePoint.toLocalDate().toString();
    }

    private static final DateTimeFormatter ocDateFormatter= DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final Pattern dateSplitPattern=Pattern.compile(".*(\\W+).*");
    private static DateTimeFormatter fullDateFormatter;
    private static String dateSplitter;
    private static final Logger log = Logging.getLogger();
}
