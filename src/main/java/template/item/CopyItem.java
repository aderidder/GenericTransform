package template.item;

import data.Record;

import java.util.logging.Level;


public class CopyItem extends TemplateItem {

	public CopyItem(String itemName, String groupID) {
		super(itemName, groupID);
	}

	@Override protected void addFirstLine(String[] splitLine) {
		try {
            setDataColumns(splitLine);
        } catch(Exception e){
            log.log( Level.SEVERE, "Problem with Copy Action: {0}", e.getMessage());
        }
	}

	@Override public void addLineItem(String [] templateLine) {
		// nothing
	}

	@Override public void performItemAction(Record inputRecord, Record outputRecord){
		String value;
		try{
            value = getInputValue(inputRecord);
			outputRecord.setValue(getUID(), value);
			log.log( Level.FINE, "Copy Action: {0}", value);
		}  catch (Exception e){
			log.log(Level.SEVERE, "Problem performing Copy  action for item: {0}. Problem: {1}", new Object[]{itemName, e.getMessage()});
		}
	}
}
