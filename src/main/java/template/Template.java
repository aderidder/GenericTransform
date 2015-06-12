package template;

import data.InputData;
import identifiers.identifiers.Identifiers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.Record;
import shared.FileOperations;
import shared.Logging;
import shared.Shared;
import template.item.ActionType;
import template.item.TemplateItem;

public class Template {
	public Template(String templateName, Identifiers identifiers){
		this.templateName = templateName;
		readTemplate(identifiers);
//		expandTemplate();
//		createRefs();
	}

	public List<String> getUidList(){
		return uidList;
	}
    public List<String> getHeader(){
        return headerNameList;
    }

	// the engine of the program
	// for each templateItem in our expandedList we perform its action
	// after all actions are performed, perform all postprocess actions
	// finally write the outputRecord to screen for visual feedback
	public void apply(Record inputRecord, Record outputRecord){
		expandedList.stream().forEach(p->p.performAction(inputRecord, outputRecord));
		expandedList.stream().forEach(p->p.performPostProcess(inputRecord, outputRecord));
		if(Logging.writeToScreen()) Shared.getLogArea().append(outputRecord.toString() + Shared.newLine);
	}

	// expandTemplate is called to ensure the repeats of a repeating group have templateItems
	// the template is a list of objects and each object is either a templateItem or a templateItemGroup
	// if the object is a templateItem, add it to the expandedList
	// if the object is a templateItemGroup (repeating group), expand the group and add all items to the expandedList
	public void expandTemplate(InputData inputData){
        for(TemplateItemGroup aGroup:itemGroups.values()){
            aGroup.setMinMax(inputData);
        }


		for(Object aTemplate:template){
			if(aTemplate instanceof TemplateItem) expandedList.add((TemplateItem) aTemplate);
			else{
				TemplateItemGroup templateItemGroup = (TemplateItemGroup) aTemplate;
				expandedList.addAll(templateItemGroup.expandGroup());
			}
		}
        createRefs();
	}

	// create references for the expandedList
	private void createRefs(){
		for(TemplateItem templateItem:expandedList){
            addQuickRef(templateItem);
            addHeaderName(templateItem);
		}
	}

    private void addHeaderName(TemplateItem templateItem){
        String name;
        name = templateItem.getPrintName();
        headerNameList.add(name);
    }

    private void addQuickRef(TemplateItem templateItem){
        String uid;
        uid = templateItem.getUID();
//        quickRef.put(uid, templateItem);
        uidList.add(uid);
    }

    private void readHeader(BufferedReader bufferedReader) throws Exception{
        String line;
        String [] splitLine;

        bufferedReader.mark(1024);
        try {
            while ((line = bufferedReader.readLine()) != null) {
                splitLine = line.split("\\t");
                if(ActionType.containsFieldType(splitLine[TemplateIndex.actionTypeIndex].toUpperCase())) break;
                bufferedReader.mark(0);
            }
        bufferedReader.reset();
        }  catch(Exception e){
            throw new Exception("Exception during readHeader of Template: "+e.getMessage());
        }
    }

	private void readTemplate(Identifiers identifiers){
		BufferedReader bufferedReader = FileOperations.openFileReader(identifiers.getSettings().getTemplateDir()+templateName);
        try {
            // read the header and basically ignore it
            readHeader(bufferedReader);
            // read the body
            readBody(bufferedReader, identifiers);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Problem in readTemplate {0}", e.getMessage());
			e.printStackTrace();
        }
        finally {
            FileOperations.closeFileReader(bufferedReader);
        }
	}

	// read the body of the template file
	private void readBody(BufferedReader bufferedReader, Identifiers identifiers) throws Exception {
		String line;
		String [] splitLine;
		TemplateItem templateItem=null;
        try{
			while((line=bufferedReader.readLine())!=null){
				splitLine = line.split("\\t");
				if(!splitLine[TemplateIndex.itemNameIndex].equalsIgnoreCase("")){
					templateItem = TemplateItem.createTemplateItem(line, identifiers);
					// if the templateItem is a repeating group, add it to the repeating group item
					// otherwise add the item to the template
					if(templateItem.isGroupItem()) addToGroup(templateItem);
					else template.add(templateItem);
				}
				// if splitLine[0] does not contain data, the line contains data that should
				// be added to the current templateItem.
				else templateItem.addLine(splitLine);
			}
		} catch(IOException e){
            throw new Exception("Exception during readBody of Template: "+e.getMessage());
		}
	}

	// add item to repeating group
	private void addToGroup(TemplateItem templateItem){
		String groupID = templateItem.getGroupID();
		String dataCol = templateItem.getDataColumn();
		TemplateItemGroup templateItemGroup;
		// if the itemgroup does not exist, create a new one and add it the hashMap and the template
		if(!itemGroups.containsKey(groupID)){
//			if(dataCol.equalsIgnoreCase("")) log.log(Level.SEVERE, "Please make sure the first item in your repeating group is not a None item");
				templateItemGroup = new TemplateItemGroup(dataCol);
				itemGroups.put(groupID, templateItemGroup);
				template.add(templateItemGroup);
		}
		// get the group from the table and add the templateItem to the group
		templateItemGroup = itemGroups.get(groupID);
		if(templateItemGroup.emptyDataCol()) templateItemGroup.setDataCol(dataCol);
		templateItemGroup.addItem(templateItem);
	}

	public String getTemplateName(){
		return templateName;
	}

	// template contains templateItems and templateItemGroups
	private final List<Object> template = new ArrayList<>();
	// expandedList contains only templateItems and is generated by expanding the template
    private final List<TemplateItem> expandedList = new ArrayList<>();
	// contains the uids for the expandedList
    private final List<String> uidList = new ArrayList<>();
	// contains the printable names for the expandedList
    private final List<String> headerNameList = new ArrayList<>();

    private final HashMap <String, TemplateItemGroup> itemGroups = new HashMap<>();
	private final String templateName;

    private static final Logger log = Logging.getLogger();

}
