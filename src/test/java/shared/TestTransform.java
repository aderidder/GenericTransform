package shared;

import data.InputData;
import data.OutputData;
import identifiers.identifiers.Identifiers;
import identifiers.settings.Settings;
import template.Template;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class TestTransform {

    public static boolean runTest(String type, int testNr){
        problemFound = false;
        switch (type){
            case "id": runIDTest(testNr); break;
            case "template": runTemplateTest(testNr); break;
        }
        return problemFound;
    }

    private static void runIDTest(int testNr){
        URL url;
//        log.log(Level.INFO, "Running id test {0}", testNr);
        switch(testNr){
            case 1: case 2: case 5: case 6: case 7: case 9: case 10: case 13: case 14:
                url = TestTransform.class.getResource("/TemplateTests/identifiers/"+testNr);
                identifierTest(url.getFile().substring(1), "settings.txt", "testData.txt");
                break;
            case 3: case 4: case 8: case 11: case 12: case 15: case 16:
                url = TestTransform.class.getResource("/TemplateTests/identifiers/"+testNr);
                identifierTest(url.getFile().substring(1), "settings1.txt", "testData1.txt");
                identifierTest(url.getFile().substring(1), "settings2.txt", "testData2.txt");
                break;
            default:
                log.log(Level.SEVERE, "testNr {0} not supported for runIDTest", testNr);
                problemFound=true;
        }
    }

    private static void runTemplateTest(int testNr){
        try{
        URL url;
        switch(testNr){
            case 1: case 2: case 3: case 4: case 5: case 6: case 7:case 10:
                url = TestTransform.class.getResource("/TemplateTests/template/"+testNr);
//                url = TestTransform.class.getResource("/TemplateTests/template/"+testNr+"/settings/settings.txt");
                templateTest(url.getFile().substring(1), "settings.txt", "testData.txt", "basicTemplate.txt", 1);
                break;
            case 8: case 9:
//                url = TestTransform.class.getResource("/TemplateTests/template/"+testNr+"/settings/settings.txt");
//                templateTest(url.getFile(), "basicTemplate.txt", 2);
                url = TestTransform.class.getResource("/TemplateTests/template/"+testNr);
                templateTest(url.getFile().substring(1), "settings.txt", "testData.txt", "basicTemplate.txt", 2);
                break;
            default:
                log.log(Level.SEVERE, "testNr {0} not supported for runTemplateTest", testNr);
                problemFound=true;
        }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void identifierTest(String baseDir, String settingsFile, String dataFile){
        Logging.addLogFileHandler(baseDir);
//        settingsFile = settingsFile.replace("/", "\\");
        Settings settings = new Settings(baseDir, settingsFile, dataFile);
        settings.setVisitNr(1);
        Shared.setSettings(settings);

        InputData inputData = InputData.readDataFile(settings.getDataDir()+ settings.getDataFile());

        Identifiers identifiers = new Identifiers(settings);
        identifiers.readIdentifierFile();
        identifiers.generateIdentifiers(inputData, false);

        identifiers.writeIdentifierFile();
        identifiers.writeOCPatientEventRegistration();

        testIDFiles(settings);
        testRegFiles(settings);
        Logging.removeLogFileHandler();
    }

    private static void templateTest(String baseDir, String settingsFile, String dataFile, String templateName, int visit){
//        settingsFile = settingsFile.replace("/", "\\");
        Logging.addLogFileHandler(baseDir);
        Settings settings = new Settings(baseDir, settingsFile, dataFile);
        settings.setVisitNr(visit);
        Shared.setSettings(settings);

        InputData inputData = InputData.readDataFile(settings.getDataDir()+ settings.getDataFile());

        Identifiers identifiers = new Identifiers(settings);
        identifiers.readIdentifierFile();
        identifiers.generateIdentifiers(inputData, false);

        Template template = new Template(templateName, identifiers);
        template.expandTemplate(inputData);

        OutputData outputData = new OutputData(template, identifiers);
        outputData.generateData(inputData, visit);

        identifiers.writeIdentifierFile();
        identifiers.writeOCPatientEventRegistration();

        outputData.writeData(settings.getBaseDir(), settings.getDataOutFileName(template));

        testIDFiles(settings);
        testRegFiles(settings);
        testDataFiles(settings, template);
        Logging.removeLogFileHandler();
    }

    private static void testIDFiles(Settings settings){
        compareFiles(settings.getBaseDir()+ settings.getTestResultDir()+ settings.getIDOutFileName(), settings.getBaseDir()+ settings.getIDOutFileName());
    }

    private static void testRegFiles(Settings settings){
        compareFiles(settings.getBaseDir()+ settings.getTestResultDir()+ settings.getRegOutFileName(), settings.getBaseDir()+ settings.getRegOutFileName());
    }

    private static void testDataFiles(Settings settings, Template template){
        compareFiles(settings.getBaseDir()+ settings.getTestResultDir()+ settings.getDataOutFileName(template), settings.getBaseDir()+ settings.getDataOutFileName(template));
    }

    private static void compareFiles(String fileName1, String fileName2){
        List<String> list1 = readFile(fileName1);
        List<String> list2 = readFile(fileName2);
        if(list1.size()<list2.size()) compareLines(list2, list1);
        else compareLines(list1, list2);
    }

    private static void compareLines(List<String> list1, List<String> list2){
        String line1, line2;
        for(int i=0; i<list1.size(); i++){
            line1 = list1.get(i);
            line2 = list2.get(i);
            if(!line1.equalsIgnoreCase(line2)){
                log.log(Level.SEVERE, "different lines: \n\t{0}\n\t{1}", new Object[]{line1, line2});
                problemFound=true;
            }
        }
    }

    private static List<String> readFile(String fileName){
        List<String> myList = new ArrayList<>();
        BufferedReader bufferedReader = FileOperations.openFileReader(fileName);
        String line;
        try {
            while((line=bufferedReader.readLine())!=null){
                myList.add(line);
            }
        } catch (IOException e) {
            problemFound=true;
        } finally{
            FileOperations.closeFileReader(bufferedReader);
        }
        return myList;
    }

    private static boolean problemFound;
    private static final Logger log = Logging.getLogger();
}
