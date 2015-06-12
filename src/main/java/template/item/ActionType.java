package template.item;

import shared.Logging;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// currently supported actions
public enum ActionType {
    COPY,
    COPYDATE,
	PERSONID,
    NONE,
    STUDYSUBJECTID,
	TRANSLATE,
	TODAY,
    WHENEMPTY;

    // check whether the provided fieldTypeString exists in the fieldTypes list
    public static boolean containsFieldType(String fieldTypeString){
        return fieldTypes.contains(fieldTypeString);
    }

    // get the actionType, based on the fieldTypeString
    public static ActionType getFieldType(String fieldTypeString){
        if(containsFieldType(fieldTypeString)) return ActionType.valueOf(fieldTypeString);
        log.log(Level.SEVERE, "Severe error determining actionType. {0} is not supported. Please select from {1}", new Object[]{fieldTypeString, fieldTypes.toString()});
        return null;
    }

    private static List<String> setupFieldTypes(){
        List<ActionType> tmpList = Arrays.asList(ActionType.values());
        return tmpList.stream().map(ActionType::name).collect(Collectors.toList());
    }

    private static final List<String> fieldTypes = setupFieldTypes();
    private static final Logger log = Logging.getLogger();
}
