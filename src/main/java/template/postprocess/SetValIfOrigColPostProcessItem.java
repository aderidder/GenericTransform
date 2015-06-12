package template.postprocess;

import data.Record;
import template.checks.CheckItem;
import template.checks.CheckType;

import java.util.logging.Level;

public class SetValIfOrigColPostProcessItem extends PostProcessItem {
	SetValIfOrigColPostProcessItem(String statement, String newVal, boolean checkPreProcessItemsFirst) throws Exception{
        super(newVal, checkPreProcessItemsFirst);
		checkItem = new CheckItem(CheckType.IFORIGCOL, statement);
	}


//	ValueIfOrigColPostprocessItem(String refCol, String refVal, String newVal, String operator){
//		super(refCol, refVal, newVal);
//		checkItem = CheckItem.createCheckItem(CheckType.IFORIGCOL, refCol, refVal, operator);
//	}

	@Override public void performItemAction(Record outputRecord, int instanceNr, String uid){
		try {

            String changeTo = newVal;
            // $ to tell we're interested in a value stored in the outputrecord and not the literal value
            if(newVal.startsWith("$")) changeTo = outputRecord.getValue(newVal.substring(1));
			outputRecord.setValue(uid, changeTo);
		}  catch (Exception e){
			log.log(Level.SEVERE, "Problem performing SetValIfOrigCol action: {0}",e.getMessage());
		}
	}

	@Override public boolean conditionMet(Record inputRecord, Record outputRecord, int instanceNr) {
		return checkItem.conditionMet(inputRecord, outputRecord, instanceNr);
	}

	private final CheckItem checkItem;
}
