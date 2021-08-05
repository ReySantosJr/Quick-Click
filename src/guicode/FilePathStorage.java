package guicode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FilePathStorage {
    /*
     * Fields
     */
    private String fileStorageTextFile;
    private String fileStorageTextFilePath;
    private String fileToScan;

    private ArrayList<FilePathObject> filePathObjectList = new ArrayList<>();
    private FilePathObject fpo;

    /*
     * Constructor
     */
    public FilePathStorage(String fileStorageTextFile, String fileStorageTextFilePath) {
	this.fileStorageTextFile = fileStorageTextFile;
	this.fileStorageTextFilePath = fileStorageTextFilePath;

	// Stores the files 
	storeFileTextDoc();
    }

    // Stores the file path to the text document
    public void storeFileTextDoc() {
	// Needed variables
	fileToScan = fileStorageTextFilePath;
	String lines = "";
	String fn = "", fp = "";

	try {
	    File f = new File(fileToScan);
	    Scanner scan = new Scanner(f);
	    FileReader fr = new FileReader(f);
	    BufferedReader br = new BufferedReader(fr);

	    while (scan.hasNext()) {
		lines = scan.nextLine();

		List<String> textDocList = new ArrayList<String>();
		Collections.addAll(textDocList, lines);

		for (int i = 0; i < textDocList.size(); i++) {
		    Scanner scan2 = new Scanner(textDocList.get(i));
		    scan2.useDelimiter("[,\n]");

		    fn = scan2.next();
		    fp = scan2.next();
		    fpo = new FilePathObject(fn, fp);
		    filePathObjectList.add(fpo);

		    scan2.close();
		}
	    }

	    scan.close();
	    br.close();

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    // Displays the hash map
    public void showfilePathList() {
	for (int i = 0; i < filePathObjectList.size(); i++) {
	    System.out.println("(" + i + ") " + filePathObjectList.get(i).getFileName());
	}
    }

    // Return list size
    public int getListSize() {
	return filePathObjectList.size();
    }

    // Return all names from storage doc
    public String getFilePathObjectNames() {
	int i = 0;

	for (i = 0; i < filePathObjectList.size(); i++) {
	    filePathObjectList.get(i).getFileName();
	    i++;
	}

	return filePathObjectList.get(i++).getFileName();
    }

    // Show folder name
    public String getFilePathStorageName() {
	return fileStorageTextFile;
    }

    // Show folder path
    public String getFilePathStorageFilePath() {
	return fileStorageTextFilePath;
    }

    // toString
    public String toString() {
	return fileStorageTextFile + ", " + fileStorageTextFilePath;
    }

    // show file object file name
    public String showFileObjectName(int k) {
	String fileName = "";
	fileName = filePathObjectList.get(k).getFileName();

	return fileName;
    }

    // show file object file path
    public String showFileObjectPath(int k) {
	String filePath = "";
	filePath = filePathObjectList.get(k).getFilePath();

	return filePath;
    }

    public String showFilePathObjectToString(int k) {
	return filePathObjectList.get(k).toString();
    }

    public void removeFilePathObject(int j) {
	filePathObjectList.remove(filePathObjectList.get(j));
    }

    // Getter for ArrayList<FilePathObject> filePathObjectList 
    public ArrayList<FilePathObject> getFilePathObjectList() {
	return filePathObjectList;
    }

    /* **************************************************
     *  MAIN METHOD TEST FOR DEBUGGING
     *
    public static void main(String[] args) {
	    FilePathStorage fps = new FilePathStorage("File Paths 1-File Storage.txt",
			"C:\\Users\\ReySa\\Desktop\\FFF - Stored Files\\File Paths\\File Paths 1-File Storage.txt");
		
	    System.out.println("***File Text Docs***");
	    System.out.println(fps.showFileTextDocs());
	    System.out.println();
	    
	    System.out.println("***File Text Docs File Paths***");
	    System.out.println(fps.showFileTextDocFilePath());
	    fps.showfilePathList();
	    System.out.println();

    }    
    ***************************************************/

}
