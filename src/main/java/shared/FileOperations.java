package shared;


// SharedFileOperations.java
// Class for file operations

import org.mozilla.universalchardet.UniversalDetector;
import ui.gui.GenericTransformGUI;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileOperations{
//	public static String getHTMLURLString(String fileName){
//		return getURLString("/html/", fileName);
//	}

	public static String getImageURLString(String fileName){
		return getURLString("/images/", fileName);
	}

	private static String getURLString(String path, String fileName){
		URL url = GenericTransformGUI.class.getResource(path +fileName);
		return url.toString();
	}

	public static URL getImageURL(String fileName){
		return getURL("/images/", fileName);
	}

	public static InputStream getHTMLInputStream(String fileName){
		return GenericTransformGUI.class.getResourceAsStream("/html/"+fileName);
	}

	private static URL getURL(String path, String fileName){
		return GenericTransformGUI.class.getResource(path +fileName);
	}


    public static boolean createDirectory(String directory){
        try {
            File file = new File(directory);
            if (!file.exists()) {
                file.mkdir();
            }
            return true;
        } catch(Exception e){
			log.log(Level.SEVERE, "Problem creating directory: "+directory);
        }
        return false;
    }

	// open file and return buffered reader
	public static BufferedReader openFileReader(String fileName){
		BufferedReader in = null;
		try{
            // try to detect charset of the file. If not detected use the default UTF-8
            String charset = DetectCharset.detectCharset(fileName);
            if(charset==null) charset = charsetDefaultIn;
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)),charset));
		} catch (Exception e){
			log.log(Level.SEVERE, "Error opening file "+fileName+newLine+"Error is: "+e.toString());
		}
		return in;
	}

	public static BufferedWriter openFileWriter(String fileName){
		return openFileWriter(fileName, false);
	}
	
	// open file and return buffered writer
	public static BufferedWriter openFileWriter(String fileName, Boolean append){
		BufferedWriter out = null;
		try {
            //Construct the BufferedWriter object
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, append), charsetDefaultOut));
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error creating output file "+fileName+newLine+"Error is: "+e.toString());
		}
		return out;
	}

	// close filereader
	public static void closeFileReader(BufferedReader in){
		try{
			in.close();
		} catch (Exception e){
			System.out.println("Error closing file"+newLine+"Error is: "+e.toString());
		}
	}

	// close filewriter
	public static void closeFileWriter(BufferedWriter out){
		try {
			if (out != null) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized <T> void writeLine(BufferedWriter out, T line){
		try{
			out.write(line.toString());
			out.newLine();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized <T> void write(BufferedWriter out, T line){
		try{
			out.write(line.toString());
			out.flush();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

	public static void setLogArea(JTextArea logArea){
		FileOperations.logArea = logArea;
	}

    private static final String charsetDefaultIn = "UTF-8";
	private static final String charsetDefaultOut = "windows-1252";
	private static final String newLine = System.lineSeparator();
	private static JTextArea logArea;
	private static final Logger log = Logging.getLogger();
}

class DetectCharset {
    static String detectCharset(String fileName) throws IOException{
        byte[] buf = new byte[4096];
        FileInputStream fis = new FileInputStream(fileName);

        UniversalDetector detector = new UniversalDetector(null);

        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }

        detector.dataEnd();

        return detector.getDetectedCharset();
    }
}
