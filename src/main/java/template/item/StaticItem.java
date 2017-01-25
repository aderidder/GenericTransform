package template.item;

import data.Record;
import shared.DateOperations;
import template.TemplateIndex;

import java.util.logging.Level;

public class StaticItem extends TemplateItem {
	public StaticItem(String itemName, String groupID) {
        super(itemName, groupID);
	}

	@Override public void performItemAction(Record inputRecord, Record outputRecord){
		try {
			outputRecord.setValue(getUID(), staticValue);
			log.log( Level.FINE, "StaticItem Action: {0}", DateOperations.getNow());
		}   catch (Exception e){
			log.log(Level.SEVERE, "Problem performing StaticItem action for item: {0}. Problem: {1}", new Object[]{itemName, e.getMessage()});
		}
    }

	@Override protected void addFirstLine(String[] splitLine) {
		staticValue = splitLine[TemplateIndex.toValueIndex];
		
	}

	@Override public void addLineItem(String []  templateLine) {
		// nothing
	}

	private String staticValue;
}
