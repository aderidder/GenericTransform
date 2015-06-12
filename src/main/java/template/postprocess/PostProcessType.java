package template.postprocess;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PostProcessType {
	XORRESULTCOL(true),
	SETVALIFRESULTCOL(false),
	SETVALIFORIGCOL(false),
    ROUNDVALUE(true);

    private PostProcessType(boolean checkPreprocessFirst){
        this.checkPreprocessFirst=checkPreprocessFirst;
    }

    public static boolean containsFieldType(String fieldTypeString){
        return fieldTypes.contains(fieldTypeString);
    }

	public static PostProcessType getFieldType(String fieldTypeString){
//		if(!fieldTypes.contains(fieldTypeString)) return null;
        if(!containsFieldType(fieldTypeString)) return null;
		return PostProcessType.valueOf(fieldTypeString);
	}

    public boolean checkPreprocessFirst(){
        return checkPreprocessFirst;
    }
	
	private static List<String> setupFieldTypes(){
		List<PostProcessType> tmpList = Arrays.asList(PostProcessType.values());
		return tmpList.stream().map(PostProcessType::name).collect(Collectors.toList());
	}
	
	private static final List<String> fieldTypes = setupFieldTypes();
    private boolean checkPreprocessFirst;

}
