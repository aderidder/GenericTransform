package template.item;

import shared.DateOperations;
import data.Record;

import java.util.logging.Level;

public class TodayItem extends TemplateItem {
	public TodayItem(String itemName, String groupID) {
        super(itemName, groupID);
	}

	@Override public void performItemAction(Record inputRecord, Record outputRecord){
		try {
			outputRecord.setValue(getUID(), DateOperations.getNow());
			log.log( Level.FINE, "Today Action: {0}", DateOperations.getNow());
		}   catch (Exception e){
			log.log(Level.SEVERE, "Problem performing Today action for item: {0}. Problem: {1}", new Object[]{itemName, e.getMessage()});
		}
    }

	@Override protected void addFirstLine(String[] splitLine) {
		// TODO Auto-generated method stub
		
	}

	@Override public void addLineItem(String []  templateLine) {
		// TODO Auto-generated method stub
	}
}
