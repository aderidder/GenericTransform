package template.item;

import identifiers.identifiers.Identifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.Record;
import shared.Logging;
import shared.Shared;
import template.TemplateIndex;
import template.checks.CheckItem;
import template.postprocess.PostProcessItem;

public abstract class TemplateItem implements Cloneable {
    TemplateItem(String itemName, String groupID) {
        this.itemName = itemName;
        this.setGroupID(groupID);
    }

    protected abstract void addFirstLine(String[] splitLine);
    protected abstract void performItemAction(Record inputRecord, Record outputRecord);
    protected abstract void addLineItem(String[] splitLine);

    String getInputValue(Record inputRecord) throws Exception{
        String value = Shared.cleanString(inputRecord.getValue(dataCol));
        value = Shared.applyPattern(value, pattern);
        return value;
    }


    public final void performAction(Record inputRecord, Record outputRecord) {
        if(conditionMet = conditionMet(inputRecord, outputRecord)){
            performItemAction(inputRecord, outputRecord);
        }
    }

    public final void performPostProcess(Record inputRecord, Record outputRecord) {
        performPostProcessAction(inputRecord, outputRecord);
    }

    public final void addLine(String[] splitLine) {
        try {
            addLineItem(splitLine);
            addCheckItems(splitLine);
            addPostProcessItems(splitLine);
        } catch(Exception e){
            log.log(Level.SEVERE, "Problem adding information to templateItem: {0}."+Shared.newLine+"Problem: {1}", new Object[]{itemName, e.getMessage()});
        }
    }

    // create the templateItem based on the itemType
    public static TemplateItem createTemplateItem(String line, Identifiers identifiers) {
        TemplateItem templateItem;
        String[] splitLine = line.split("\\t");
        String itemName=splitLine[TemplateIndex.itemNameIndex];
        try {
            String groupID = extractType(splitLine);
            String action = splitLine[TemplateIndex.actionTypeIndex];

            // we're using the semicolon to tell the program we wish to apply a pattern
            String[] splitAction = action.split(":");

            String pattern = "";
            if (splitAction.length > 1) pattern = splitAction[1];

            ActionType itemType = ActionType.getFieldType(splitAction[0].toUpperCase());

            switch (itemType) {
                case PERSONID:
                    if (!Shared.getSettings().usePersonID())
                        log.log(Level.SEVERE, "Found a personID action, but no personIDDef was specified in the settings file. Please fix!");
                    templateItem = new PersonIDItem(itemName, groupID);
                    break;
                case STUDYSUBJECTID:
                    templateItem = new StudySubjectIDItem(itemName, groupID);
                    break;
                case EVENTNR:
                    templateItem = new EventNrItem(itemName, groupID);
                    break;
                case TODAY:
                    templateItem = new TodayItem(itemName, groupID);
                    break;
                case TRANSLATE:
                    templateItem = new TranslateItem(itemName, groupID);
                    break;
                case TRANSLATEMULTI:
                    templateItem = new TranslateItemMulti(itemName, groupID);
                    break;
                case TRANSLATECOPY:
                    templateItem = new TranslateCopyItem(itemName, groupID);
                    break;
                case COPY:
                    templateItem = new CopyItem(itemName, groupID);
                    break;
                case COPYDATE:
                    templateItem = new CopyDateItem(itemName, groupID);
                    break;
                case NONE:
                    templateItem = new NoneItem(itemName, groupID);
                    break;
                case WHENEMPTY:
                    templateItem = new WhenEmptyItem(itemName, groupID);
                    break;
                default:
                    return null;
            }
            templateItem.identifiers = identifiers;
            templateItem.setPattern(pattern);
            templateItem.addFirstLine(splitLine);
            templateItem.addCheckItems(splitLine);
            templateItem.addPostProcessItems(splitLine);
            return templateItem;
        } catch (Exception e){
            log.log(Level.SEVERE, "Problem creating templateItem for item: {0}."+Shared.newLine+"Problem: {1}", new Object[]{itemName, e.getMessage()});
        }
        return null;
    }

    void setPattern(String pattern){
        this.pattern = pattern;
    }

