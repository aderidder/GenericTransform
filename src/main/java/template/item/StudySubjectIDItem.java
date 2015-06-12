package template.item;

import data.Record;

import java.util.logging.Level;

public class StudySubjectIDItem extends TemplateItem {
	public StudySubjectIDItem(String itemName, String groupID) {
        super(itemName, groupID);
	}

	@Override public void performItemAction(Record inputRecord, Record outputRecord){
		try {
			String value = identifiers.getStudySubjectID(inputRecord);
			outputRecord.setValue(getUID(), value);
			log.log( Level.FINE, "StudySubjectID Action: {0}", value);
		} catch (Exception e){
			log.log(Level.SEVERE, "Problem performing StudySubjectID action: {0}", e.getMessage());
		}
	}

	@Override protected void addFirstLine(String [] splitLine){
		
	}
	
	@Override public void addLineItem(String []  templateLine) {
		// TODO Auto-generated method stub
	}

}
