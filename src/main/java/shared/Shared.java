package shared;

import identifiers.settings.Settings;

import javax.swing.*;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shared {

	public static void setSettings(Settings settings){
		Shared.settings = settings;
	}
	
	public static Settings getSettings(){
		return settings;
	}

	// clean a string, removal of " and trimming
	public static String cleanString(String aString){
        if(aString.equalsIgnoreCase("")) return "";
		String cleanString = aString;
		if(cleanString.endsWith("\"")) cleanString = cleanString.substring(0, cleanString.length()-1);
		if(cleanString.startsWith("\"")) cleanString = cleanString.substring(1);
		return cleanString.trim();
	}

	public static String applyPattern(String value, String stringPattern){
		stringPattern = stringPattern.replace("\\\\", "\\");
		if(!stringPattern.equalsIgnoreCase("")) {
			Pattern pattern = Pattern.compile(stringPattern);
			Matcher matcher = pattern.matcher(value);
			if (matcher.matches()) {
				return matcher.group(1);
			}
			return "";
		}
		return value;
	}

    public static void setLogArea(JTextArea logArea){
        Shared.logArea=logArea;
    }

    public static JTextArea getLogArea(){
        return logArea;
    }

	// Newline character
	public static final String newLine = System.lineSeparator();
    public static final String fileSeparator = File.separator;
	private static Settings settings;
    private static JTextArea logArea;
}

