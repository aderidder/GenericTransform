package template;

import data.InputData;
import shared.Logging;
import shared.Shared;
import template.item.TemplateItem;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TemplateItemGroup {
    public TemplateItemGroup(String dataCol){
        setDataCol(dataCol);
    }

    boolean emptyDataCol(){
        return dataCol.equalsIgnoreCase("");
    }

    void setDataCol(String dataCol){
        this.dataCol = Shared.cleanString(dataCol);
    }

    void setMinMax(InputData inputData){
        Matcher matcher = splitPattern.matcher(dataCol);
        if(matcher.matches()){
            groupMin = Integer.parseInt(matcher.group(2).trim());
            String partialName = matcher.group(1);
            setMax(partialName, inputData);
        }
    }

    private void setMax(String partialName, InputData inputData){
        List<String> headerList = inputData.getHeaderList();
        Pattern namePattern = Pattern.compile("(?i)"+partialName+"_(\\d+)");
        for(String headerName:headerList){
            Matcher matcher = namePattern.matcher(headerName);
            if(matcher.matches()){
                int value = Integer.parseInt(matcher.group(1));
                if(value>groupMax) groupMax = value;
            }
        }
    }

//
//    private void setGroupMinMax(String dataCol){
//        dataCol = Shared.cleanString(dataCol);
//        Matcher matcher = minMaxPattern.matcher(dataCol);
//        if(matcher.matches()){
//            groupMin = Integer.parseInt(matcher.group(2).trim());
//            groupMax = Integer.parseInt(matcher.group(3).trim());
//        }
//        else log.log( Level.SEVERE, "TemplateItemGroup: Problem determining group min and group max in:\n{0}", dataCol);
//    }

    void addItem(TemplateItem templateItem){
        itemGroup.add(templateItem);
    }

    private String extractDataCol(String dataColString){
//        Matcher matcher = dataColPattern.matcher(dataColString);
        Matcher matcher = splitPattern.matcher(dataColString);
        if(matcher.matches()){
            return matcher.group(1).trim();
        }
        return "";
    }

    List<TemplateItem> expandGroup(){
        String dataCol;
        int repeat=0;
        List<TemplateItem> expandedGroup = new ArrayList<>();
        TemplateItem newItem;

        for(int i=groupMin; i<=groupMax; i++){
            for(TemplateItem templateItem:itemGroup){
                dataCol = extractDataCol(templateItem.getDataColumn());
                if(dataCol.equalsIgnoreCase("")) newItem = templateItem.clone("", repeat);
                else newItem = templateItem.clone(dataCol+"_"+i, repeat);
                expandedGroup.add(newItem);
            }
            repeat++;
        }
        return expandedGroup;
    }

    private String dataCol;

    private int groupMin;
    private int groupMax;
    //	private final String groupID;
    private final List<TemplateItem> itemGroup = new ArrayList<>();
//    private static final Pattern minMaxPattern= Pattern.compile("(.+)_(\\d+)\\s*,.+_(\\d+).*");
//    private static final Pattern dataColPattern= Pattern.compile("(.+)_\\d+\\s*,.+_\\d+.*");

    private static final Pattern splitPattern = Pattern.compile("(.+)_(\\d+)\\s*");
    static final Logger log = Logging.getLogger();
}
