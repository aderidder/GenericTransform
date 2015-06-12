package shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OperatorOperations {
    public static List<String> splitStatement(String statement, String splitter){
        return splitStatement(statement, splitter, true);
    }

    // or should we create a statement item, with postProcess children and checkitem children...
    public static List<String> splitStatement(String statement, String splitter, boolean addEmpty){
        String [] splitString = statement.split(splitter);
        List<String> aList = new ArrayList<>(2);
        Collections.addAll(aList, splitString);
        if(addEmpty && aList.size()<2) aList.add("");
        return aList.stream().map(String::trim).collect(Collectors.toList());
    }

    public static String getAndOperator(){return andOperator;}

    // extract the operator from a String
    // e.g. appel=groen --> =
    public static String getOperator(String line){
        Matcher matcher = operatorPattern.matcher(line);
        if(matcher.matches()){
            return matcher.group(1).trim();
        }
        log.log( Level.SEVERE, "Problem determining operator in line: {0}", line);
        return "";
    }

    // = with lookback to prevent != resulting in =
    private static final Pattern operatorPattern= Pattern.compile(".*\\w+\\s*((?!!)=|>|<|!=).*");
    private static final String andOperator = "&";
    private static final Logger log = Logging.getLogger();
}
