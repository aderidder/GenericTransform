package template.item;

import shared.DateOperations;
import data.Record;

import java.util.logging.Level;


public class CopyDateItem extends TemplateItem {

	public CopyDateItem(String itemName, String groupID) {
		super(itemName, groupID);
		// TODO Auto-generated constructor stub
	}

	@Override protected void addFirstLine(String[] splitLine) {
        try {
            setDataColumns(splitLine);
        } catch(Exception e){
            log.log( Level.SEVERE, "Problem performing CopyDate action for item: {0}. Problem: {1}", new Object[]{itemName, e.getMessage()});
        }
	}

	@Override public void addLineItem(String [] templateLine) {
		// TODO Auto-generated method stub
		
	}

	@Override public void performItemAction(Record inputRecord, Record outputRecord){
		String value;
//		String refCol = getRefCol();
		try {
            value = getInputValue(inputRecord);
			value = DateOperations.toOCDate(value).toString();
			outputRecord.setValue(getUID(), value);
			log.log( Level.FINE, "CopyDate Action: {0}", value);
		}  catch (Exception e){
			log.log(Level.SEVERE, "Problem performing CopyDate action: {0}, Reference Column: {1}", new Object[]{e.getMessage(),dataCol});
		}
	}
}
