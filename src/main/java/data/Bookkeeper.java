package data;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// basically a hashmap linking the header name to the index
class Bookkeeper {
	Bookkeeper(List<String> completeDataHeader) throws Exception{
        this.completeDataHeader = completeDataHeader;
		buildEntries(completeDataHeader);
	}

    // check whether the headerName exists
	boolean containsKey(String headerName){
		return headerToIndex.containsKey(headerName);
	}

	// returns the index based on the headerName
	int getIndex(String headerName) throws Exception {
		if(!headerToIndex.containsKey(headerName)) {
            // if the headerName doesn't exist, it means someone is referring to a faulty name
            // as we don't really have a lot of information here, throw an Exception
			throw new Exception("Exception in Bookkeeper: problem finding headerName: "+headerName);
		}
        return headerToIndex.get(headerName);
	}

	// build the hashmap
	private void buildEntries(List<String> completeDataHeader) throws Exception{
		for(int i=0; i<completeDataHeader.size(); i++){
			addEntry(completeDataHeader.get(i), i);
		}
	}

	private void addEntry(String headerName, int index) throws Exception{
		if(!containsKey(headerName)) headerToIndex.put(headerName, index);
		else {
            // shouldn't happen, as entries should be unique
            // as we don't really have a lot of information here, throw an Exception
			throw new Exception("Exception in Bookkeeper: the headerName "+headerName+" appears twice. Please fix.");
		}
	}

    List<String> getHeaderList(){
        return completeDataHeader;
    }
	// create case independent map
	private final Map<String, Integer> headerToIndex = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private List<String> completeDataHeader;
}
