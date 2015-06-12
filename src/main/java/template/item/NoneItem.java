package template.item;

import data.Record;

import java.util.logging.Level;


public class NoneItem extends TemplateItem {

	public NoneItem(String itemName, String groupID) {
        super(itemName, groupID);
	}

	@Override protected void addFirstLine(String[] splitLine) {
		// nothing 
	}

	@Override public void addLineItem(String []  templateLine) {
		// nothing 
	}

	@Override public void performItemAction(Record inputRecord, Record outputRecord){
        log.log( Level.FINE, "None Action");
	}
}
