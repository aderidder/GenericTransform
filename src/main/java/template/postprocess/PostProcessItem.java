package template.postprocess;

import java.util.logging.Logger;

import data.Record;
import shared.Logging;
import template.TemplateIndex;

// postprocess items are after all actions have been performed
public abstract class PostProcessItem {
	// refCol: reference column
	// refVal: reference value
	// newVal: new value. Also used for the number of digits in case of RoundValue
//	PostProcessItem(String refCol, String refVal, String newVal){
//		this.newVal = newVal;
//		refValues = Arrays.asList(refVal.split(",")).stream().map(p-> p.trim()).collect(Collectors.toList());
//		this.refCol = refCol;
//	}

    PostProcessItem(boolean checkPreprocessItemsFirst){
        this.checkPreprocessItemsFirst = checkPreprocessItemsFirst;
    }

	PostProcessItem(String newVal, boolean checkPreprocessItemsFirst){
		this.newVal = newVal;
        this.checkPreprocessItemsFirst = checkPreprocessItemsFirst;
	}
	
	public abstract void performItemAction(Record outputRecord, int instanceNr, String uid);
	public abstract boolean conditionMet(Record inputRecord, Record outputRecord, int instanceNr);

    public boolean checkPreprocessedFirst(){
        return checkPreprocessItemsFirst;
    }


    // create a postprocess item, based on the input line
	public static PostProcessItem createPostProcessItem(String[] splitLine) throws Exception{
		PostProcessItem postprocessItem;
		// retrieve the index which contains the checktype
		int index = TemplateIndex.checkTypeIndex;
		if(splitLine.length<=index) return null;
		PostProcessType postProcessType = PostProcessType.getFieldType(splitLine[index].toUpperCase());

		if(postProcessType == null) return null;

        // maybe add a check to check whether index+x even exists?
		String statement = splitLine[index+1];
		String newVal = "";

        if(splitLine.length>index+2) newVal = splitLine[index+2];

        boolean checkPreProcessItem = postProcessType.checkPreprocessFirst();

		switch(postProcessType){
			case XORRESULTCOL:
				postprocessItem = new XorResultColPostProcessItem(statement, newVal, checkPreProcessItem);
				break;
			case SETVALIFRESULTCOL:
				postprocessItem = new SetValIfResultColPostProcessItem(statement, newVal, checkPreProcessItem);
				break;
			case SETVALIFORIGCOL:
				postprocessItem = new SetValIfOrigColPostProcessItem(statement, newVal, checkPreProcessItem);
				break;
            case ROUNDVALUE:
                postprocessItem = new RoundValuePostProcessItem(newVal, checkPreProcessItem);
                break;
			default: return null;	
		}
		return postprocessItem;
	}

	static final Logger log = Logging.getLogger();

    boolean checkPreprocessItemsFirst;
    String newVal;
}
