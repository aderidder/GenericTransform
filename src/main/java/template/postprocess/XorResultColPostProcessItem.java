package template.postprocess;

import data.Record;
import template.checks.CheckItem;
import template.checks.CheckType;

import java.util.List;
import java.util.logging.Level;


//Je kan dus bv bij 1 item doen:
//	MyField		XORResultCol	FUDuur=		-1
//				XORResultCol	Artritis<2	-10
//
//Als dan de eerste leeg is wordt deze -1 en als de 2e <2 is, dan wordt die leeg en deze -10
//Als aan beiden wordt voldaan, dan krijg je op dit moment -1,-10

public class XorResultColPostProcessItem extends PostProcessItem {
	XorResultColPostProcessItem(String statement, String newVal, boolean checkPreprocessItemsFirst) throws Exception {
        super(newVal, checkPreprocessItemsFirst);
		checkItem = new CheckItem(CheckType.IFRESULTCOL, statement);
	}

	@Override public void performItemAction(Record outputRecord, int instanceNr, String uid){
		try {
//			clearFields(outputRecord, instanceNr);
//			String curValue = outputRecord.getValue(uid);
//			if (!curValue.equals("")) curValue += ",";
//			outputRecord.setValue(uid, curValue + newVal);

            // clear the field(s) specified as the refColumns
            clearFields(outputRecord, instanceNr);
            // check whether a value is already stored
            String curValue = outputRecord.getValue(uid);
            // if a value exists, add a comma to allow us to add a new value (basically turn it into a multi-select)
            if (!curValue.equals("")) curValue += ",";

            // for the new value
            String changeTo = newVal;
            // $ to tell we're interested in a value stored in the outputrecord and not the literal value
            if(newVal.startsWith("$")) changeTo = outputRecord.getValue(newVal.substring(1));
            outputRecord.setValue(uid, curValue + changeTo);

		} catch (Exception e){
			log.log(Level.SEVERE, "Problem performing XorResultCol action: {0}", e.getMessage());
		}
	}

	@Override public boolean conditionMet(Record inputRecord, Record outputRecord, int instanceNr) {
		return checkItem.conditionMet(inputRecord, outputRecord, instanceNr);
	}

	private void clearFields(Record outputRecord, int instanceNr)throws Exception{
		List<String>refCols = checkItem.getRefCols();
		for(String refCol:refCols) outputRecord.setValue(refCol+instanceNr, "");
	}

//	private final String newVal;
	private final CheckItem checkItem;
}
