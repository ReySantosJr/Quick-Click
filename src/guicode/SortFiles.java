package guicode;

import java.io.File;
import java.util.Comparator;

public class SortFiles {
    /*
     * Fields
     */
    // For files
    Comparator<FilePathObject> sortAlpaAscendFile;
    Comparator<FilePathObject> sortAlpaDescendFile;
    Comparator<FilePathObject> sortFileSizeFile;
    Comparator<FilePathObject> sortDateModifiedFile;
    
    /*
     * Constructor
     */
    public SortFiles() {
    }
    
    /* 
     * File Sorting
     */
    // File, Alphabetical ascending Sort
    public Comparator<FilePathObject> sortAlphaAscendingFile() {
	sortAlpaAscendFile = new Comparator<>() {
	    @Override
	    public int compare(FilePathObject fpo1, FilePathObject fpo2) {
		return fpo1.getFileName().compareTo(fpo2.getFileName());
	    }
	};
	
	return sortAlpaAscendFile;
    }
    
    // File, Alphabetical descending Sort
    public Comparator<FilePathObject> sortAlphaDescendingFile() {
	sortAlpaDescendFile = new Comparator<>() {
	    @Override
	    public int compare(FilePathObject fpo1, FilePathObject fpo2) {
		return fpo2.getFileName().compareTo(fpo1.getFileName());
	    }
	};
	
	return sortAlpaDescendFile;
    }
    
    // File, By file size
    public Comparator<FilePathObject> sortFileSizeFile() {
	sortFileSizeFile = new Comparator<>() {
	    @Override
	    public int compare(FilePathObject fpo1, FilePathObject fpo2) {
		return Long.compare(fpo1.getFileSize(new File(fpo1.getFilePath())), 
			fpo2.getFileSize(new File(fpo2.getFilePath())));
	    }
	};
	
	return sortFileSizeFile;
    }
    
    // File, By date modified
    public Comparator<FilePathObject> sortByDateModifiedFile() {
	sortDateModifiedFile = new Comparator<>() {
	    @Override
	    public int compare(FilePathObject fpo1, FilePathObject fpo2) {
		return fpo2.getDateLastModified(new File(fpo2.getFilePath())).
			compareTo(fpo1.getDateLastModified(new File(fpo1.getFilePath())));
	    }
	};
	
	return sortDateModifiedFile;
    }
    
}
