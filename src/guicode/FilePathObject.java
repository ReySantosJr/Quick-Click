package guicode;

import java.awt.Component;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import java.io.File;
import java.io.IOException;

public class FilePathObject {
    /*
     * Fields
     */
    private String filePath = "";
    private String fileName = "";
    private File fileToOpen;
    private long fileSize;

    /*
     * Constructor
     */
    public FilePathObject(String fileName, String filePath) {
	this.fileName = fileName;
	this.filePath = filePath;
    }

    // Opens the file
    public void openFile(Component comp) {
	try {
	    // Sets uo runtime to open files
	    fileToOpen = new File(filePath);
	    Runtime open = Runtime.getRuntime();

	    // Checks to see if file is a file or directory, or website
	    // If neither, a google search will be done
	    if (fileToOpen.isDirectory() || fileToOpen.isFile()) {
		open.exec("explorer " + filePath);
	    } else if (filePath.contains(".com") || filePath.contains(".org") || filePath.contains(".net")
		    || filePath.contains(".edu")) {
		Desktop desktop = Desktop.getDesktop();
		URI oURL = new URI(filePath);
		desktop.browse(oURL);
	    } else {
		JOptionPane.showMessageDialog(comp, fileToOpen.getName() + " does not exist.", "INVALID FILEPATH",
			JOptionPane.WARNING_MESSAGE);
	    }
	} catch (IOException | URISyntaxException ex) {
	    ex.printStackTrace();
	}
    } 

    // Gets the size of the file
    public long getFileSize(File file) {
	fileToOpen = file;

	if (fileToOpen.isFile()) {
	    fileSize = fileToOpen.length();
	}

	else if (fileToOpen.isDirectory()) {
	    try {
		fileSize = Files.walk(new File(filePath).toPath()).map(f -> f.toFile()).filter(f -> f.isFile())
			.mapToLong(f -> f.length()).sum();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	return fileSize;
    } // END OF getFileSize(File file)

    // Gets the last date modified of the file
    public String getDateLastModified(File file) {
	fileToOpen = file;
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	return sdf.format(fileToOpen.lastModified());
    }

    // Return file path
    public String getFilePath() {
	return filePath;
    }

    // Display file name
    public String getFileName() {
	return fileName;
    }

    /*
     * toString
     */
    public String toString() {
	return fileName + "," + filePath;
    }

    /*
     * ************************************** MAIN METHOD TEST FOR DEBUGGING
     * 
     * public static void main(String[] args) { FilePathObject t = new
     * FilePathObject("Notepad", "C:\\Users\\ReySa\\Desktop\\Glasses Test");
     * System.out.println(t.showName()); System.out.println(t.showFilePath());
     * System.out.println(t.getFileSize(new File(t.showFilePath())));
     * System.out.println(t.getDateLastModified(new File(t.showFilePath()))); }
     */
}
