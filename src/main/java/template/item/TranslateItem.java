package template.item;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import shared.OperatorOperations;
import shared.Shared;
import data.Record;
import template.TemplateIndex;

public class TranslateItem extends TemplateItem {
	public TranslateItem(String itemName, String groupID) {
        super(itemName, groupID);
	}

    private String checkKey(String key){
        if (!translateMap.containsKey(key)) {
            log.log(Level.SEVERE, "TranslateItem item: {0}, missing key {1}", new Object[]{itemName, key});
            return null;
        }
        return translateMap.get(key);
    }

	@Override public void performItemAction(Record inputRecord, Record outputRecord){
//		String refCol = getRefCol();
		String value, newValue;
		try {
            value = getInputValue(inputRecord);

            // why are we doing this? are we expecting multiple inputvalues in this case?
			List<String> splitString = OperatorOperations.splitStatement(value, ",", false);

			newValue = splitString.stream().map(this::checkKey).collect(Collectors.joining(", "));
			outputRecord.setValue(getUID(), newValue);
			log.log(Level.FINE, "TranslateItem Action: From {0} to {1}", new Object[]{value, newValue});
		} catch (Exception e){
			log.log(Level.SEVERE, "Problem performing Translate action for item: {0}. Problem: {1}", new Object[]{itemName, e.getMessage()});
		}
	}
	
	@Override protected void addFirstLine(String[] splitLine) {
		try {
            setDataColumns(splitLine);
            addTranslateValue(splitLine);
        } catch(Exception e){
			log.log(Level.SEVERE, "Problem adding information to Translate action for item: {0}. Problem: {1}", new Object[]{itemName, e.getMessage()});
        }
	}
	
	private void addTranslateValue(String [] splitLine) throws Exception{
		if(TemplateIndex.translateFromValueIndex >= splitLine.length) throw new Exception("Missing value for translate from");
		String origValue = splitLine[TemplateIndex.translateFromValueIndex];
		String newValue = Shared.cleanString(splitLine[TemplateIndex.toValueIndex]);
		translateMap.put(origValue, newValue);		
	}

	@Override public void addLineItem(String []  templateLine) {
        try {
		    addTranslateValue(templateLine);
        } catch(Exception e){
			log.log(Level.SEVERE, "Problem adding information to Translate action for item: {0}. Problem: {1}", new Object[]{itemName, e.getMessage()});
        }
	} 

	private final HashMap <String, String> translateMap = new HashMap<>();
}
