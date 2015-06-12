package identifiers.settings;

import shared.DateOperations;

class IdentifierSettingsVerifier {
    static void verifySettings(Settings settings) throws Exception{
        String message = checkCompulsoryFields(settings);
        message += checkDOBFields(settings);
        message += checkGenderFields(settings);
        message += checkGenerateFields(settings);
        if(!message.equalsIgnoreCase("")) throw new Exception(message);
    }

    // Checks for compulsory fields
    private static String checkCompulsoryFields(Settings settings){
        String message="";

        if(settings.getGenerateIdentifiers().equalsIgnoreCase("")){
            message+=IdentifierSettingsErrors.COMPULSORY_GENERATEIDENTIFIERS.getErrorMessage();
        }
        if(settings.getUsePersonID().equalsIgnoreCase("")){
            message+=IdentifierSettingsErrors.COMPULSORY_USEPERSONID.getErrorMessage();
        }
        // StudySubjectID must be present
        if(settings.getOCStudySubjectIDDef().equalsIgnoreCase("")){
            message+=IdentifierSettingsErrors.COMPULSORY_STUDYSUBJECTIDDEF.getErrorMessage();
        }
        if(settings.getVisitDateDef().equalsIgnoreCase("")){
            message+=IdentifierSettingsErrors.COMPULSORY_VISITDATEDEF.getErrorMessage();
        }
        if(DateOperations.getDateFormat().equalsIgnoreCase("")){
            message+=IdentifierSettingsErrors.COMPULSORY_DATEFORMAT.getErrorMessage();
        }
//        if(idSettings.getDataFile().equalsIgnoreCase("")){
//            message+=IdentifierSettingsErrors.COMPULSORY_DATAFILE.getErrorMessage();
//        }
        if(settings.getUseDateOfBirth().equalsIgnoreCase("")){
            message+=IdentifierSettingsErrors.COMPULSORY_USEDATEOFBIRTH.getErrorMessage();
        }
        if(settings.getUseGender().equalsIgnoreCase("")){
            message+=IdentifierSettingsErrors.COMPULSORY_USEGENDER.getErrorMessage();
        }
        return message;
    }

    private static String checkGenderFields(Settings settings){
        String message="";
        if(settings.getUseGender().equalsIgnoreCase("y")) {
            if (settings.getGenderDef().equalsIgnoreCase("")) {
                message += IdentifierSettingsErrors.GENDER_NOGENDERDEF.getErrorMessage();
            }
            if (!settings.genderMapContainsValue("m")) {
                message += IdentifierSettingsErrors.GENDER_NOMALECODE.getErrorMessage();
            }
            if (!settings.genderMapContainsValue("f")) {
                message += IdentifierSettingsErrors.GENDER_NOFEMALECODE.getErrorMessage();
            }
        }
        if(settings.getUseGender().equalsIgnoreCase("n")) {
            if (!settings.getGenderDef().equalsIgnoreCase("")) {
                message += IdentifierSettingsErrors.GENDER_GENDERDEF.getErrorMessage();
            }
            if (settings.genderMapContainsValue("m")) {
                message += IdentifierSettingsErrors.GENDER_MALECODE.getErrorMessage();
            }
            if (settings.genderMapContainsValue("f")) {
                message += IdentifierSettingsErrors.GENDER_FEMALECODE.getErrorMessage();
            }
        }
        return message;
    }

    private static String checkDOBFields(Settings settings){
        String message="";
        if(settings.getUseDateOfBirth().equalsIgnoreCase("y")) {
            if (settings.getDOBDef().equalsIgnoreCase("")) {
                message += IdentifierSettingsErrors.DOB_NODOBDEF.getErrorMessage();
            }
            if (settings.getYearOfBirthOnly().equalsIgnoreCase("")) {
                message += IdentifierSettingsErrors.DOB_NOYEAROFBIRTHONLY.getErrorMessage();
            }
        }
        if(settings.getUseDateOfBirth().equalsIgnoreCase("n")) {
            if (!settings.getDOBDef().equalsIgnoreCase("")) {
                message += IdentifierSettingsErrors.DOB_DOBDEF.getErrorMessage();
            }
            if (!settings.getYearOfBirthOnly().equalsIgnoreCase("")) {
                message += IdentifierSettingsErrors.DOB_YEAROFBIRTHONLY.getErrorMessage();
            }
        }
        return message;
    }

