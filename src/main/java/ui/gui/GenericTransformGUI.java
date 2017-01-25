package ui.gui;

import data.InputData;
import data.OutputData;
import identifiers.identifiers.Identifiers;
import identifiers.settings.FileSettings;
import identifiers.settings.Settings;
import shared.FileOperations;
import shared.Logging;
import shared.Shared;
import template.Template;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GenericTransformGUI extends JPanel implements ActionListener{

    // The GUI is a JPanel

    GenericTransformGUI(){
        // Setup our panel with a borderlayout
        super(new BorderLayout());
        setup();
    }

    private void setup(){
        //Create a file chooser and filters for xml and html
        templateFilter = new FileNameExtensionFilter("template file", "txt");
        settingsFilter = new FileNameExtensionFilter("settings file", "txt");
        dataFilter = new FileNameExtensionFilter("data file", "txt");

        // create the uneditable log area with a scroll pane
        logArea = new JTextArea(30,150);
        logArea.setEditable(false);
        logArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        FileOperations.setLogArea(logArea);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        // Create the top panel and the bottom panel
        JPanel topPanel = topPaneSetup();
        JPanel bottomPanel = bottomPaneSetup();

        // Add the panels to the borderlayout
        add(topPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.PAGE_END);

        Shared.setLogArea(logArea);
        Logging.addWindowHandler(logArea);
    }

    // create the menubar
    private JMenuBar createMenuBar(){
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;
        menuBar = new JMenuBar();

        // Build the File menu
        menu = new JMenu("File");
        menuItem = new JMenuItem("Exit");
        menuItem.setActionCommand("Exit");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        // Build the About menu
        menu = new JMenu("About");
        menuItem = new JMenuItem("Help");
        menuItem.setActionCommand("Help");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("License");
        menuItem.setActionCommand("License");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("About");
        menuItem.setActionCommand("About");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        // Return the menuBar
        return menuBar;
    }

    // create the bottom panel
    private JPanel bottomPaneSetup(){
        JPanel buttonPanel = new JPanel();
        // create the Run Button
        JButton button = new JButton("Run");
        button.setPreferredSize(new Dimension(150, 30));
        button.setActionCommand("Run");
        button.addActionListener(this);
        buttonPanel.add(button);

        // create the Exit Button
        button = new JButton("Exit");
        button.setPreferredSize(new Dimension(150,30));
        button.setActionCommand("Exit");
        button.addActionListener(this);
        buttonPanel.add(button);

        return buttonPanel;
    }

    // create standard GridBagConstraints
    private static GridBagConstraints getDefaultConstraints(){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // set the xweight for the component. As all components (unless overwritten) have
        // the same weight, they will all have the same sizes
        gridBagConstraints.weightx = 0.5;
        // margins
        gridBagConstraints.insets = new Insets(3,3,3,3);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        return gridBagConstraints;
    }

    private JTextField addBrowseRow(String textLabel, int y, JPanel pane, String actionCommand){
        JButton button;
        JTextField textField;
        GridBagConstraints gridBagConstraints;
        JLabel label = new JLabel(textLabel);
        gridBagConstraints = getDefaultConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = y;
        pane.add(label, gridBagConstraints);

        // textfield
        textField = new JTextField(20);
        gridBagConstraints = getDefaultConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = y;
        pane.add(textField, gridBagConstraints);

        // button
        button = new JButton("Browse");
        gridBagConstraints = getDefaultConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = y;
        gridBagConstraints.weightx = 0.2; // smaller weight
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
        pane.add(button, gridBagConstraints);
        return textField;
    }

    // create the top Panel
    private JPanel topPaneSetup(){
        JPanel pane = new JPanel();

        // set the layout to a gridbaglayout
        pane.setLayout(new GridBagLayout());

        baseDirTextField = addBrowseRow("Base Directory", 0, pane, "BrowseBaseDir");
        settingsFileTextField = addBrowseRow("Settings File", 1, pane, "BrowseSettingsFile");
        dataFileTextField = addBrowseRow("Data File", 2, pane, "BrowseDataFile");
        templateFileTextField = addBrowseRow("Template File", 3, pane, "BrowseTemplateFile");


        return pane;
    }


    // create and show the GUI
    public static void createAndShowGUI(){
        // create the frame
        JFrame frame = new JFrame("Generic Transform");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // create our panel and add it to the frame
        GenericTransformGUI translateGUI = new GenericTransformGUI();
        frame.add(translateGUI);
        // add the menubar to the frame
        frame.setJMenuBar(translateGUI.createMenuBar());

        // change the icon to a ctmm icon
        URL url = FileOperations.getImageURL("ctmmSymbol.jpg");
        ImageIcon img = new ImageIcon(url);
        frame.setIconImage(img.getImage());

        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private String getTextFieldValue(JTextField textField){
        return textField.getText().trim();
    }


    // run the translator
    private void runProgram(){
        String baseDir = getTextFieldValue(baseDirTextField);
        String settingsFile = getTextFieldValue(settingsFileTextField);
        String templateFile = getTextFieldValue(templateFileTextField);
        String dataFile = getTextFieldValue(dataFileTextField);

        if(!templateFile.equalsIgnoreCase("")&&!settingsFile.equalsIgnoreCase("")&&!dataFile.equalsIgnoreCase("")){
            // ensure the directory variable is set properly
//                dir = templateFile.substring(0,templateFile.lastIndexOf(fileSeparator));
            try{
//                String visitNr = PopUps.visitNrPopup();
                List<String> eventInfo =PopUps.visitNrPopup();

                if(eventInfo!=null && !eventInfo.get(0).equalsIgnoreCase("") && !eventInfo.get(1).equalsIgnoreCase("")) {
//                        Logging.addLogFileHandler(baseDir);
                    new TransformWorker(logArea, baseDir, settingsFile, templateFile, dataFile, eventInfo).execute();
                }

            } catch(Exception e){
                // show message if not successful
                logArea.append(e.toString());
                JOptionPane.showMessageDialog(this, "The rule translator encountered a problem:"+newline+e.toString(),
                        "There was a problem", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            log.log(Level.SEVERE, "Please specify files and provide a proper visitNr");
        }
    }

    // show help
    private void showHelp(){
        Help.showHelp();
    }

    private String getDir(String subDir){
        String baseDir = getTextFieldValue(baseDirTextField);
        if(!baseDir.equalsIgnoreCase("")){
            if(!baseDir.endsWith(fileSeparator)) return baseDir+fileSeparator+subDir;
            return baseDir+subDir;
        }
        return "";
    }

    // handle actions
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("BrowseBaseDir")) {
            JFileChooser fileChooser = PopUps.getDirectoryChooser(getTextFieldValue(baseDirTextField));
            PopUps.browse(baseDirTextField, fileChooser, this);
        }
        // Handle open browse button for rules file.
        else if (e.getActionCommand().equalsIgnoreCase("BrowseTemplateFile")) {
            JFileChooser fileChooser = PopUps.getFileChooser(templateFilter, getDir(FileSettings.getTemplateDir()));
            PopUps.browse(templateFileTextField, fileChooser, this);
        }
        // Handle open browse button for crfVersion file.
        else if (e.getActionCommand().equalsIgnoreCase("BrowseSettingsFile")) {
            JFileChooser fileChooser = PopUps.getFileChooser(settingsFilter, getDir(FileSettings.getSettingsDir()));
            PopUps.browse(settingsFileTextField, fileChooser, this);
        }
        else if (e.getActionCommand().equalsIgnoreCase("BrowseDataFile")) {
            JFileChooser fileChooser = PopUps.getFileChooser(dataFilter, getDir(FileSettings.getDataDir()));
            PopUps.browse(dataFileTextField, fileChooser, this);
        }
        // Handle run
        // should make a separate thread for this
        else if (e.getActionCommand().equalsIgnoreCase("Run")) {
            logArea.setText("");
            runProgram();
        }
        // Handle exit
        else if (e.getActionCommand().equalsIgnoreCase("Exit")) {
            System.exit(0);
        }
        else if (e.getActionCommand().equalsIgnoreCase("License")) {
            PopUps.showLicense(this);
        }
        // Handle about
        else if (e.getActionCommand().equalsIgnoreCase("About")) {
            PopUps.showAbout(this);
        }
        // Handle help
        else if (e.getActionCommand().equalsIgnoreCase("Help")) {
            showHelp();
        }
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private JTextField baseDirTextField, settingsFileTextField, templateFileTextField, dataFileTextField;
    private JTextArea logArea;
    private FileNameExtensionFilter templateFilter, settingsFilter, dataFilter;

    private Logger log = Logging.getLogger();

    private static final String fileSeparator = Shared.fileSeparator;
    private static final String newline = Shared.newLine;
    //	private static final long serialVersionUID = -7755429999545798394L;
}

class TransformWorker extends SwingWorker<Integer, String> {
    TransformWorker(JTextArea logArea, String baseDir, String settingsFile, String templateName, String dataFile, List<String> visitInfo){
        this.logArea = logArea;
        this.baseDir=baseDir;
        this.settingsFile=settingsFile;
        this.templateName=templateName;
        this.dataFile=dataFile;
//        this.visit=visit;
        this.visitInfo = visitInfo;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        Logging.addLogFileHandler(baseDir);
        logArea.append("Running GenericTransform..." + newline);
        runTransform();
        logArea.append("Done!" + newline);
        Logging.removeLogFileHandler();
        logArea.setCaretPosition(logArea.getDocument().getLength());
        return 1;
    }

    private void runTransform(){
        logArea.append("Reading the settings..." + newline);
        // read the settings
        Settings settings = new Settings(baseDir, settingsFile, dataFile);
        Shared.setSettings(settings);

        logArea.append("Reading data..." + newline);
        // read the dataFile
        InputData inputData = InputData.readDataFile(settings.getDataDir()+ settings.getDataFile());

        logArea.append("Reading/Generating identifiers..." + newline);
        // read and generate the identifiers
        Identifiers identifiers = new Identifiers(settings);
        identifiers.readIdentifierFile();
        identifiers.generateIdentifiers(inputData, false);

        logArea.append("Reading the template..." + newline);
        // read the template and expand it for repeating groups
        Template template = new Template(templateName, identifiers);
        template.expandTemplate(inputData);

        //TODO for this test it's hardcoded
        settings.setVisitName(visitInfo.get(0));

        int [] visitRange = getVisitRange(visitInfo.get(1), identifiers);

        logArea.append("Generating data..." + newline);
        for(int visitNr = visitRange[0]; visitNr<=visitRange[1]; visitNr++) {
//            logArea.setCaretPosition(logArea.getDocument().getLength());
            logArea.append(newline+"**** VisitNr "+visitNr+" ****"+newline);
            settings.setVisitNr(visitNr);
            // create output data
            OutputData outputData = new OutputData(template, identifiers);
            outputData.generateData(inputData, visitNr);

            // write identifiers, registration and data
            identifiers.writeIdentifierFile();
//            identifiers.writeOCPatientEventRegistration();
            identifiers.writeOCDUEventRegistrationFile();
            identifiers.writeOCDUSubjectRegistrationFile();
            outputData.writeData(settings.getFullOutDir(), settings.getDataOutFileName(template));
        }
    }

    private int[] getVisitRange(String visit, Identifiers identifiers){
        int [] range = new int[2];
        if(visit.equalsIgnoreCase("")){
            range[0]=1;
            range[1]=identifiers.getMaxNrVisits();
        }
        else {
            String[] splitVisit = visit.split("-");
            range[0] = Integer.parseInt(splitVisit[0]);
            if (splitVisit.length == 2) range[1] = Integer.parseInt(splitVisit[1]);
            else range[1] = Integer.parseInt(splitVisit[0]);
        }
        return range;
    }

    private static final String newline = Shared.newLine;
    private final JTextArea logArea;
    String baseDir;
    String settingsFile;
    String templateName;
    String dataFile;
//    String visit;
    List<String> visitInfo;
}