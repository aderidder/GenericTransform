package template.item;

import data.Record;
import shared.Shared;

import java.util.logging.Level;

public class EventNrItem extends TemplateItem {
	public EventNrItem(String itemName, String groupID) {
        super(itemName, groupID);
	}

	@Override public void performItemAction(Record inputRecord, Record outputRecord){
		try {
			String value = String.valueOf(Shared.getSettings().getVisitNr());
			outputRecord.setValue(getUID(), value);
			log.log( Level.FINE, "EventNr Action: {0}", value);
		} catch (Exception e){
			log.log(Level.SEVERE, "Problem performing EventNr action: {0}", e.getMessage());
		}
	}

	@Override protected void addFirstLine(String [] splitLine){
		
	}
	
	@Override public void addLineItem(String []  templateLine) {

	}

}
