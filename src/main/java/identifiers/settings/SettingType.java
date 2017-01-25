package identifiers.settings;

import shared.Logging;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public enum SettingType {
	STUDYNAME,
    SITENAME,
    GENERATEIDENTIFIERS,    // Whether to generate Identfiers
	USEPERSONID,            //
	PERSONIDDEF,            // The column to use for the person Identifiers
	PERSONIDPREFIX,         // If generating identifiers, the prefix to use for the PersonID
    PERSONIDEXTEND,         // Whether to add an _nr to the personID. This is used when a repeatColDef is given
                            // e.g. SS: ESRA-55-0001_1 --> P: ESRA-55-0001_1 or ESRA-55-0001
                            //      SS: ESRA-55-0001_2 --> P: ESRA-55-0001_2 or ESRA-55-0001
	STUDYSUBJECTIDDEF,      // The column to use for the studySubject Identifiers
	STUDYSUBJECTIDPREFIX,   // If generating identifiers, the prefix to use for the SSID
	VISITDATEDEF,           // The column to use for the visitDate Identifiers
	USEDATEOFBIRTH,
	DATEOFBIRTHDEF,         // The column to use for the dob
	YEAROFBIRTHONLY,        // Whether the full date may be used as dob or only the year
	USEGENDER,
	GENDERDEF,              // The column to use for the gender
	GENDERMALECODE,         // The code for Male
	GENDERFEMALECODE,       // The code for Female
	REPEATCOLDEF,           // The column to use for a repeating column (e.g. biological nr)
	DATEFORMAT,             // The date format used in the study
	DATAFILE,               // The name of the dataFile
	IDENTIFIERFILE,         // The name of an identifierFile
    CUSTOMDATAFILE,         // The name of a custom output dataFile
    CUSTOMIDFILE,           // The name of a custom output identifierFile
    CUSTOMREGFILE;          // The name of a custom output registrationFile

    private static boolean containsFieldType(String fieldTypeString){
        return fieldTypes.contains(fieldTypeString);
    }

	public static SettingType getFieldType(String fieldTypeString){
        if(containsFieldType(fieldTypeString)) return SettingType.valueOf(fieldTypeString);

        log.log(Level.SEVERE, "Severe error determining SettingType. {0} is not supported. Please select from {1}", new Object[]{fieldTypeString, fieldTypes.toString()});
        return null;

	}
	
	private static List<String> setupFieldTypes(){
		List<SettingType> tmpList = Arrays.asList(SettingType.values());
		return tmpList.stream().map(SettingType::name).collect(Collectors.toList());
	}
	
	private static final List<String> fieldTypes = setupFieldTypes();
    private static final Logger log = Logging.getLogger();

}
