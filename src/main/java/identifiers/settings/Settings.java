package identifiers.settings;

import shared.DateOperations;
import shared.FileOperations;
import shared.Logging;
import template.Template;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings {
	public Settings(String baseDir, String settingsFile, String dataFile){
//		this.visitNr = visitNr;
		fileSettings = new FileSettings(baseDir, settingsFile, dataFile);
		readSettingsFile();
		generateIDHeader();
		generateRegistrationHeader();
	}

	public void setVisitNr(int visitNr){
		this.visitNr = visitNr;
	}

	public int getVisitNr(){
		return visitNr;
	}

	private void generateIDHeader(){
		idHeader = ocStudySubjectIDDef+"\t"+ identifierSettings.getStudySubjectIDDef()+"\t";
		if(!identifierSettings.getRepeatColDef().equals("")) idHeader+= identifierSettings.getRepeatColDef()+"\t";
		if(useDateOfBirth()) idHeader+= identifierSettings.getDOBDef()+"\t";
		if(useGender()) idHeader+= identifierSettings.getGenderDef()+"\t";
		idHeader+= identifierSettings.getVisitDateDef();

		if(usePersonID()) idHeader = ocPersonIDDef+"\t"+idHeader;
	}

	private void generateRegistrationHeader(){
		registrationHeader = ocStudySubjectIDDef;
//		if(useDateOfBirth()) registrationHeader+="\t"+ocDOBDef;
//		if(useGender()) registrationHeader+="\t"+ocGenderDef;
		if(useDateOfBirth()) registrationHeader+="\t"+ocDOBDef;
		if(useGender()) registrationHeader+="\t"+ocGenderDef;
		if(usePersonID()) registrationHeader = ocPersonIDDef+"\t"+registrationHeader;
	}

	private void readSettingsFile(){
		String line="";
		String [] splitLine;
		BufferedReader bufferedReader = FileOperations.openFileReader(fileSettings.getFullSettingsDir()+fileSettings.getSettingsFile());
		try {
			while((line=bufferedReader.readLine())!=null){
				if(!line.equalsIgnoreCase("")) {
					splitLine = line.split("\\t");
					if (splitLine.length == 1) setSetting(splitLine[0], "");
					else setSetting(splitLine[0], splitLine[1]);
				}
			}
			IdentifierSettingsVerifier.verifySettings(this);
		} catch (IOException e) {
			log.log(Level.SEVERE, "IOException: Problem in readSettingsFile. Line: {0}", line);
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		catch (Exception e) {
			log.log( Level.SEVERE, "IdentifierSettings: Problem with the arguments in the settings file.\n{0}", e.getMessage());
		} finally{
			FileOperations.closeFileReader(bufferedReader);
		}
	}

	private void setSetting(String setting, String value){
		SettingType settingType = SettingType.getFieldType(setting.toUpperCase());
		switch (settingType){
			case GENERATEIDENTIFIERS: identifierSettings.setGenerateIdentifiers(value); break;
			case USEPERSONID: identifierSettings.setUsePersonID(value); break;
			case PERSONIDDEF: identifierSettings.setPersonIDDef(value); break;
			case PERSONIDEXTEND: identifierSettings.setExtendPersonID(value); break;
			case PERSONIDPREFIX: identifierSettings.setPersonIDPrefix(value); break;
			case STUDYSUBJECTIDDEF: identifierSettings.setStudySubjectIDDef(value); break;
			case STUDYSUBJECTIDPREFIX: identifierSettings.setStudySubjectIDPrefix(addDash(value)); break;
			case VISITDATEDEF: identifierSettings.setVisitDateDef(value); break;
			case USEDATEOFBIRTH: identifierSettings.setUseDateOfBirth(value); break;
			case DATEOFBIRTHDEF: identifierSettings.setDobDef(value); break;
			case YEAROFBIRTHONLY: identifierSettings.setYearOfBirthOnly(value); break;
			case USEGENDER: identifierSettings.setUseGender(value); break;
			case GENDERMALECODE: identifierSettings.addMaleCode(value); break;
			case GENDERFEMALECODE: identifierSettings.addFemaleCode(value); break;
			case GENDERDEF: identifierSettings.setGenderDef(value); break;
			case REPEATCOLDEF: identifierSettings.setRepeatColDef(value); break;
			case DATEFORMAT: DateOperations.setDateFormat(value); break;
//			case DATAFILE: fileSettings.setDataFile(value); break;
			case IDENTIFIERFILE: fileSettings.setIdentifierFile(value); break;
			case CUSTOMDATAFILE: fileSettings.setCustomDataOutFileName(value); break;
			case CUSTOMIDFILE: fileSettings.setCustomIDOutFileName(value); break;
			case CUSTOMREGFILE: fileSettings.setCustomRegOutFileName(value); break;
			default:
				log.log( Level.SEVERE, "Unknown setting: {0}; ignored", setting);
		}
	}

	public String getIDHeader(){
		return idHeader;
	}
	public String getOCPersonIDDef(){
		return ocPersonIDDef;
	}
	public String getOCStudySubjectIDDef(){
		return ocStudySubjectIDDef;
	}

	// gets based on settings
	public boolean hasRepeatCol(){
		return !identifierSettings.getRepeatColDef().equals("");
	}
	public boolean usePersonID(){
		return toBoolean(identifierSettings.getUsePersonID());
	}
	public boolean useDateOfBirth(){
		return toBoolean(identifierSettings.getUseDateOfBirth());
	}
	public boolean useGender(){
		return toBoolean(identifierSettings.getUseGender());
	}
	public boolean extendPersonID() { return toBoolean(identifierSettings.getPersonIDExtend()); }
	public boolean useYearOfBirthOnly(){
		return toBoolean(identifierSettings.getYearOfBirthOnly());
	}
	public boolean generateIdentifiers(){
		return toBoolean(identifierSettings.getGenerateIdentifiers());
	}

	// gets for settings
	public String getRepeatColDef(){
		return identifierSettings.getRepeatColDef();
	}
	public String getVisitDateDef(){
		return identifierSettings.getVisitDateDef();
	}
	public String getGenderDef(){
		return identifierSettings.getGenderDef();
	}
	public String getDOBDef(){
		return identifierSettings.getDOBDef();
	}
	public String getStudySubjectIDDef(){
		return identifierSettings.getStudySubjectIDDef();
	}
	public String getPersonIDDef(){
		return identifierSettings.getPersonIDDef();
	}
	public String getStudySubjectIDPrefix(){
		return identifierSettings.getStudySubjectIDPrefix();
	}
	public String getPersonIDPrefix(){
		return addDash(identifierSettings.getPersonIDPrefix());
	}
	public String getPersonIDExtend(){return identifierSettings.getPersonIDExtend();}
	public String getUsePersonID(){return identifierSettings.getUsePersonID();}
	public String getUseGender(){return identifierSettings.getUseGender();}
	public String getUseDateOfBirth(){return identifierSettings.getUseDateOfBirth();}
	public String getYearOfBirthOnly(){return identifierSettings.getYearOfBirthOnly();}
	public String getGenerateIdentifiers(){return identifierSettings.getGenerateIdentifiers();}

	// gets for fileSettings
	public String getDataOutFileName(Template template){
		return fileSettings.getDataOutFileName(template, visitNr);
	}
	public String getIdentifierFile(){
		return fileSettings.getIdentifierFile();
	}
	public String getDataFile(){
		return fileSettings.getDataFile();
	}
	public String getIDOutFileName(){
		return fileSettings.getIDOutFileName();
	}
	public String getRegOutFileName(){
		return fileSettings.getRegOutFileName();
	}
	public String getTestResultDir(){
		return FileSettings.getTestResultDir();
	}
	public String getBaseDir(){
		return fileSettings.getBaseDir();
	}
	public String getDataDir(){
		return fileSettings.getFullDataDir();
	}
	public String getTemplateDir(){
		return fileSettings.getFullTemplateDir();
	}
	public String getFullOutDir(){
		return fileSettings.getFullOutDir();
	}

	public String getRegistrationHeader(int maxNrVisits){
		String visits="";
		for(int i=0; i<maxNrVisits; i++) visits+="\tV"+(i+1);
		return registrationHeader+visits;
	}

	public String translateGender(String gender){
		try {
			return identifierSettings.translateGender(gender);
		} catch(Exception e){
			log.log(Level.SEVERE, "Problem translating gender. Error is: {0}", e.getMessage());
		}
		return "";
	}

	public int getMaxID(){
		return maxID;
	}
	public void setMaxID(int value){
		if(value>maxID) maxID = value;
	}

	boolean genderMapContainsValue(String gender){
		return identifierSettings.containsGenderValue(gender);
	}

	private boolean toBoolean(String value){
		return value.equalsIgnoreCase("y");
	}

	private String addDash(String value){
		if(!value.equalsIgnoreCase("") && !value.endsWith("-")) return value+"-";
		return value;
	}

	private String idHeader;
	private String registrationHeader;

	private int maxID=0;
	private int visitNr;

	private final String ocStudySubjectIDDef="StudySubjectID";
	private final String ocPersonIDDef="PersonID";
	private final String ocGenderDef="Gender";
	private final String ocDOBDef="Date_of_Birth";

	private final IdentifierSettings identifierSettings = new IdentifierSettings();
	private final FileSettings fileSettings;

	private static final Logger log = Logging.getLogger();
}

class IdentifierSettings {
	IdentifierSettings(){}

	String getGenerateIdentifiers(){
		return generateIdentifiers;
	}
	String getRepeatColDef(){
		return repeatColDef;
	}
	String getVisitDateDef(){
		return visitDateDef;
	}
	String getGenderDef(){
		return genderDef;
	}
	String getDOBDef(){
		return dobDef;
	}
	String getStudySubjectIDDef(){
		return studySubjectIDDef;
	}
	String getPersonIDDef(){
		return personIDDef;
	}
	String getStudySubjectIDPrefix(){
		return studySubjectIDPrefix;
	}
	String getPersonIDPrefix(){
		return personIDPrefix;
	}
	String getPersonIDExtend(){return extendPersonID;}
	String getUsePersonID(){return usePersonID;}
	String getUseGender(){return useGender;}
	String getUseDateOfBirth(){return useDateOfBirth;}
	String getYearOfBirthOnly(){return yearOfBirthOnly;}

	void setGenerateIdentifiers(String generateIdentifiers) {
		this.generateIdentifiers = generateIdentifiers;
	}
	void setPersonIDDef(String personIDDef) {
		this.personIDDef = personIDDef;
	}
	void setStudySubjectIDDef(String studySubjectIDDef) {
		this.studySubjectIDDef = studySubjectIDDef;
	}
	void setVisitDateDef(String visitDateDef) {
		this.visitDateDef = visitDateDef;
	}
	void setDobDef(String dobDef) {
		this.dobDef = dobDef;
	}
	void setStudySubjectIDPrefix(String studySubjectIDPrefix) {
		this.studySubjectIDPrefix = studySubjectIDPrefix;
	}
	void setPersonIDPrefix(String personIDPrefix) {
		this.personIDPrefix = personIDPrefix;
	}
	void setRepeatColDef(String repeatColDef) {
		this.repeatColDef = repeatColDef;
	}
	void setGenderDef(String genderDef) {
		this.genderDef = genderDef;
	}
	void setUsePersonID(String usePersonID) {
		this.usePersonID = usePersonID;
	}
	void setUseGender(String useGender) {
		this.useGender = useGender;
	}
	void setUseDateOfBirth(String useDateOfBirth) {
		this.useDateOfBirth = useDateOfBirth;
	}
	void setYearOfBirthOnly(String yearOfBirthOnly) {
		this.yearOfBirthOnly = yearOfBirthOnly;
	}
	void setExtendPersonID(String extendPersonID) {
		this.extendPersonID = extendPersonID;
	}
	void addMaleCode(String maleCode){
		genderMap.put(maleCode, "m");
	}
	void addFemaleCode(String femaleCode){
		genderMap.put(femaleCode, "f");
	}

	String translateGender(String gender) throws Exception{
		if(genderMap.containsKey(gender)) return genderMap.get(gender);
		throw new Exception("translateGender: No coding found for gender "+gender);
	}

	boolean containsGenderValue(String gender){
		return genderMap.containsValue(gender);
	}

	// settings
	private String personIDPrefix="";
	private String repeatColDef="";
	private String genderDef="";
	private String usePersonID="";
	private String useGender="";
	private String useDateOfBirth="";
	private String yearOfBirthOnly ="";
	private String extendPersonID="";
	private String generateIdentifiers="";
	private String personIDDef ="";
	private String studySubjectIDDef ="";
	private String visitDateDef="";
	private String dobDef="";
	private String studySubjectIDPrefix="";

	private final Map<String, String> genderMap = new HashMap<>();
}

