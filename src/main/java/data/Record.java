package data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import shared.DateOperations;
import shared.Logging;
import shared.Shared;

// Records store one line of data
public class Record implements Comparable<Record>{
	public Record(int size, Bookkeeper bookkeeper) {
		createEmptyRecord(size);
		this.bookkeeper = bookkeeper;
	}
	
	// to get the value, we retrieve the index via the bookkeeper and then return the value at that location
	public String getValue(String name) throws Exception{
		return record.get(bookkeeper.getIndex(name)).trim();
	}

	// to set the value, we retrieve the index via the bookkeer and then set the value at that location
	public void setValue(String name, String value) throws Exception{
		record.set(bookkeeper.getIndex(name), value);
	}

	// transform the record's value to a tab-delimited string
	public String toString(){
		return record.stream().collect(Collectors.joining("\t"));
	}

    // return whether we can reference the headerName for this record
	public boolean containsKey(String headerName){
		return bookkeeper.containsKey(headerName);
	}

	// used for sorting the data by visitdate, which we need to determine the visitNr
	// required to ensure V1<V2<V3 etc.
	public int compareTo(Record record) {
		LocalDate myDate, otherDate;
		String visitDateDef = Shared.getSettings().getVisitDateDef();
		String myValue="", otherValue="";

		try {
			myValue = getValue(visitDateDef);
			otherValue = record.getValue(visitDateDef);
		} catch (Exception e){
			log.log(Level.SEVERE, "Problem sorting records by visit date: "+e.getMessage()+"\nvisit date definition: "+visitDateDef);
		}

		// returns for missing visitDates
		if(myValue.equals("") && otherValue.equals("")) return 0;
		if(!myValue.equals("") && otherValue.equals("")) return -1;
		if(myValue.equals("") && !otherValue.equals("")) return 1;
		
		myDate = DateOperations.toOCDate(myValue);
		otherDate = DateOperations.toOCDate(otherValue);
		if(myDate.isBefore(otherDate)) return -1;
		return 1;
	}

    // add all the values to the record
    void addAllValues(String [] values){
        for(int i=0; i<values.length; i++) addValue(i, values[i]);
    }

    // adding a value is setting the record at a certain index to the value
    void addValue(int index, String value){
        record.set(index, value);
    }

    private void createEmptyRecord(int size){
        record = new ArrayList<>(size);
        for(int i=0; i<size; i++) record.add("");
    }

    private List<String> record;
	private final Bookkeeper bookkeeper;
	private static final Logger log = Logging.getLogger();
}
