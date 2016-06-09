package ui;

import ui.gui.GenericTransformGUI;

import javax.swing.*;

class GenericTransform {

	public static void main(String ... args){
//		Logging.loggerSetLevel(Level.FINEST);
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			javax.swing.SwingUtilities.invokeLater(GenericTransformGUI::createAndShowGUI);
		});
	}

}
