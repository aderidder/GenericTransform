package template.item;

import data.Record;
import java.util.logging.Level;

public class PersonIDItem extends TemplateItem {
	public PersonIDItem(String itemName, String groupID) {
        super(itemName, groupID);
	}

	@Override public void performItemAction(Record inputRecord, Record outputRecord){
		try {
			String value = identifiers.getPersonID(inputRecord);
			outputRecord.setValue(getUID(), value);
			log.log( Level.FINE, "PersonID Action: {0}", value);
		} catch (Exception e){
			log.log(Level.SEVERE, "Problem performing PersonID action: {0}", e.getMessage());
		}
	}

	@Override protected void addFirstLine(String [] splitLine){
		// could do some error detection, e.g. if someone is trying to do some postprocessing
	}
	
	@Override public void addLineItem(String []  templateLine) {
		// could do some error detection, as this shouldn't occur
	}
}
