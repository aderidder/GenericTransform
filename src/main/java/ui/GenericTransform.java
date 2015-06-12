package ui;

//import identifiers.settings.IdentifierSettings;
//import identifiers.identifiers.Identifiers;
//import data.InputData;
//import data.OutputData;
//import shared.Shared;
//import template.Template;
import ui.gui.GenericTransformGUI;

import javax.swing.*;

class GenericTransform {

//	private static void runTransform(String settingsFile, String templateName, int visit){
//        // read the identifier settings
//		IdentifierSettings identifierSettings = new IdentifierSettings(settingsFile, visit);
//		Shared.setIdentifierSettings(identifierSettings);
//
//        // read the dataFile
//		InputData inputData = InputData.readDataFile(identifierSettings.getDataDir()+identifierSettings.getDataFile());
//
//        //
//		Identifiers identifiers = new Identifiers(identifierSettings);
//		identifiers.readIdentifierFile();
//		identifiers.generateIdentifiers(inputData, false);
//
//		Template template = new Template(templateName, identifiers);
//		OutputData outputData = new OutputData(template, identifiers);
//		outputData.generateData(inputData, visit);
//
//		identifiers.writeIdentifierFile();
//		identifiers.writeOCPatientEventRegistration();
//
//        outputData.writeData(identifierSettings.getBaseDir(), identifierSettings.getDataOutFileName(template));
//	}

//    private static void runTransform(){
//		int visit=1;
//
////		String settingsFile = "D:\\data\\upload\\Nijmegen\\test\\settings\\settingsERA.txt";
////		String templateName = "EQ5DTemplate.txt";
//
//        String settingsFile = "D:\\data\\upload\\Nijmegen\\test\\settings\\settingsERAMed.txt";
//        String templateName = "ERA_medPunctiesTemplate.txt";
//
//        runTransform(settingsFile, templateName, visit);
//    }


	public static void main(String ... args){
//		Logging.loggerSetLevel(Level.FINEST);
//		runTransform();
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			javax.swing.SwingUtilities.invokeLater(GenericTransformGUI::createAndShowGUI);
		});
	}

}
