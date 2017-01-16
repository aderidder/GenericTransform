package template.checks;

import shared.Logging;
import template.postprocess.PostProcessType;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// currently supported checkypes
public enum CheckType {
	IFORIGCOL,
	IFRESULTCOL;

	// check whether the provided fieldTypeString exists in the fieldTypes list
    private static boolean containsFieldType(String fieldTypeString){
        return fieldTypes.contains(fieldTypeString);
    }

	// get the checkType, based on the fieldTypeString
    public static CheckType getFieldType(String fieldTypeString){
		// first check whether the fieldTypeString is a CheckType
		if(!containsFieldType(fieldTypeString)){
			// it it isn't check whether it is a PostProcessType; if it isn't generate a SEVERE log entry
            if(!PostProcessType.containsFieldType(fieldTypeString)) {
				log.log( Level.SEVERE, "Issue creating a CheckType. The provided type {0} doesn't exist", fieldTypeString);
			}
            return null;
        }
		return CheckType.valueOf(fieldTypeString);
	}

	// create a list of CheckType values for easy reference
	private static List<String> setupFieldTypes(){
		List<CheckType> tmpList = Arrays.asList(CheckType.values());
		return tmpList.stream().map(CheckType::name).collect(Collectors.toList());
	}

	private static final List<String> fieldTypes = setupFieldTypes();
    private static final Logger log = Logging.getLogger();
}
