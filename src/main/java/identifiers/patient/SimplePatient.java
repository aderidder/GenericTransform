package identifiers.patient;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import identifiers.settings.Settings;
import shared.Shared;
import data.Record;

public class SimplePatient extends Patient{
	public SimplePatient(Record record, Settings settings){
		super(settings);
		setup(record);
	}
	
	private void setup(Record record){
		if(settings.useGender()) setDBGender(record);
		if(settings.useDateOfBirth()) setDOB(record);
		
		setOCPersonID(record);
		setOCStudySubjectID(record);
		setDBPatientID(record);
	}

	private void setDBGender(Record record){
		try {
			dbGender = record.getValue(settings.getGenderDef());
		} catch (Exception e){
			log.log(Level.SEVERE, "SimplePatient, problem setting gender: "+e.getMessage()+"\nGender Definition: "+ settings.getGenderDef());
		}
	}

	private void setDOB(Record record){
		try {
			dbDateOfBirth = record.getValue(settings.getDOBDef());
		}  catch (Exception e){
			log.log(Level.SEVERE, "SimplePatient, problem setting date of birth: {0}\nDate of Birth Definition: {1}", new Object[]{e.getMessage(), settings.getDOBDef()});
		}
	}

	private void setDBPatientID(Record record){
		try {
			dbPatientID.add(record.getValue(settings.getStudySubjectIDDef()));
		} catch (Exception e){
			log.log(Level.SEVERE, "SimplePatient, problem with identifier: {0}\nStudySubject ID: {1}", new Object[]{e.getMessage(), settings.getStudySubjectIDDef()});
		}
	}

	public int getPatientMaxVisits(){
		return patientMaxVisits;
	}
	
	@Override public boolean isVisitNr(Record record, int visitNr){
		try {
			String visitDate = record.getValue(settings.getVisitDateDef());
			if(visits.size()<visitNr) return false;
			// V1 is at index 0
			if (visits.get(visitNr - 1).getNormalDate().equalsIgnoreCase(visitDate)) return true;
		} catch(Exception e){
			log.log(Level.SEVERE, "SimplePatient, problem checking visitDate: {0}\nVisitDateDef: {1}", new Object[]{e.getMessage(), settings.getVisitDateDef()});
		}
		return false;
	}

    // The flow is as follows:
    // 1. dates are read from a registration file
    // 2. dates are read from the data file
    //
    // in case of 1, the visit will be added, but alreadyRegistered will be set to true, to ensure a
    // new registration file does not contain the visit
    // in case of 2, the visit can already exist (index!=-1), in which case nothing happens
    // or, if the visit does not exist, it is added and, as it is really new, will be written to the
    // registration file
	@Override public void addVisit(Record record, boolean idFileSource){
		try {
			String visitDate = record.getValue(settings.getVisitDateDef());
			// create a list with dates already available for the patient and check whether our "new" date
			// already exists
			List<String> dates = visits.stream().map(Visit::getNormalDate).collect(Collectors.toList());
			int index = dates.indexOf(visitDate);

			if (index == -1) {
				Visit visit = new Visit(visitDate);
				if (idFileSource) visit.setAlreadyRegistered();
				visits.add(visit);
				if (visits.size() > patientMaxVisits) patientMaxVisits = visits.size();
			}
		} catch(Exception e){
			log.log(Level.SEVERE, "SimplePatient, problem adding visit: {0}\nVisitDateDef: {1}", new Object[]{e.getMessage(), settings.getVisitDateDef()});
		}
	}

    // the record can have two sources:
    // 1) an idFile and 2) a dataFile
	private void setOCPersonID(Record record){
		try {
			// check whether personID should be used
			if (settings.usePersonID()) {
				// check whether the identifier record contains a personID and if so, store the ID
				if (record.containsKey(settings.getOCPersonIDDef())) {
					this.ocPersonID = record.getValue(settings.getOCPersonIDDef());
				}
				// otherwise check whether we are generating identifiers
				else if (settings.generateIdentifiers()) {
					this.ocPersonID = generatePatientID();
				}
				// final option is looking at the data record
				else {
					this.ocPersonID = record.getValue(settings.getPersonIDDef());
				}
			}
		} catch(Exception e){
			log.log(Level.SEVERE, "SimplePatient, problem setting personID: {0}\nPersonIDDef: {1}", new Object[]{e.getMessage(), settings.getPersonIDDef()});
		}
	}
	
	private void setOCStudySubjectID(Record record){
		try{
		if(record.containsKey(settings.getOCStudySubjectIDDef())) {
			this.ocStudySubjectID = record.getValue(settings.getOCStudySubjectIDDef());
            if(settings.generateIdentifiers()) {
                setMaxID();
            }
		}
        else if(settings.generateIdentifiers()){
			this.ocStudySubjectID = generateStudySubjectID();
            setMaxID();
		}
        else{
            this.ocStudySubjectID = record.getValue(settings.getStudySubjectIDDef());
        }
	} catch(Exception e){
			log.log(Level.SEVERE, "SimplePatient, problem setting studySubjectID: {0}\nstudySubjectIDDef: {1}", new Object[]{e.getMessage(), settings.getStudySubjectIDDef()});
	}
	}

    private void setMaxID(){
        myIndex = Integer.parseInt(ocStudySubjectID.substring(ocStudySubjectID.lastIndexOf("-")+1));
        settings.setMaxID(myIndex);
    }

	@Override public String getStudySubjectID(Record record) {
		return ocStudySubjectID;
	}

	@Override public String getPersonID(Record record) {
		return ocPersonID;
	}

//	public String generateOCPatientEventRegistration(int maxNrVisits){
//		return createOCRegLine(visits, maxNrVisits);
//	}
	
	public String generateIDRegistration(){
		String line = createIDRegLine(visits, "")+Shared.newLine;
		return line.substring(0, line.length()-Shared.newLine.length());
	}

    @Override public String generateOCDUSubjectRegistration(){
        return createOCDUSubjectRegistrationLine(visits);
    }

    @Override public String generateOCDUEventRegistration(){
        return createOCDUEventRegistrationLine(visits);
    }

	private final List<Visit> visits = new ArrayList<>();
}



