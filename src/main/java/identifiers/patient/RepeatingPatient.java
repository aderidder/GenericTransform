package identifiers.patient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import identifiers.settings.Settings;
import shared.Shared;
import data.Record;

public class RepeatingPatient extends Patient{
	public RepeatingPatient(Record record, Settings settings){
		super(settings);
		setup(record);
	}
	
	private void setup(Record record){
		if(settings.useGender()) setDBGender(record);
		if(settings.useDateOfBirth()) setDOB(record);
		patientRepeatCol = settings.getRepeatColDef();
		
		setOCPersonID(record);
		setOCStudySubjectID(record);
		setDBPatientID(record);
	}

	private void setDBGender(Record record){
		try {
			dbGender = record.getValue(settings.getGenderDef());
		} catch (Exception e){
//			throw new PatientException("RepeatingPatient, problem setting gender: "+e.getMessage()+"\nGender Definition: "+identifierSettings.getGenderDef());
			log.log(Level.SEVERE, "RepeatingPatient, problem setting gender: {0}\nGender Definition: {1}", new Object[]{e.getMessage(), settings.getGenderDef()});
		}
	}

	private void setDOB(Record record){
		try {
			dbDateOfBirth = record.getValue(settings.getDOBDef());
		}  catch (Exception e){
//			throw new PatientException("RepeatingPatient, problem setting date of birth: "+e.getMessage()+"\nDate of Birth Definition: "+identifierSettings.getDOBDef());
			log.log(Level.SEVERE, "RepeatingPatient, problem setting date of birth: {0}\nDate of Birth definition: {1}", new Object[]{e.getMessage(), settings.getDOBDef()});
		}
	}
	
	private void setDBPatientID(Record record){
		try {
			dbPatientID.add(record.getValue(settings.getStudySubjectIDDef()));
		} catch (Exception e){
//			throw new PatientException("RepeatingPatient, problem with identifier: "+e.getMessage()+"\nStudySubject ID: "+identifierSettings.getStudySubjectIDDef());
			log.log(Level.SEVERE, "RepeatingPatient, problem with identifier: {0}\nStudySubject ID: {1}", new Object[]{e.getMessage(), settings.getStudySubjectIDDef()});
		}
	}
	
	public int getPatientMaxVisits(){
		return patientMaxVisits;
	}
	
	private String getPatientRepeatNr(Record record){
		String patientRepeatNr="-1";
		try {
			if (!patientRepeatCol.equals("")) patientRepeatNr = record.getValue(patientRepeatCol);
		} catch(Exception e){
//			throw new PatientException("Problem with RepeatCol: "+e.getMessage()+"\nRepeatCol: "+patientRepeatCol);
			log.log(Level.SEVERE, "RepeatingPatient, problem with RepeatCol: {0}\nRepeatCol: {1}", new Object[]{e.getMessage(),patientRepeatCol});
		}
		return patientRepeatNr;
	}

    // does this record belong to a certain visitNr?
	@Override public boolean isVisitNr(Record record, int visitNr){
		try {
			String visitDate = record.getValue(settings.getVisitDateDef());
			String patientRepeatNr = getPatientRepeatNr(record);

			List<Visit> visitList = visits.get(patientRepeatNr);
			int index = visitNr - 1;

			if (visitList.size() > index) {
				if (visitList.get(index).getNormalDate().equalsIgnoreCase(visitDate)) return true;
			}
		} catch(Exception e){
			log.log(Level.SEVERE, "RepeatingPatient, problem checking visitDate: {0}\nVisitDateDef: {1}", new Object[]{e.getMessage(), settings.getVisitDateDef()});
		}
            // V1 is at index 0
//            if (visitList.get(visitNr - 1).getNormalDate().equalsIgnoreCase(visitDate)) return true;
		return false;
	}