    private static String checkGenerateFields(Settings settings){
        if(settings.getGenerateIdentifiers().equalsIgnoreCase("y")){
            return checkGenerateIdentifiersYes(settings);
        }
        else return checkGenerateIdentifiersNo(settings);
    }

    private static String checkGenerateIdentifiersYes(Settings settings){
        String message="";
        // if generateIdentifiers = "y", a StudySubjectIDPrefix is compulsory
        if(settings.getStudySubjectIDPrefix().equalsIgnoreCase("")){
            message+=IdentifierSettingsErrors.GENERATE_NOSTUDYSUBJECTIDPREFIX.getErrorMessage();
        }

        // When using a personID
        if(settings.getUsePersonID().equalsIgnoreCase("y")){
            if(settings.getPersonIDDef().equalsIgnoreCase("")){
                message+=IdentifierSettingsErrors.GENERATE_NOPERSONIDDEF.getErrorMessage();
            }
            if(settings.getPersonIDPrefix().equalsIgnoreCase("")){
                message+=IdentifierSettingsErrors.GENERATE_NOPERSONIDPREFIX.getErrorMessage();
            }
            if(!settings.getRepeatColDef().equalsIgnoreCase("") && settings.getPersonIDExtend().equalsIgnoreCase("")){
                message+=IdentifierSettingsErrors.GENERATE_NOPERSONIDEXTEND.getErrorMessage();
            }
            if(settings.getRepeatColDef().equalsIgnoreCase("") && !settings.getPersonIDExtend().equalsIgnoreCase("")){
                message+=IdentifierSettingsErrors.GENERATE_NOREPEATCOLDEF.getErrorMessage();
            }
        }

        // When not using a personID
        if(settings.getUsePersonID().equalsIgnoreCase("n")){
            if(!settings.getPersonIDDef().equalsIgnoreCase("")){
                message+=IdentifierSettingsErrors.GENERATE_PERSONIDDEF.getErrorMessage();
            }
            if(!settings.getPersonIDPrefix().equalsIgnoreCase("")){
                message+=IdentifierSettingsErrors.GENERATE_PERSONIDPREFIX.getErrorMessage();
            }
            if(!settings.getPersonIDExtend().equalsIgnoreCase("")){
                message+=IdentifierSettingsErrors.GENERATE_PERSONIDEXTEND.getErrorMessage();
            }
        }
        return message;
    }

    private static String checkGenerateIdentifiersNo(Settings settings){
        String message="";

        // if generateIdentifiers = "y", a StudySubjectIDPrefix is compulsory
        if(!settings.getStudySubjectIDPrefix().equalsIgnoreCase("")){
            message+=IdentifierSettingsErrors.GENERATE_STUDYSUBJECTIDPREFIX.getErrorMessage();
        }

        // When using a personID
        if(settings.getUsePersonID().equalsIgnoreCase("y")){
            if(settings.getPersonIDDef().equalsIgnoreCase("")){
                message+=IdentifierSettingsErrors.GENERATE_NOPERSONIDDEF.getErrorMessage();
            }
            if(!settings.getPersonIDPrefix().equalsIgnoreCase("")){
                message+=IdentifierSettingsErrors.GENERATE_PERSONIDPREFIX.getErrorMessage();
            }
        }

        // When not using a personID
        if(settings.getUsePersonID().equalsIgnoreCase("n")){
            if(!settings.getPersonIDDef().equalsIgnoreCase("")){
                message+=IdentifierSettingsErrors.GENERATE_PERSONIDDEF.getErrorMessage();
            }
            if(!settings.getPersonIDPrefix().equalsIgnoreCase("")){
                message+=IdentifierSettingsErrors.GENERATE_PERSONIDPREFIX.getErrorMessage();
            }
        }

        if(!settings.getRepeatColDef().equalsIgnoreCase("")){
            message+=IdentifierSettingsErrors.GENERATE_REPEATCOLNOTPOSSIBLE.getErrorMessage();
        }
        return message;
    }


//    private static String checkFieldTypes(){
//        String message="";
//        return message;
//    }
//
//    private static boolean isYesNoField(String value){
//        return value.equalsIgnoreCase("y") || value.equalsIgnoreCase("n");
//    }

}




