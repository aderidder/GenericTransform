package template.checks;

import java.util.List;
import java.util.logging.Level;

import shared.OperatorOperations;
import shared.Shared;
import data.Record;

// check whether a result column meets a certain criterion
public class IfResultColCheck extends SingleCheck{
	IfResultColCheck(String refCol, String refVal, String operator){
		super(refCol, refVal, operator);
	}


	// first try to get the value in the group (instanceNr)
	// if that headerName doesn't exist, return the instance 0 version (no-group)
	private String getValue(Record outputRecord, int instanceNr) throws Exception{
		if(outputRecord.containsKey(refCol + instanceNr))
		   return Shared.cleanString(outputRecord.getValue(refCol + instanceNr));
		else {
			return Shared.cleanString(outputRecord.getValue(refCol+"0"));
		}

	}

	@Override public boolean conditionMet(Record inputRecord, Record outputRecord, int instanceNr) {
		try {
			// get the value(s) from the outputRecord stored at refCol
			String value = getValue(outputRecord, instanceNr);
			// transform the value(s) to a list, splitting the value by ","
			List<String> splitString = OperatorOperations.splitStatement(value, ",", false);
			// perform the actual check
			return handleCheck(splitString);
		}  catch (Exception e){
			log.log(Level.SEVERE, "Problem performing IfResultCol action: {0}, refCol: {1}", new Object[]{e.getMessage(), refCol});
		}
		return false;
	}

}
