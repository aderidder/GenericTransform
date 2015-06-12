package template.postprocess;

import data.Record;
import template.checks.CheckItem;
import template.checks.CheckType;

import java.util.logging.Level;

public class SetValIfResultColPostProcessItem extends PostProcessItem {
	SetValIfResultColPostProcessItem(String statement, String newVal, boolean checkPreprocessItemsFirst) throws Exception{
        super(newVal, checkPreprocessItemsFirst);
		checkItem = new CheckItem(CheckType.IFRESULTCOL, statement);
	}

	@Override public void performItemAction(Record outputRecord, int instanceNr, String uid){
		try {
            String changeTo = newVal;

            // $ to tell we're interested in a value stored in the outputrecord and not the literal value
            if(newVal.startsWith("$")) changeTo = outputRecord.getValue(newVal.substring(1)+instanceNr);

            outputRecord.setValue(uid, changeTo);
		}  catch (Exception e){
			log.log(Level.SEVERE, "Problem performing SetValIfResultCol action: {0}", e.getMessage());
		}
	}

	@Override public boolean conditionMet(Record inputRecord, Record outputRecord, int instanceNr) {
		return checkItem.conditionMet(inputRecord, outputRecord, instanceNr);
	}

	private final CheckItem checkItem;
}
