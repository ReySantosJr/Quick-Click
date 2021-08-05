package guicode;

import java.io.File;
import java.util.ArrayList;

public class FileFolder {
    /* 
     * Fields 
     * */
    private String folderName;
    private String folderPath;
    
    private File folderFile;
    private File[] filePathStorage;
    
    private ArrayList<FilePathStorage> filePathStorageList = new ArrayList<>();
    
    /* 
     * Constructor 
     * */
    public FileFolder(String folderName, String folderPath) {
	this.folderName = folderName;
	this.folderPath = folderPath;
	
	storeFileStorageDocs();
    }
    
    public FileFolder() {}
    
    // Store File Storage objects of textdocs to filePathStorageList
    private void storeFileStorageDocs() {
	folderFile = new File(folderPath);
	filePathStorage = folderFile.listFiles();
	
        for (int i = 0; i < filePathStorage.length; i++) {
            filePathStorageList.add(new FilePathStorage(filePathStorage[i].getName(), filePathStorage[i].getAbsolutePath()));                 
        }   
    }
    
    // Show folder name
    public String getFolderName() {
	return folderName;
    }
    
    // Show folder path
    public String getFolderFilePath() {
	return folderPath;
    }
    
    // toString
    public String toString() {
	return folderName + ", " + folderPath;
    }
  
    
    /* *************************************************
     /*  MAIN METHOD TEST FOR DEBUGGING 
     
    public static void main(String[] args) {
	FileFolder ff = new FileFolder("File Paths", "C:\\Users\\ReySa\\Desktop\\FFF - Stored Files\\File Paths");

//	System.out.println(ff.showFolderName());
//	System.out.println(ff.showFolderFilePath());
//	System.out.println(ff);
    }
     ***************************************************/

}
