package identifiers.settings;

import shared.DateOperations;
import shared.Shared;
import template.Template;

public class FileSettings {
    FileSettings(String baseDir, String settingsFile, String dataFile){
        this.settingsFile = settingsFile;
        this.dataFile = dataFile;
        setBaseDir(baseDir);
    }
//
//    public void addLogFileHandler(){
//        if(FileOperations.createDirectory(getFullLogDir()))
//            Logging.addFileHandler(getFullLogDir()+"logFile" + DateOperations.getNow() + ".txt");
//    }

    private void setBaseDir(String baseDir){
        if(!baseDir.endsWith(Shared.fileSeparator)) baseDir += Shared.fileSeparator;
        this.baseDir = baseDir;
    }

    String getIDOutFileName(){
        if (customIDOutFileName.equalsIgnoreCase("")) return "idFile_"+ DateOperations.getNow()+".txt";
        return customIDOutFileName;
    }
//    String getRegOutFileName(){
//        if (customRegOutFileName.equalsIgnoreCase("")) return "registrationFile_"+ DateOperations.getNow()+".txt";
//        return customRegOutFileName;
//    }
    String getOCDUSubjectRegFile(){
        return "ocdiSubjectRegistrationFile_"+ DateOperations.getNow()+".txt";
    }

    String getOCDUEventRegFile(){
        return "ocdiEventRegistrationFile_"+ DateOperations.getNow()+".txt";
    }

    String getDataOutFileName(Template template, int visit){
        if(customDataOutFileName.equalsIgnoreCase("")){
            String templateName = template.getTemplateName();
            return templateName.substring(0,templateName.lastIndexOf("."))+"_Out_"+visit+".txt";
        }
        return customDataOutFileName;
    }
    String getIdentifierFile(){
        return identifierFile;
    }
    String getDataFile(){
        return dataFile;
    }
    String getSettingsFile(){
        return settingsFile;
    }
    String getBaseDir(){
        return baseDir;
    }

    String getFullOutDir(){
        return baseDir+outDir;
    }
    String getFullDataDir(){
        return baseDir+dataDir;
    }
    String getFullTemplateDir(){
        return baseDir+templateDir;
    }
    String getFullSettingsDir(){
        return baseDir+settingsDir;
    }
//    String getFullLogDir(){
//        return baseDir+logDir;
//    }


//    void setDataFile(String dataFile){
//        this.dataFile = dataFile;
//    }
    void setIdentifierFile(String identifierFile) {
        this.identifierFile = identifierFile;
    }
//    void setBaseDir(String baseDir){
////        baseDir = settingsFile.substring(0, settingsFile.indexOf(settingsDir));
//        this.baseDir = baseDir;
//    }
    void setCustomIDOutFileName(String customIDOutFileName){
        this.customIDOutFileName = customIDOutFileName;
    }
    void setCustomRegOutFileName(String customRegOutFileName){
        this.customRegOutFileName = customRegOutFileName;
    }
    void setCustomDataOutFileName(String customDataOutFileName){
        this.customDataOutFileName = customDataOutFileName;
    }

//    public static String getOutDir(){
//        return outDir;
//    }
    public static String getLogDir(){
        return logDir;
    }
    public static String getSettingsDir(){
        return settingsDir;
    }
    public static String getDataDir(){
        return dataDir;
    }
    public static String getTemplateDir(){
        return templateDir;
    }
    public static String getTestResultDir(){
        return testResultDir;
    }

    // directory variables. Assumes a baseDir with settings, data and template subdirectories
    private String baseDir;
    private String dataFile;
    private String settingsFile;
    private String identifierFile="";
    private String customIDOutFileName="";
    private String customRegOutFileName="";
    private String customDataOutFileName="";

    private static final String outDir = "out"+ Shared.fileSeparator;
    private static final String logDir = "log"+Shared.fileSeparator;
    private static final String settingsDir = "settings"+Shared.fileSeparator;
    private static final String dataDir = "data"+Shared.fileSeparator;
    private static final String templateDir = "template"+Shared.fileSeparator;
    private static final String testResultDir = "testResult"+Shared.fileSeparator;


}
