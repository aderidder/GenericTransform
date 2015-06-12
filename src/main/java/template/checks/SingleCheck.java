package template.checks;

import data.Record;
import shared.Logging;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class SingleCheck {
    SingleCheck(String refCol, String refValue, String operator){
        this.refValue = refValue;
        this.refCol = refCol;
        this.operator = operator;
    }

    public abstract boolean conditionMet(Record inputRecord, Record outputRecord, int instanceNr);

    boolean handleCheck(List<String> splitString){
        switch(operator){
            case "!=":
                return conditionMetNotEquals(splitString);
            case "=":
                return conditionMetEquals(splitString);
            case ">":
                return conditionMetGT(splitString);
            case "<":
                return conditionMetLT(splitString);
            default:
                log.log(Level.SEVERE, "{0} not supported. The program supports: !=, =, > and <", operator);
        }
        return false;
    }

    // check for !=
    // in case of multiple outputvalues (e.g. a checkbox field, multi-select it will return true if one condition is met
    private boolean conditionMetNotEquals(List<String> splitString){
        return !splitString.contains(refValue);
    }

    // check for =
    // in case of multiple outputvalues (e.g. a checkbox field, multi-select it will return true if one condition is met
    private boolean conditionMetEquals(List<String> splitString){
        return splitString.contains(refValue);
    }

    // check for >
    // in case of multiple outputvalues (e.g. a checkbox field, multi-select it will return true if one condition is met
    private boolean conditionMetGT(List<String> splitString){
        for(String outputValue:splitString){
            if(outputValue.equalsIgnoreCase("")) return false;
            else if(Double.parseDouble(outputValue) > Double.parseDouble(refValue)) return true;
        }
        return false;
    }

    // check for <
    // in case of multiple outputvalues (e.g. a checkbox field, multi-select it will return true if one condition is met
    private boolean conditionMetLT(List<String> splitString){
        for(String outputValue:splitString){
            if(outputValue.equalsIgnoreCase("")) return false;
            else if(Double.parseDouble(outputValue) < Double.parseDouble(refValue)) return true;
        }
        return false;
    }

    String getRefCol(){
        return refCol;
    }

    private String operator="=";
    final String refCol;
    private final String refValue;
    static final Logger log = Logging.getLogger();
}
