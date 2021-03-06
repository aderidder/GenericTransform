package identifiers.patient;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import identifiers.settings.Settings;
import shared.DateOperations;
import shared.Logging;
import shared.Shared;
import data.Record;

public abstract class Patient {
	Patient(Settings settings){
		this.settings = settings;
	}

    // Abstract classes which any implementing class require
    public abstract String generateIDRegistration();
//	public abstract String generateOCPatientEventRegistration(int maxNrVisits);
	public abstract void addVisit(Record record, boolean idFileSource);
	public abstract int getPatientMaxVisits();
	public abstract boolean isVisitNr(Record record, int visitNr);
	public abstract String getStudySubjectID(Record record);
	public abstract String getPersonID(Record record);

	public abstract String generateOCDUSubjectRegistration();
    public abstract String generateOCDUEventRegistration();


    // generate a patientID
    // called by implementing SimplePatient and RepeatingPatient
	final String generatePatientID(){
        // find the maximum number used so far and add 1
        int curPatientNr = settings.getMaxID() + 1;
		return settings.getPersonIDPrefix()+ curPatientNr;
	}

    // generate a studySubjectID
    // called by implementing SimplePatient and RepeatingPatient
    final String generateStudySubjectID(){
        // find the maximum number used so far and add 1
		int curPatientNr = settings.getMaxID()+1;
		return  settings.getStudySubjectIDPrefix()+curPatientNr;
	}


    // SimplePatients don't have a suffix.
    // call the createOCRegLine with ""
//    final String createOCRegLine(List<Visit> visitList, int maxNrVisits){
//        return createOCRegLine(visitList, "", maxNrVisits);
//    }
//
//	final String addEmptyVisits(List<Visit> visitList, int maxNrVisits){
//		int patientNrVisits = visitList.size();
//		String returnString="";
//		for(int i = patientNrVisits; i<maxNrVisits; i++){
//			returnString+="\t";
//		}
//		return returnString;
//	}
    // create a line for the registration file
    // this is based on the visitList (as each visit needs to be in the registration file)
    // and on the suffix
//    final String createOCRegLine(List<Visit> visitList, String suffix, int maxNrVisits){
//		// turn the visitList into a String
//		String visits = visitList.stream().map(Visit::getRegistrationDate).collect(Collectors.joining("\t"));
//		String regLine="";
//		// check whether there are visits
//		if(!visits.trim().equals("")){
//			// add personID information to the line if necessary
//			if(settings.usePersonID()){
//                if(settings.extendPersonID()) regLine=ocPersonID+suffix+"\t";
//                else regLine=ocPersonID+"\t";
//            }
//			regLine+=ocStudySubjectID+suffix;
//
//			// add dob information to the line if necessary
//			if(settings.useDateOfBirth()) {
//				if(settings.useYearOfBirthOnly()) regLine += "\t" + DateOperations.toOCDate(dbDateOfBirth).getYear();
//				else regLine += "\t" + DateOperations.toOCDate(dbDateOfBirth);
//			}
//
//			// add gender information to the line if necessary
//			if(settings.useGender()){
//				regLine += "\t"+ settings.translateGender(dbGender);
//			}
//			regLine+="\t"+visits;
//		}
//		return regLine+addEmptyVisits(visitList, maxNrVisits);
//	}


    // SimplePatients don't have a suffix.
    // call the createOCRegLine with ""
    final String createOCDUSubjectRegistrationLine(List<Visit> visitList){
        return createOCDUSubjectRegistrationLine(visitList, "");
    }

    // PersonID; Study Subject ID; Gender; Date of Enrollment; Site (optional); Study
    final String createOCDUSubjectRegistrationLine(List<Visit> visitList, String suffix){
        String regLine="";
        // check whether there are visits
        if(visitList.size()>0){
            // add personID information to the line if necessary
            if(settings.usePersonID()){
                if(settings.extendPersonID()) regLine=ocPersonID+suffix+"\t";
                else regLine=ocPersonID+"\t";
            }
            regLine+=ocStudySubjectID+suffix;

            // add gender information to the line if necessary
            if(settings.useGender()){
                regLine += "\t"+ settings.translateGender(dbGender);
            }

            // add dob information to the line if necessary
            if(settings.useDateOfBirth()) {
                if(settings.useYearOfBirthOnly()) regLine += "\t" + DateOperations.toOCDate(dbDateOfBirth).getYear();
                else regLine += "\t" + DateOperations.toOCDate(dbDateOfBirth);
            }

            // use first visit as enrollment date
            regLine+="\t"+visitList.get(0).getRegistrationDate();

            regLine += "\t"+settings.getSiteName()+"\t"+settings.getStudyName();
        }
        return regLine;
    }

    final String createOCDUEventRegistrationLine(List<Visit> visitList){
        return createOCDUEventRegistrationLine(visitList, "");
    }

    // Study Subject ID; Event Name; Start Date; Site; Location; Study; Repeat Number
    final String createOCDUEventRegistrationLine(List<Visit> visitList, String suffix){
        StringBuffer regLine=new StringBuffer();
        for(int i=0; i<visitList.size(); i++){
            Visit visit = visitList.get(i);
            regLine.append(ocStudySubjectID).append(suffix).append("\t");
            regLine.append(settings.getVisitName()).append("\t");
            regLine.append(visit.getRegistrationDate()).append("\t");
            regLine.append(settings.getSiteName()).append("\t");
            regLine.append("\t"); //location
            regLine.append(settings.getStudyName()).append("\t");
            regLine.append(i+1).append("\n");
        }
        String line = regLine.toString();
        // line is empty return empty string otherwise remove the last carriage return
        return line.length()==0?"":line.substring(0, line.length()-1);
    }

    final String createIDRegLine(List<Visit> visitList, String repString){
		String dbIds = dbPatientID.stream().collect(Collectors.joining("\t"));
		String aString = Shared.newLine;
		if(settings.usePersonID()) aString+=ocPersonID+"\t";
		aString+=ocStudySubjectID+"\t"+dbIds+"\t"+repString;
		if(settings.useDateOfBirth()) {
            if(settings.useYearOfBirthOnly()) aString += DateOperations.toOCDate(dbDateOfBirth).getYear()+"\t";
            else aString += dbDateOfBirth+"\t";
        }
		if(settings.useGender()) aString += dbGender+"\t";
		aString+=visitList.stream().map(Visit::getNormalDate).collect(Collectors.joining(aString));
		
		return aString.substring(Shared.newLine.length());
	}
	

	class Visit{
		Visit(String date){
			this.date=date;
		}

		void setAlreadyRegistered(){
			alreadyRegistered=true;
		}
		String getNormalDate(){
			return date;
		}
		String getRegistrationDate(){
			if(alreadyRegistered) return "";
			return DateOperations.toOCDate(date).toString();
		}

		private final String date;
		private boolean alreadyRegistered=false;
	}

	
	final Settings settings;
	final List<String> dbPatientID = new ArrayList<>();
	
	String dbGender;
	String dbDateOfBirth;

	String ocPersonID;
	String ocStudySubjectID;
	
	int myIndex;
	int patientMaxVisits=0;
	static final Logger log = Logging.getLogger();
}
