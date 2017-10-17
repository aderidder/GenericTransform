package data;

import identifiers.identifiers.Identifiers;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import shared.FileOperations;
import shared.Logging;
import shared.Shared;
import template.Template;

// Class for generating output data
public class OutputData{
    // the output data requires the template and the identifiers
	public OutputData(Template template, Identifiers identifiers){
		this.identifiers = identifiers;
		this.template = template;
		setup();
	}

    // setup the output data
	private void setup(){
		try {
            // create a bookkeeper based on the uidList
            // this list also contains the headers for the expanded repeating groups
            // the bookkeeper wil then allow us to reference the items by name
			bookkeeper = new Bookkeeper(template.getUidList());
            // the header is variant, but with the proper formatting for the output (e.g. _G1)
            header = new ArrayList<>(Arrays.asList("Unique Protocol ID", "Site", "EventName", "EventRepeat"));

			header.addAll(template.getHeader());
			size = template.getUidList().size();
		} catch(Exception e){
			log.log(Level.SEVERE, "Problem during setup of output data. Error is: {0}", e.getMessage());
		}
	}

    // this is where the data is actually generated. It requires the input data and also the visitnr for which
    // we are trying to generate data
	public void generateData(InputData inputData, int visitNr){
		try {
            // retrieve the data records
			List<Record> inputList = inputData.getRecords();
			Record outputRecord;
            // for each inputRecord
			for (Record inputRecord : inputList) {
                // check whether it has the proper visitNr
				if (identifiers.isVisitNr(inputRecord, visitNr)) {
                    // if it does, create a new outputRecord
					outputRecord = new OutputRecord(size, bookkeeper);
                    // and apply the template to the input and output record
					template.apply(inputRecord, outputRecord);
                    // add the outputRecord to the list
					output.add(outputRecord);
				}
			}
		} catch (Exception e){
			log.log(Level.SEVERE, "Problem generating output data. Error is: {0}"+e.getMessage());
		}
	}

    // write the outputData to a file
    public void writeData(String dir, String fileName) {
        if (FileOperations.createDirectory(dir)){
            // open the write
            BufferedWriter fileWriter = FileOperations.openFileWriter(dir + fileName);
            // write the header
            FileOperations.writeLine(fileWriter, header.stream().collect(Collectors.joining("\t")));
            // write the data
            FileOperations.write(fileWriter, output.stream().map(Record::toString).collect(Collectors.joining(Shared.newLine)));
            // close the write
            FileOperations.closeFileWriter(fileWriter);
        }
    }
	
	private int size;
	private Bookkeeper bookkeeper;
	private final List<Record> output = new ArrayList<>();
    private List<String> header;
	
	private final Template template;
	private final Identifiers identifiers;

	private static final Logger log = Logging.getLogger();
}