	@Override public void addVisit(Record record, boolean idFileSource){
		try {
			String visitDate = record.getValue(settings.getVisitDateDef());
			String patientRepeatNr = getPatientRepeatNr(record);

			if (!visits.containsKey(patientRepeatNr)) visits.put(patientRepeatNr, new ArrayList<>());
			List<Visit> visitList = visits.get(patientRepeatNr);

			List<String> dates = visitList.stream().map(Visit::getNormalDate).collect(Collectors.toList());
			int index = dates.indexOf(visitDate);

			if (index == -1) {
				Visit visit = new Visit(visitDate);
				if (idFileSource) visit.setAlreadyRegistered();
				visitList.add(visit);
			}

			if (visitList.size() > patientMaxVisits) patientMaxVisits = visitList.size();
		} catch(Exception e){
			log.log(Level.SEVERE, "RepeatingPatient, problem adding visit: {0}\nVisitDateDef: {1}", new Object[]{e.getMessage(), settings.getVisitDateDef()});
		}
	}

    private void setOCPersonID(Record record){
		try {
			if (settings.usePersonID()) {
				if (record.containsKey(settings.getOCPersonIDDef())) {
					this.ocPersonID = record.getValue(settings.getOCPersonIDDef());
				} else if (settings.generateIdentifiers()) {
					this.ocPersonID = generatePatientID();
				} else {
					this.ocPersonID = record.getValue(settings.getPersonIDDef());
				}
			}
		} catch(Exception e){
			log.log(Level.SEVERE, "RepeatingPatient, problem setting personID: {0}\nPersonIDDef: {1}", new Object[]{e.getMessage(), settings.getPersonIDDef()});
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
			log.log(Level.SEVERE, "RepeatingPatient, problem setting studySubjectID: {0}\nstudySubjectIDDef: {1}", new Object[]{e.getMessage(), settings.getStudySubjectIDDef()});
		}

    }

    private void setMaxID(){
        myIndex = Integer.parseInt(ocStudySubjectID.substring(ocStudySubjectID.lastIndexOf("-")+1));
        settings.setMaxID(myIndex);
    }
	
	@Override public String getStudySubjectID(Record record){
		try {
			return ocStudySubjectID + "_" + record.getValue(patientRepeatCol);
		} catch(Exception e){
			log.log(Level.SEVERE, "RepeatingPatient, problem getting studySubjectID: {0}\nstudySubjectIDDef: {1}", new Object[]{e.getMessage(), settings.getStudySubjectIDDef()});
		}
		return "";
	}

	@Override public String getPersonID(Record record){
		try {
			// check... probably for personID Action
			if (settings.extendPersonID()) {
				return ocPersonID + "_" + record.getValue(patientRepeatCol);
			}
			return ocPersonID;
		} catch(Exception e){
			log.log(Level.SEVERE, "RepeatingPatient, problem getting personID: {0} \nRepeatCol: {1}", new Object[]{e.getMessage(), patientRepeatCol});
		}
		return "";
	}

	public String generateOCPatientEventRegistration(int maxNrVisits){
		String line="", tmpLine;
		List<Visit> visitList;
		List<String> list = new ArrayList<>(visits.keySet());
		Collections.sort(list);
		for(String key:list) {
			visitList = visits.get(key);
			tmpLine = createOCRegLine(visitList, "_"+key, maxNrVisits);
			if(!tmpLine.trim().equals("")) line += tmpLine+Shared.newLine;
		}

		if(line.trim().equals("")) return "";
		return line.substring(0, line.length()-Shared.newLine.length());
	}
	
	
	public String generateIDRegistration(){
		String line="", keyString;
		List<String> list = new ArrayList<>(visits.keySet());
		List<Visit> visitList;
		Collections.sort(list);
		for(String key:list) {
			visitList = visits.get(key);
			keyString = key+"\t";
			line += createIDRegLine(visitList, keyString)+Shared.newLine;
		}

		return line.substring(0, line.length()-Shared.newLine.length());
	}

	private String patientRepeatCol=""; //bv bioVolgnr
	private final Map<String, ArrayList<Visit>> visits = new HashMap<> ();
}

