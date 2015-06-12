/*
 * Copyright (c) 2015 VU University Medical Center / Center for Translational Molecular Medicine.
 * Licensed under the Apache License version 2.0 (see http://www.apache.org/licenses/LICENSE-2.0.html)
 */

package ui.gui;

import shared.FileOperations;

import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// help is also a panel
class Help extends JPanel implements ListSelectionListener {
	private Help(){
		// panel with borderlayout
		super(new BorderLayout());
		setup();
	}

	private void setup(){
		// setup the help panel
		JSplitPane helpPanel = helpPaneSetup();
		// add the help panel to the center of the borderlayout
		add(helpPanel, BorderLayout.CENTER);
	}

	// setup the help panel, which is a splitpane
	// on the left we will have the topics; on the right the text for the selected topic
	private JSplitPane helpPaneSetup(){
		// create a new list based on the help topics
		// the model is single select and we start by setting the selected index to the first topic
		JList<Object> list = new JList<>(helpItems.stream().map(e->e.getTitle()).toArray());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);

		// create a scrollpanel for the list
		JScrollPane listScrollPane = new JScrollPane(list);

		// create an editorpanel which accepts html
		rightArea = new JEditorPane();
		rightArea.setContentType("text/html");
		rightArea.setEditable(false);

		// add the panel to a scroll pane
		JScrollPane rightScrollPane = new JScrollPane(rightArea);

		//Create the split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, rightScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		//Provide minimum sizes for the two components in the split pane.
		Dimension minimumSize = new Dimension(200, 100);
		listScrollPane.setMinimumSize(minimumSize);
		rightScrollPane.setMinimumSize(minimumSize);

		//Provide a preferred size for the split pane.
		splitPane.setPreferredSize(new Dimension(1000, 400));

		// show the first help topic
		showHelp(list.getSelectedIndex());
		return splitPane;
	}

	// close the window
	private static void closeWindow(){
		if(helpAlreadyShown){
			helpAlreadyShown = false;
			frame.dispose();
		}
	}

	// create and show the GUI
	private static void createAndShowGUI(){
		// used for check to see if a help window is already open
		helpAlreadyShown = true;
		
		// create the frame and set the close operation to do nothing
        frame = new JFrame("Help");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// add the window listener and call closeWindow when a windowclosing event occurs
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				closeWindow();
			}
		});

		// create the help panel and add it to the frame
		Help help = new Help(); 
		frame.add(help);

		// add the ctmm icon to the frame
		URL url = FileOperations.getImageURL("ctmmSymbol.jpg");
		ImageIcon img = new ImageIcon(url);
		frame.setIconImage(img.getImage());

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	// show help, based on the selected index
	private void showHelp(int index){
		rightArea.setText(helpItems.get(index).getText());
		rightArea.setCaretPosition(0);
	}

	// event listener for list
	// If someone clicks a different section, change the window content
	public void valueChanged(ListSelectionEvent e) {
		JList<?> list = (JList<?>)e.getSource();
		showHelp(list.getSelectedIndex());
	}

	// create the help items and return a list with the help items
	private static List<HelpItem> createHelpItems(){
		List<HelpItem> helpItems = new ArrayList<>();
		// Create HelpItem by providing a section title and the file to read with its contents
		helpItems.add(new HelpItem("1.General", "general.txt"));
		helpItems.add(new HelpItem("2.Directory Structure", "dirStructure.txt"));
		helpItems.add(new HelpItem("3.Data File", "dataFile.txt"));
		helpItems.add(new HelpItem("4.Settings File", "settingsFile.txt"));
		helpItems.add(new HelpItem("5.Template File", "templateFile.txt"));
		helpItems.add(new HelpItem("6.Running the Program", "usage.txt"));
		helpItems.add(new HelpItem("7.Simple Example", "example1.txt"));
		helpItems.add(new HelpItem("8.Simple Example Group", "example2.txt"));
		helpItems.add(new HelpItem("9.More Complex Example", "example3.txt"));
		return helpItems;
	}

	// show help
	public static void showHelp(){
		// check whether a helpwindow already exists. If not create a new one, otherwise send it to front.
		if(!helpAlreadyShown) javax.swing.SwingUtilities.invokeLater(Help::createAndShowGUI);
		else frame.toFront();
	}

    private JEditorPane rightArea;
	private static JFrame frame;
	private static boolean helpAlreadyShown = false;
	private static List<HelpItem> helpItems = createHelpItems();
//	private static final long serialVersionUID = 331623305138507005L;

}

class HelpItem{
	HelpItem(String title, String source){
		this.title = title;
		text = setHelpText(source);
	}

	String getTitle(){
		return title;
	}

	String getText(){
		return text;
	}

	// Reads a helpfile using the textURL
	private static String setHelpText(String textURL){
		Matcher matcher;
		try {
			// using an inputstream to read resource
			InputStream inputStream = FileOperations.getHTMLInputStream(textURL);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			// stringbuilder more efficient than concatenating string every time.
			StringBuilder out = new StringBuilder();
			String line, source;
			// read the file
			while ((line = reader.readLine()) != null) {
				// replace the src by the resource version of the source
				matcher = sourcePattern.matcher(line);
				if(matcher.matches()) {
					source=matcher.group(1);
					line = line.replace(source, FileOperations.getImageURLString(source));
				}
				out.append(line);
			}
			return out.toString();
		}catch (Exception e){
			e.printStackTrace();
		}
		return "";
	}


	private String title;
	private String text;
	// pattern to look for a src link, which we need to replace
	private static final Pattern sourcePattern = Pattern.compile(".*src=\"(.*?)\".*");
}