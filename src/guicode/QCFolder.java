package guicode;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class QCFolder extends JFrame{
    /*
     * Fields
     */
    // User name of computer
    private String username = "";
    
    // Holds files paths of Main root folder locations
    private String rootFilePath = "";
    
    // For file and folder making
    private File mainDir;
    private File subDir;
    private File file;
    
    private boolean rootFolderHasbeenMade = false;
    
    /* GUI */
 //   JPanel mainPanel = new JPanel
    

    /*
     * Constructor
     */
    public QCFolder() {
	JOptionPane.showMessageDialog(null, "QuickClick Folder was not found in your computer", "***ATTENTION***", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /*
     * Stores root directory of program if computer doesn't have one
     */
    public void makeNewRootDirectory(JTextField jtxt) {
	// Var choices
	String osHardDrive = "OS Hard Drive";
	String userDoc = "User's Documents";
	String userDesktop = "User's Desktop";
		
	String[] options = new String[] {osHardDrive, userDoc, userDesktop};
	int response = JOptionPane.showOptionDialog(null, "Place QuickClick root folder in:", "QC root directory not found", 
		JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);

	// Puts main folder in computer's C Drive
	if (response == 0) {
	    String hardDriveName = JOptionPane.showInputDialog("Enter name of OS Hard Drive: ");
	    File testOSDrive = new File(hardDriveName + ":\\");
	    
	    if(!testOSDrive.exists()) {
		JOptionPane.showMessageDialog(null, "OS Drive doesn't exist.", "RESTART PROGRAM", JOptionPane.WARNING_MESSAGE);
	    }
	    
	    else 
		mainDir = new File(hardDriveName + ":\\QC - Stored Files");
		mainDir.mkdir();
	    	subDir = new File(mainDir.getAbsolutePath() + "\\0-Default Files");
	    	subDir.mkdir();

	    	try {
	    	    file = new File(subDir.getAbsolutePath() + "\\Default-File Storage.txt");
	    	    file.createNewFile();
	    	    rootFilePath = mainDir.getAbsolutePath();
	    	    rootFolderHasbeenMade = true;

	    	    JOptionPane.showMessageDialog(null, "Root folder made in the OS Drive \nTitled: 'QC - Stored Files'", mainDir.getAbsolutePath(), JOptionPane.PLAIN_MESSAGE);
	    	    jtxt.setText(rootFilePath);
	    	} catch (Exception e) {
	    	    e.printStackTrace();
	    	}
	}

	// Puts main folder in user's documents
	else if (response == 1) {
	    username = System.getProperty("user.name");

	    mainDir = new File("C:\\Users\\" + username + "\\Documents\\QC - Stored Files");
	    mainDir.mkdir();

	    subDir = new File(mainDir.getAbsolutePath() + "\\0-Default Files");
	    subDir.mkdir();

	    try {
		file = new File(subDir.getAbsolutePath() + "\\Default-File Storage.txt");
		file.createNewFile();
		rootFilePath = mainDir.getAbsolutePath();
		rootFolderHasbeenMade = true;

		JOptionPane.showMessageDialog(null, "Root folder made in Documents \nTitled: 'QC - Stored Files'", mainDir.getAbsolutePath(), JOptionPane.PLAIN_MESSAGE);
		jtxt.setText(rootFilePath);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	// Puts main folder in user's desktop
	else if (response == 2) {
	    username = System.getProperty("user.name");

	    mainDir = new File("C:\\Users\\" + username + "\\Desktop\\QC - Stored Files");
	    mainDir.mkdir();

	    subDir = new File(mainDir.getAbsolutePath() + "\\0-Default Files");
	    subDir.mkdir();

	    try {
		file = new File(subDir.getAbsolutePath() + "\\Default-File Storage.txt");
		file.createNewFile();
		rootFilePath = mainDir.getAbsolutePath();
		rootFolderHasbeenMade = true;

		JOptionPane.showMessageDialog(null, "Root folder made in Desktop \nTitled: 'QC - Stored Files'", mainDir.getAbsolutePath(), JOptionPane.PLAIN_MESSAGE);
		jtxt.setText(rootFilePath);		
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	
	else {
	    rootFolderHasbeenMade = false;
	    System.exit(0);
	}
    } // END OF makeNewRootDirectory()    
}