enum IdentifierSettingsErrors {
    COMPULSORY_GENERATEIDENTIFIERS("Compulsory setting missing: please specify GenerateIdentifiers (y/n) in your settings"),
    COMPULSORY_USEPERSONID("Compulsory setting missing: please specify UsePersonID (y/n) in your settings"),
    COMPULSORY_STUDYSUBJECTIDDEF("Compulsory setting missing: please specify a StudySubjectIDDef in your settings"),
    COMPULSORY_VISITDATEDEF("Compulsory setting missing: please specify a VisitDateDef in your settings"),
    COMPULSORY_USEDATEOFBIRTH("Compulsory setting missing: please specify UseDateOfBirth (y/n) in your settings"),
    COMPULSORY_USEGENDER("Compulsory setting missing: please specify UseGender (y/n) in your settings"),
    COMPULSORY_DATEFORMAT("Compulsory setting missing: please specify a DateFormat in your settings"),
//    COMPULSORY_DATAFILE("Compulsory setting missing: please specify a DateFile in your settings"),

    GENDER_NOMALECODE("When UseGender='y', please specify GenderMaleCode in your settings. This is compulsory."),
    GENDER_NOFEMALECODE("When UseGender='y', please specify a GenderFemaleCode in your settings. This is compulsory."),
    GENDER_NOGENDERDEF("When UseGender='y', please specify a GenderDef in your settings. This is compulsory."),
    GENDER_MALECODE("When UseGender='n', please do not specify GenderMaleCode in your settings"),
    GENDER_FEMALECODE("When UseGender='n', please do not specify a GenderFemaleCode in your settings"),
    GENDER_GENDERDEF("When UseGender='n', please do not specify a GenderDef in your settings"),

    DOB_NODOBDEF("When UseDateOfBirth='y', please specify DateOfBirthDef in your settings. This is compulsory."),
    DOB_NOYEAROFBIRTHONLY("When UseDateOfBirth='y', please specify YearOfBirthOnly (y/n) in your settings. This is compulsory."),
    DOB_DOBDEF("When UseDateOfBirth='n', please do not specify DateOfBirthDef in your settings"),
    DOB_YEAROFBIRTHONLY("When UseDateOfBirth='n', please do not specify YearOfBirthOnly in your settings"),


    GENERATE_NOSTUDYSUBJECTIDPREFIX("When GenerateIdentifiers = y, please specify a StudySubjectIDPrefix in your settings. This is compulsory"),
    GENERATE_STUDYSUBJECTIDPREFIX("When GenerateIdentifiers = n, please do not specify a StudySubjectIDPrefix in your settings"),

    GENERATE_NOPERSONIDPREFIX("When GenerateIdentifiers = y and UsePersonID=y, please specify a PersonIDPrefix in your settings. This is compulsory"),
    GENERATE_NOPERSONIDEXTEND("When GenerateIdentifiers = y, UsePersonID=y and RepeatColDef is specified,  please specify a PersonIDExtend in your settings. This is compulsory"),
    GENERATE_NOREPEATCOLDEF("When GenerateIdentifiers = y, UsePersonID=y and PersonIDExtend=y ,  please specify a RepeatColDef in your settings. This is compulsory"),

    GENERATE_NOPERSONIDDEF("When UsePersonID=y, please specify a PersonIDDef in your settings. This is compulsory"),
    GENERATE_PERSONIDPREFIX("When UsePersonID=n, please do not specify a PersonIDPrefix in your settings"),
    GENERATE_PERSONIDDEF("When UsePersonID=n, please do not specify a PersonIDDef in your settings"),
    GENERATE_PERSONIDEXTEND("When and UsePersonID=n, please do not specify a PersonIDExtend in your settings"),

    GENERATE_REPEATCOLNOTPOSSIBLE("When GenerateIdentifiers = n, please do not specify a RepeatColDef")
    ;

    private IdentifierSettingsErrors(String errorMessage){
        this.errorMessage=errorMessage;
    }

    String getErrorMessage(){
        fatalError=true;
        return errorMessage+"\n";
    }

    static boolean getFatalError(){
        return fatalError;
    }


    private final String errorMessage;
    private static boolean fatalError=false;

//    private static final Logger log = Shared.getLogger(IdentifierSettingsErrors.class.getName());
}