    // called for repeating groups
    // each repeat has its own set of templateItems
    public final TemplateItem clone(String dataCol, int groupRepeat) {
        try {
            TemplateItem templateItem = (TemplateItem) super.clone();
            templateItem.setDataCol(dataCol);
            templateItem.setInstanceNr(groupRepeat);
            return templateItem;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    // a repeating group in the Template will have multiple dataColumns, e.g. Comor1, Comor2, Comor3
    // these become the dataColumns list
    // otherwise the list contains a single item
    final void setDataColumns(String[] splitLine) throws Exception{
        try {
            dataCol = Shared.cleanString(splitLine[TemplateIndex.dataColumnsIndex]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new Exception("Problem with the data columns. Please check whether the data columns are provided");
        }
    }

    // try to create check items
    // these are checks that define whether the action should occur
    // e.g. if answeredQuestion original column = No, don't put data in the current column
    final void addCheckItems(String[] splitLine) throws Exception{
        CheckItem checkItem = CheckItem.createCheckItem(splitLine);
        if (checkItem != null) checkItems.add(checkItem);
    }

    // try to create postprocess items
    // these occur after the action
    // e.g. if labValue=-1, labValueNull = -1 and labValue=""
    final void addPostProcessItems(String[] splitLine) throws Exception{
        PostProcessItem postprocessItem = PostProcessItem.createPostProcessItem(splitLine);
        if (postprocessItem != null) postProcessItems.add(postprocessItem);
    }

    final void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public final String getDataColumn(){
        return dataCol;
    }

    public final void setInstanceNr(int instanceNr) {
        this.instanceNr = instanceNr;
    }

    public final boolean isGroupItem() {
        return getGroupID().startsWith("G");
    }

    public final String getGroupID() {
        return groupID;
    }

    // hier
    public final String getUID() {
        if (groupID.equalsIgnoreCase("ID")) return itemName;
        return itemName + instanceNr;
    }

    // return a printable variant of the itemName
    public final String getPrintName() {
        if (groupID.equalsIgnoreCase("ID")) return itemName;
        else if (groupID.equalsIgnoreCase("N")) return itemName;
        // in case of a group, the itemName requires a _G# value
        return itemName + "_G" + (instanceNr + 1);
    }

    // check whether a condition is met
    // when no checkItems exist or when one of the checkItems returns true, the function returns true (so it's "or")
    private boolean conditionMet(Record inputRecord, Record outputRecord) {
        if (checkItems.size() == 0) return true;
        for (CheckItem checkItem : checkItems) {
            // check whether the condition is met
            if (checkItem.conditionMet(inputRecord, outputRecord, instanceNr)) return true;
        }
        return false;
    }

    private void performPostProcessAction(Record inputRecord, Record outputRecord) {
        for (PostProcessItem postProcessItem : postProcessItems) {
            if((postProcessItem.checkPreprocessedFirst()&conditionMet) || !postProcessItem.checkPreprocessedFirst()) {
                if (postProcessItem.conditionMet(inputRecord, outputRecord, instanceNr)) {
                    postProcessItem.performItemAction(outputRecord, instanceNr, getUID());
                }
            }
        }
    }

    private void setDataCol(String dataCol){
        this.dataCol = dataCol;
    }

    private static String extractType(String [] splitLine) throws Exception{
        String type = splitLine[TemplateIndex.groupIDIndex];
        if(type.equalsIgnoreCase("id")||(type.equalsIgnoreCase("n"))) return type;
        Matcher matcher = splitPattern.matcher(type);
        if(matcher.matches()) return type;
        throw new Exception ("Please specify id, n or Gx as type, with the x being an integer");
    }

    private static final Pattern splitPattern = Pattern.compile("((G|g)\\d+)\\s*");

    String pattern;
    String dataCol="";
    private final List<CheckItem> checkItems = new ArrayList<>();
    private final List<PostProcessItem> postProcessItems = new ArrayList<>();
    private int instanceNr = 0; // for datacolumn index
    Identifiers identifiers;
    final String itemName;
    private String groupID;

    private boolean conditionMet;

	static final Logger log = Logging.getLogger();
}
//
//class TemplateChecker{
//    static boolean validLine(String [] splitLine){
//        if(!validType(splitLine[TemplateIndex.groupIDIndex])) return false;
//        return true;
//    }
//
//    static boolean validType(String type){
//        if(type.equalsIgnoreCase("id")||(type.equalsIgnoreCase("n"))) return true;
//        Matcher matcher = splitPattern.matcher(type);
//        if(matcher.matches()) return true;
//        return false;
//    }
//    private static final Pattern splitPattern = Pattern.compile("(G\\d+)\\s*");
//}