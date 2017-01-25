package ui.gui;

import shared.FileOperations;
import shared.Shared;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class PopUps {


    // show about information
    public static void showAbout(JPanel panel){
            // create image
            URL url = FileOperations.getImageURL("tracer_trait_vumc.jpg");
            ImageIcon img = new ImageIcon(url);
            // show dialog
            JOptionPane.showMessageDialog(panel,
                    "Developed at the VU University Medical Center for CTMM TRACER by TRACER ICT"+newline+
                            "Developer: Sander de Ridder"+newline+
                            "WP-lead: Jeroen Beliën"+newline+
                            "Tested by: Rinus Voorham"+newline+
                            "Program version "+version+newline+newline,
                    "About",
                    JOptionPane.INFORMATION_MESSAGE, img);
    }

    public static void showLicense(JPanel panel){
            URL url = FileOperations.getImageURL("CTMM VUmc.jpg");
            ImageIcon img = new ImageIcon(url);
            // show dialog
            JOptionPane.showMessageDialog(panel,
                    "This program is Copyright 2015 VU University Medical Center / Center for Translational Molecular Medicine, " +newline+
                            "and can be reused under the Apache 2.0 license. Unless required by applicable law or agreed to in writing, "+newline+
                            "software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR " +newline+
                            "CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing " +newline+
                            "permissions and limitations under the License."+newline+
                            "You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0",
                    "License",
                    JOptionPane.INFORMATION_MESSAGE, img);
    }

    // create a new filechooser based on the directory and the filter
    public static JFileChooser getFileChooser(FileNameExtensionFilter filter, String dir){
        JFileChooser fileChooser = new JFileChooser(dir);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(filter);
        return fileChooser;
    }

    // create a new filechooser based on the directory and the filter
    public static JFileChooser getDirectoryChooser(String dir){
        JFileChooser fileChooser = new JFileChooser(dir);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        return fileChooser;
    }

    // browse file
    public static void browse(JTextField textField, JFileChooser fileChooser, JPanel panel){
        // show the fileChooser
        int returnVal = fileChooser.showOpenDialog(panel);

        // if ok is pressed, set the textfield to the selected file and store the directory
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if(fileChooser.getFileSelectionMode()==JFileChooser.DIRECTORIES_ONLY)
                textField.setText(selectedFile.getAbsolutePath());
            else
                textField.setText(selectedFile.getName());
        }
    }

    public static List<String> visitNrPopup() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField eventName = new JTextField(10);
        eventName.addAncestorListener( new RequestFocusListener() );
        JTextField eventNumber = new JTextField(10);

        panel.add(new JLabel("Event name:"));
        panel.add(eventName);
        panel.add(new JLabel("Event number: "));
        panel.add(eventNumber);
        eventName.requestFocusInWindow();
        JOptionPane.showConfirmDialog(null, panel, "Event info: ", JOptionPane.OK_CANCEL_OPTION);

        return Arrays.asList(eventName.getText(), eventNumber.getText());
    }

    private static String newline = Shared.newLine;
    private static final double version = 1.0;
}

// Taken from https://tips4java.wordpress.com/2010/03/14/dialog-focus/
// This allows us to give focus to the Dialog's evenName field
class RequestFocusListener implements AncestorListener
{
    private boolean removeListener;

    /*
     *  Convenience constructor. The listener is only used once and then it is
     *  removed from the component.
     */
    public RequestFocusListener()
    {
        this(true);
    }

    /*
     *  Constructor that controls whether this listen can be used once or
     *  multiple times.
     *
     *  @param removeListener when true this listener is only invoked once
     *                        otherwise it can be invoked multiple times.
     */
    public RequestFocusListener(boolean removeListener)
    {
        this.removeListener = removeListener;
    }

    @Override
    public void ancestorAdded(AncestorEvent e)
    {
        JComponent component = e.getComponent();
        component.requestFocusInWindow();

        if (removeListener)
            component.removeAncestorListener((AncestorListener) this);
    }

    @Override
    public void ancestorMoved(AncestorEvent e) {}

    @Override
    public void ancestorRemoved(AncestorEvent e) {}
}