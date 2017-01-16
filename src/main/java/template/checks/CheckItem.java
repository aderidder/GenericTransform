package template.checks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import data.Record;
import shared.Logging;
import shared.OperatorOperations;
import shared.Shared;
import template.TemplateIndex;

// CheckItem
public class CheckItem {
	public CheckItem(CheckType checkType, String formula) throws Exception{
        this.checkType = checkType;
        createItems(formula);
	}

	public static CheckItem createCheckItem(String [] splitLine) throws Exception{
        // retrieve the index
        int index = TemplateIndex.checkTypeIndex;
        // if the line is smaller than index+1, the line contains no CheckItem (or PostProcessItem)
        if (splitLine.length < index + 1) return null;
        // retrieve the string and check whether it is a checkTypeString
        String checkTypeString = splitLine[index];
        if(checkTypeString.equalsIgnoreCase("")){
            log.log(Level.SEVERE, "There was an issue creating a checkItem: the type is not defined, but an " +
                    "argument/value was provided. \n\tLine with error: {0}",
                    Arrays.asList(splitLine).stream().collect(Collectors.joining(" ")));
            return null;
        }

        CheckType checkType = CheckType.getFieldType(checkTypeString.toUpperCase());
        if (checkType == null){
            return null;
        }

        // check whether too many arguments were provided
        if(splitLine.length>index+2){
            log.log(Level.WARNING, "Warning for item: {0} - {1} requires 2 fields: the Name and the Check. " +
                    "Everything following will be ignored.",
                    new Object[]{splitLine[TemplateIndex.itemNameIndex], checkTypeString});
        }

        return new CheckItem(checkType, splitLine[index + 1]);
	}

    public boolean conditionMet(Record inputRecord, Record outputRecord, int instanceNr){
        for(SingleCheck singleCheck:checks){
            if(!singleCheck.conditionMet(inputRecord, outputRecord, instanceNr)) {
                return false;
            }
        }
        return true;
    }

    public List<String> getRefCols(){
        return checks.stream().map(SingleCheck::getRefCol).collect(Collectors.toList());
    }

    private void createItems(String formula) throws Exception{
        String operator;
        List<String> aList;
        List<String> splitFormula = OperatorOperations.splitStatement(Shared.cleanString(formula), OperatorOperations.getAndOperator(), false);
        for(String part:splitFormula){
            operator = OperatorOperations.getOperator(part);
            if(operator.equalsIgnoreCase("")) {
                throw new Exception("Problem finding an operator in the formula: "+formula);
            }
            aList = OperatorOperations.splitStatement(part, operator);
            checks.add(createCheckItem(aList.get(0), aList.get(1), operator));
        }
    }

    // createCheckItem based on a CheckType, reference column, reference value and an operator
	private SingleCheck createCheckItem(String refCol, String refVal, String operator){
		SingleCheck singleCheck;
		switch(checkType){
			case IFORIGCOL:
				singleCheck = new IfOrigColCheck(refCol, refVal, operator);
				break;
			case IFRESULTCOL:
                singleCheck = new IfResultColCheck(refCol, refVal, operator);
				break;
			default:
                log.log( Level.SEVERE, "createCheckItem: unsupported type: {0}", checkType);
                return null;
		}
		return singleCheck;
	}

    private final CheckType checkType;
    private final List<SingleCheck> checks = new ArrayList<>();
    private static final Logger log = Logging.getLogger();
}
