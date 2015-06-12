package identifiers.identifiers;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.InputData;
import data.Record;
import identifiers.patient.Patient;
import identifiers.patient.RepeatingPatient;
import identifiers.patient.SimplePatient;
import identifiers.settings.Settings;
import shared.FileOperations;
import shared.Logging;

public class Identifiers {
    public Identifiers(Settings settings){
		this.settings = settings;
	}

    // returns whether the record belongs to the visitNr
    public boolean isVisitNr(Record record, int visitNr){
        String key = getKey(record);
        Patient patient = keyToPatient.get(key);
        return patient.isVisitNr(record, visitNr);
    }

    // returns the patient's studySubjectID
    // this is used by the SudySubjectID itemAction
    public String getStudySubjectID(Record record){
        String key = getKey(record);
        Patient patient = keyToPatient.get(key);
        return patient.getStudySubjectID(record);
    }

    // returns the patient's personID
    // this is used by the PersonID itemAction
    public String getPersonID(Record record){
        String key = getKey(record);
        Patient patient = keyToPatient.get(key);
        return patient.getPersonID(record);
    }

    // generate the Identifiers File
    public void writeIdentifierFile(){
        BufferedWriter bufferedWriter = FileOperations.openFileWriter(settings.getBaseDir()+ settings.getIDOutFileName());
        // write the header
        FileOperations.writeLine(bufferedWriter, settings.getIDHeader());
        // for each patient in our table, generate the ID Registration and write it to the file
        for(Patient patient:keyToPatient.values()){
            FileOperations.writeLine(bufferedWriter, patient.generateIDRegistration());
        }
        FileOperations.closeFileWriter(bufferedWriter);
    }

    // generate the Event Registration File
    public void writeOCPatientEventRegistration(){
        String line;
        BufferedWriter bufferedWriter = FileOperations.openFileWriter(settings.getBaseDir()+ settings.getRegOutFileName());
        // write the header, which is based on the maxNrVisits, as each visit will get a Vx column
        FileOperations.writeLine(bufferedWriter, settings.getRegistrationHeader(maxNrVisits));
        // for each patient in our table, generate the Event Registration and write it to the file
        for(Patient patient:keyToPatient.values()){
            line = patient.generateOCPatientEventRegistration(maxNrVisits);
            if(!line.trim().equals("")) FileOperations.writeLine(bufferedWriter, line);
        }

        FileOperations.closeFileWriter(bufferedWriter);
    }

    // read an already existing identifier file
    public void readIdentifierFile() {
        String fileName = settings.getIdentifierFile();
        if (!fileName.equals("")) {
            // read the data file
            InputData data = InputData.readDataFile(settings.getBaseDir() + fileName);
            if (data != null) {
                // generate identifiers is called with "true" to ensure the patients won't be registered again
                generateIdentifiers(data, true);
            }
        }
    }

    // generate the patients/identifiers/visits structure for an inputfile
    // this inputfile will be either a previously generated identifier file, or a datafile
	public void generateIdentifiers(InputData inputData, boolean idFileSource){
		List<Record> inputRecords = inputData.getRecords();
		for(Record record:inputRecords){
			addPatient(record, idFileSource);
		}	
	}

    // add a patient based on the record information
    // idFileSource is used to determine whether the record is new or an already existing one
    // which is used when writing the registration file
    private void addPatient(Record record, boolean idFileSource){
        try{
            Patient patient;
            String key = getKey(record);

            // check whether the key is already in the table
            // if it isn't, check whether there is a repeating column
            //      if there is, creating a repeating patient
            //      if there isn't create a normal patient
            if(!keyToPatient.containsKey(key)){
                if(settings.hasRepeatCol()) patient = new RepeatingPatient(record, settings);
                else patient = new SimplePatient(record, settings);
                keyToPatient.put(key, patient);
            }
            patient = keyToPatient.get(key);

            // add a visit to this patient
            addVisit(patient, record, idFileSource);
        } catch(Exception e){
            log.log(Level.SEVERE, "Problem in Identifiers whilst adding a record. The error is: {0}\n", e.getMessage());
        }
    }

    // add a visit to the patient
    // also check whether this patient has a higher number of visits than our current maximum
    // if so, update the maximum
    private void addVisit(Patient patient, Record record, boolean idFileSource){
        patient.addVisit(record, idFileSource);
        int nrVisits = patient.getPatientMaxVisits();
        if(nrVisits>maxNrVisits) maxNrVisits = nrVisits;
    }

    // the key was more complicated, but right now we're settling for the StudySubjectID
	private String getKey(Record record){
        String key="";
        try {
            key = record.getValue(settings.getStudySubjectIDDef());
        } catch (Exception e){
            log.log(Level.SEVERE, "Problem finding StudySubjectID: "+e.getMessage()+"\nStudySubjectID definition: "+ settings.getStudySubjectIDDef());
        }
        return key;
    }

    public Settings getSettings(){
        return settings;
    }

    public int getMaxNrVisits(){
        return maxNrVisits;
    }

	private final Settings settings;
    private final Map<String, Patient> keyToPatient = new HashMap<>();
    private int maxNrVisits=0;
    private static final Logger log = Logging.getLogger();
}
