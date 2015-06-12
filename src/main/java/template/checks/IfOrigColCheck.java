package template.checks;

import java.util.List;
import java.util.logging.Level;

import shared.OperatorOperations;
import shared.Shared;
import data.Record;

// check whether an original column meets a criterion
public class IfOrigColCheck extends SingleCheck{
	IfOrigColCheck(String refCol, String refVal, String operator){
		super(refCol, refVal, operator);
	}


	@Override public boolean conditionMet(Record inputRecord, Record outputRecord, int instanceNr) {
        try {
            // get the value(s) from the inputRecord stored at refCol
            // should we check for e.g. a refCol start / stop index to check repeating group and do something with the instanceNr?
            String value= Shared.cleanString(inputRecord.getValue(refCol));
            // transform the value(s) to a list, splitting the value by ","
            List<String> splitString = OperatorOperations.splitStatement(value, ",", false);
            // perform the actual check
            return handleCheck(splitString);
        } catch (Exception e){
            log.log(Level.SEVERE, "Problem performing IfOrigCol action: {0}",e.getMessage());
        }
        return false;
	}
}
