package template.item;

import data.Record;
import template.TemplateIndex;

import java.util.logging.Level;

public class WhenEmptyItem extends TemplateItem {
    public WhenEmptyItem(String itemName, String groupID) {
        super(itemName, groupID);
    }

    @Override protected void addFirstLine(String [] splitLine){
        try {
            setDataColumns(splitLine);
            whenEmptyValue = splitLine[TemplateIndex.toValueIndex];
        } catch(Exception e){
            log.log( Level.SEVERE, "Problem with WhenEmpty Action: {0}", e.getMessage());
        }
    }
    @Override protected void performItemAction(Record inputRecord, Record outputRecord){
        String value ="";
        try {
            // check whether a data column was specified
            if (!dataCol.equalsIgnoreCase("")) {
                // if so, retrieve the value stored
                value = getInputValue(inputRecord);
            }

            // if the value is empty, set our value to the whenEmptyValue
            if (value.equalsIgnoreCase(""))
                outputRecord.setValue(getUID(), whenEmptyValue);
            // if the value is not empty, set our value to the normal value
            else
                outputRecord.setValue(getUID(), whenNormalValue);
            log.log( Level.FINE, "WhenEmpty Action: looking at {0}, value becomes {1}", new Object[]{value, outputRecord.getValue(getUID())});
        } catch (Exception e){
            log.log(Level.SEVERE, "Problem performing WhenEmpty action for item: {0}. Problem: {1}", new Object[]{itemName, e.getMessage()});
        }
    }

    @Override protected void addLineItem(String [] splitLine){
        whenNormalValue = splitLine[TemplateIndex.toValueIndex];
    }

    private String whenEmptyValue;
    private String whenNormalValue ="";
}
