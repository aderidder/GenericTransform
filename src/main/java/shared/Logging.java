package shared;

import identifiers.settings.FileSettings;

import javax.swing.*;
import java.util.logging.*;

public class Logging {

//    public static void addLogFileHandler(Settings settings) {
    public static void addLogFileHandler(String baseDir) {
        if(!baseDir.endsWith(Shared.fileSeparator)) baseDir+=Shared.fileSeparator;
        String fullLogDir = baseDir+FileSettings.getLogDir();
        if (FileOperations.createDirectory(fullLogDir))
            addFileHandler(fullLogDir + "logFile" + DateOperations.getNow() + ".txt");
    }

    // fileHandler setup. Level for Handler is Level.INFO
    private static void addFileHandler(String fileName){
        try{
            if(fileHandler==null) {
                Logger logger = Logger.getLogger("GenericTransform");
                fileHandler = new FileHandler(fileName);
                fileHandler.setLevel(Level.INFO);
                fileHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(fileHandler);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void removeLogFileHandler(){
        if(fileHandler!=null) {
            Logger logger = Logger.getLogger("GenericTransform");
            fileHandler.flush();
            fileHandler.close();
            logger.removeHandler(fileHandler);
            fileHandler = null;
        }
    }

    public static void addWindowHandler(JTextArea logArea){
        if(windowHandler==null) {
            Logger logger = Logger.getLogger("GenericTransform");
            windowHandler = new WindowHandler(logArea);
            windowHandler.setLevel(Level.INFO);
            windowHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(windowHandler);
        }
    }



    // method to create logger
    public static Logger getLogger(){
        return Logger.getLogger("GenericTransform");

    }

    public static boolean writeToScreen(){
        return writeToScreen;
    }
    public static void setWriteToScreen(boolean writeToScreen){
        Logging.writeToScreen = writeToScreen;
    }

    private static boolean writeToScreen = true;
//    private static Handler consoleHandler = new ConsoleHandler();
    private static FileHandler fileHandler=null;
    private static WindowHandler windowHandler=null;

    // Logger and consoleHandler setup
    // Level for Handler is Level.SEVERE
    // Level for logger is Level.INFO, as we want to give more information for the fileHandler
    static{
        Logger logger = Logger.getLogger("GenericTransform");
//        consoleHandler.setLevel(Level.SEVERE);
        logger.setUseParentHandlers(false);
//        logger.addHandler(consoleHandler);
        logger.setLevel(Level.INFO);
    }

}

class WindowHandler  extends StreamHandler {
    WindowHandler(JTextArea logArea){
        this.logArea = logArea;
    }

    @Override
    public void publish(LogRecord record) {
        super.publish(record);
        flush();

        if (logArea != null) {
            logArea.append(getFormatter().format(record));
        }
    }

    private JTextArea logArea;
}