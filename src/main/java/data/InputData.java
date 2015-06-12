package data;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import shared.FileOperations;
import shared.Logging;


public class InputData {
	private InputData(String header) throws Exception{
		addHeader(header);
	}

    // return the list of records
	public List<Record> getRecords(){
		return records;
	}

    // read a data file and return an InputData file
    // assumes the first line contains header information
    public static InputData readDataFile(String fileName){
        String line="";
        InputData inputData = null;

        // open fileReader
        BufferedReader bufferedReader = FileOperations.openFileReader(fileName);
        try {
            // read the first line with the header and create an InputData Object based on the header
            line = bufferedReader.readLine();
            inputData = new InputData(line);

            // add data
            while((line=bufferedReader.readLine())!=null){
                inputData.addData(line);
            }
            inputData.sortData();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Problem in readDataFile. Line: {0}", line);
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        finally{
            FileOperations.closeFileReader(bufferedReader);
        }
        return inputData;
    }

	// sort the data to ensure the visit dates are properly ordered.
	// if you don't sort, you could e.g. get: V1 = 1-1-2013 and V2 = 1-1-1980, instead of the other way around
	private void sortData(){
		Collections.sort(records);
	}

    // create the bookkeeper, based on the header
    // trim each entry in the list to ensure no spaces occur at the beginning or end of a header name
	private void addHeader(String line) throws Exception {
		String [] header = line.split("\\t");
        List<String> headerList = Arrays.asList(header).stream().map(String::trim).collect(Collectors.toList());
        size = headerList.size();
        bookkeeper = new Bookkeeper(headerList);
	}

    // add data to this inputData
    // convert the String to a String [], based on tab and call addData
	private void addData(String line) {
		if(!line.trim().equals("")){
			addData(line.split("\\t"));
		}
	}

    // add data to this InputData
    // Create a new Record and add all values to it
    // add the records to the record list
	private void addData(String [] values){
		Record record = new Record(size, bookkeeper);
		record.addAllValues(values);
		records.add(record);
	}

    public List<String> getHeaderList(){
        return bookkeeper.getHeaderList();
    }

    // should move this to the bookkeeper;
    private Bookkeeper bookkeeper;
	private int size;
    private final List<Record> records = new ArrayList<>();
	private static final Logger log = Logging.getLogger();


}

