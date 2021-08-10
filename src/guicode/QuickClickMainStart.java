package guicode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class QuickClickMainStart extends JFrame implements ActionListener {
    /*
     * Fields
     */
    /* GUI Components */
    private JPanel northPanel = new JPanel();
    private JPanel centerPanel = new JPanel();
    private JPanel southPanel = new JPanel();

    // Boxes for layout
    private Box rootFolderLocationBox = Box.createHorizontalBox();

    // Label
    private JLabel rootFolderTitleLbl = new JLabel("Root Folder in: ");
    private JTextField rootFolderLocationTxtField = new JTextField(25);

    // Buttons
    private JButton defaultBtn = new JButton("| *** Open Default Files *** |");
    private JButton setUpDefaultBtn = new JButton("| Set Up Default Files |");
    private JButton searchFileBtn = new JButton("| Search Other Files |");
    private JButton infoBtn = new JButton("info & How to?");
    
    // Checkbox
    private JCheckBox qcCheckBox = new JCheckBox("Quick Click");
    private String quickClickOn = "";

    /* Data types*/
    // root folder existence
    private boolean rootFolderExists = false;

    // User name of computer
    private String username = "";
    
    // Font
    private Font defaultFont = new Font("Tahoma Plain", Font.BOLD, 14);	

    // Used for searching root file path
    private File startingFilePath;
    private String[] filePathsToSearch;
    private File reCheckRootFilepath; 

    // Holds files paths of Main root folder locations
    private String rootFilePath = "";
    private String defaultStorageFilePath = "";

    // Collections to hold subfolder names that hold 
    // the text that store the file paths
    private ArrayList<FileFolder> fileFolderList;
    private ArrayList<FilePathObject> defaultFileList;

    // To create file path storage docs
    private FilePathStorage file_path_storage;

    /*
     * Constructor
     */
    QuickClickMainStart() {
	// Checks for root folder 
	checksforRootFolder();
	
	// Checks if quick click is on
	quickClickOnOff();	
	
	// Open all default files
	doQuickClick();	
	
	// Creates window
	makeMainMenu();	
	
	// Enables these buttons first until the user clicks the other
	setUpDefaultBtn.setEnabled(true);
	searchFileBtn.setEnabled(true);

	/* Actions Listeners */
	defaultBtn.addActionListener(this);
	setUpDefaultBtn.addActionListener(this);
	searchFileBtn.addActionListener(this);
	infoBtn.addActionListener(this);	
	
	    checksforRootFolder();
	    
	// Enables search file window again
	addWindowListener(new java.awt.event.WindowAdapter() {
	    @Override
	    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		if(reCheckRootFilepath.exists()) {
		    System.exit(0);
		}
		else {
		    JOptionPane.showMessageDialog(null, "Check your:\n       - OS drive\n       - Documents folder\n"
		    	+ "       - Desktop\n       - or Recycling Bin.", "ATTENTION - ROOT FOLDER IS MISSING", JOptionPane.WARNING_MESSAGE);
		}
	    }
	}); // Enables search file window again
    }

    /*
     * Main Menu
     */
    void makeMainMenu() {
	// Window Settings
	setTitle("Quick Click Menu");
	setSize(300, 232);
	setResizable(false);
	setLocation(50, 50);
	setDefaultCloseOperation(EXIT_ON_CLOSE);	
	
	/* Box & Layout setup */
	// Root folder setup
	rootFolderLocationBox.add(rootFolderTitleLbl);
	rootFolderLocationTxtField.setEditable(false);
	rootFolderLocationBox.add(rootFolderLocationTxtField);
	northPanel.setBorder(BorderFactory.createEtchedBorder());
	northPanel.add(rootFolderLocationBox);

	// Center panel setup
	centerPanel.setLayout(new BorderLayout(0, 10));
	centerPanel.add(defaultBtn, BorderLayout.NORTH);
	centerPanel.add(setUpDefaultBtn, BorderLayout.CENTER);
	centerPanel.add(searchFileBtn, BorderLayout.SOUTH);
	centerPanel.setBorder(BorderFactory.createTitledBorder(null, "Main Menu", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, defaultFont));


	// info and quick click button panel
	infoBtn.setPreferredSize(new Dimension(120, 25));
	southPanel.setBorder(BorderFactory.createEtchedBorder());
	southPanel.add(infoBtn);
	qcCheckBox.setSelected(false);
	southPanel.add(qcCheckBox);
	
	// Add panels to main content pane 
	getContentPane().add("North", northPanel);
	getContentPane().add("Center", centerPanel);
	getContentPane().add("South", southPanel);
	
	// Checkbox selection
	for (int i = 0; i < file_path_storage.getFilePathObjectList().size(); i++) {
	    if(file_path_storage.getFilePathObjectList().get(i).getFilePath().equals("null")) {
		quickClickOn = file_path_storage.getFilePathObjectList().get(i).getFileName();
		
		if(quickClickOn.equals("true")) {
		    qcCheckBox.setSelected(true);
		}
	    }	
	}
	
	// Visibility on
	setVisible(true);
    } //END OF void makeMainMenu()

    /*
     * Searches for root directory in C Drive, User's Documents, and Desktop
     * in order to start the program
     */
    public void checksforRootFolder() {
	// Search in C Drive
	startingFilePath = new File("C:\\");
	filePathsToSearch = startingFilePath.list();

	for (String filePathsToSearchOfCDrive : filePathsToSearch) {
	    if (filePathsToSearchOfCDrive.equals("QC - Stored Files")) {

		// Root file path is stored
		rootFilePath = startingFilePath.getAbsolutePath() + "\\" + filePathsToSearchOfCDrive;
		rootFolderLocationTxtField.setText(rootFilePath);
		reCheckRootFilepath = new File(rootFilePath);

		rootFolderExists = true;
		makeListsForProgram();
	    }
	}

	// Gets current user name and sets it into a file path String var 'username'
	username = System.getProperty("user.name");
	startingFilePath = new File("C:\\Users\\" + username);

	// Search in user's Desktop
	File userNameDesktop = new File(startingFilePath.getAbsolutePath().concat("\\Desktop"));
	filePathsToSearch = userNameDesktop.list();

	for (String filePathsToSearchOfDesktop : filePathsToSearch) {
	    if (filePathsToSearchOfDesktop.equals("QC - Stored Files")) {

		// Root file path is stored
		rootFilePath = userNameDesktop + "\\" + filePathsToSearchOfDesktop;
		rootFolderLocationTxtField.setText(rootFilePath);
		reCheckRootFilepath = new File(rootFilePath);

		rootFolderExists = true;
		makeListsForProgram();
	    }
	}

	// Search in user's Documents
	File userNameDocuments = new File(startingFilePath.getAbsolutePath().concat("\\Documents"));
	filePathsToSearch = userNameDocuments.list();

	for (String filePathsToSearchOfDocuments : filePathsToSearch) {
	    if (filePathsToSearchOfDocuments.equals("QC - Stored Files")) {

		// Root file path is stored
		rootFilePath = userNameDocuments.getAbsolutePath() + "\\" + filePathsToSearchOfDocuments;
		rootFolderLocationTxtField.setText(rootFilePath);
		reCheckRootFilepath = new File(rootFilePath);

		rootFolderExists = true;
		makeListsForProgram();
	    }
	}
	
	// If QC folder doesn't exist, one is made
	if(!rootFolderExists) {
	    QCFolder qcf = new QCFolder();
	    qcf.makeNewRootDirectory(rootFolderLocationTxtField);
	    checksforRootFolder();
	    
	}
	
    } // END OF checksforRootFolder()
    
    /*
     * Initiate QuickClick Feature
     */
    private void quickClickOnOff() {
	if(quickClickOn.equals("false")) {
	    qcCheckBox.setSelected(false);
	}
	else if(quickClickOn.equals("true")) {
	    qcCheckBox.setSelected(true);
	}
	
	qcCheckBox.addItemListener(c -> {
	    if (c.getStateChange() == ItemEvent.DESELECTED) {
		quickClickOn = "false";

		try {
		    FileWriter writeNewFilePath = new FileWriter(
			    rootFilePath + "\\0-Default Files\\Default-File Storage.txt");
		    BufferedWriter buffWriteNewFilePath = new BufferedWriter(writeNewFilePath);

		    for (int m = 0; m < (defaultFileList.size()); m++) {
			buffWriteNewFilePath.write(
				defaultFileList.get(m).getFileName() + "," + defaultFileList.get(m).getFilePath());
			buffWriteNewFilePath.newLine();
		    }

		    buffWriteNewFilePath.write(quickClickOn + ",null");

		    buffWriteNewFilePath.close();

		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    
	    else if (c.getStateChange() == ItemEvent.SELECTED) {
		quickClickOn = "true";
		
		try {
		    FileWriter writeNewFilePath = new FileWriter(
			    rootFilePath + "\\0-Default Files\\Default-File Storage.txt");
		    BufferedWriter buffWriteNewFilePath = new BufferedWriter(writeNewFilePath);

		    for (int m = 0; m < (defaultFileList.size()); m++) {
			buffWriteNewFilePath.write(
				defaultFileList.get(m).getFileName() + "," + defaultFileList.get(m).getFilePath());
			buffWriteNewFilePath.newLine();
		    }

		    buffWriteNewFilePath.write(quickClickOn + ",null");

		    buffWriteNewFilePath.close();

		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /*
     * Do quick click feature
     */
    private void doQuickClick() {
	if(quickClickOn.equals("true")) {
	    openDefaultFiles();
	    
	    int select = JOptionPane.showConfirmDialog(this, "Enter main menu?", 
		    "Quick Cick", JOptionPane.YES_NO_OPTION);
	   
	    switch(select) {
	    	case 0: 
	    	    break;
	    	case 1: 	    	    
	    	    System.exit(0);
	    	    break;
	    }	    
	}
	
	else if(quickClickOn.equals("false")) {
	    // DO NOTHING
	}
    }
    
    /*
     * Gets main root folder and stores file path and 
     * stores file path lists in a String array
     */
    public void makeListsForProgram() {
	// Vars for holding folder names and variables, and count	
	File fileOfMainRootPath = new File(rootFilePath);
	String[] folderNamePaths = fileOfMainRootPath.list();
	
	// Store folder name and folder file paths
	fileFolderList = new ArrayList<FileFolder>();
	for (String folderNames : folderNamePaths) {
	    fileFolderList.add(new FileFolder(folderNames, rootFilePath + "\\" + folderNames));
	}

	// Vars for holding default file paths
	File defaultFolder = new File(fileFolderList.get(0).getFolderFilePath());
	File[] defaultFolderArray = defaultFolder.listFiles();
	defaultStorageFilePath = defaultFolderArray[0].getAbsolutePath();
	defaultFileList = new ArrayList<FilePathObject>();

	// Sets up default folder, and default storage file
	file_path_storage = new FilePathStorage(defaultFolderArray[0].getName(), defaultStorageFilePath);
	for (int i = 0; i < file_path_storage.getFilePathObjectList().size(); i++) {
	    if(file_path_storage.getFilePathObjectList().get(i).getFilePath().equals("null")) {
		quickClickOn = file_path_storage.getFilePathObjectList().get(i).getFileName();
	    }
	    else
		defaultFileList.add(file_path_storage.getFilePathObjectList().get(i));
	}	
    } // END OF makeListsForProgram()
    
    /*
     * Opens all default files.
     * Is used in method, actionperformed(ActionEvent e)
     */
    private void openDefaultFiles() {
	// Vars needed
	ArrayList<String> filesDontExistList = new ArrayList<>();
	String nonFiles = "";

	// If default file list is empty, displays message
	if (defaultFileList.isEmpty()) {
	    JOptionPane.showMessageDialog(defaultBtn, "Default list is empty.", "ATTENTION",
		    JOptionPane.INFORMATION_MESSAGE);

	} else {
	    // Opens all defualt files
	    for (int i = 0; i < defaultFileList.size(); i++) {
		try {
		    File fileToOpen = new File(defaultFileList.get(i).getFilePath());
		    Runtime open = Runtime.getRuntime();

		    if (fileToOpen.isDirectory() || fileToOpen.isFile()) {
			open.exec("explorer " + defaultFileList.get(i));			
		    } else if (defaultFileList.get(i).getFilePath().contains(".com")) {
			Desktop desktop = Desktop.getDesktop();
			URI oURL = new URI(defaultFileList.get(i).getFilePath());
			desktop.browse(oURL);
		    } else {
			filesDontExistList.add(defaultFileList.get(i).getFileName());
		    }
		} catch (IOException | URISyntaxException ex) {
		    ex.printStackTrace();
		}
	    }

	    // 4 second pause before alerting user of invalid files
	    try {
		Thread.sleep(4000);
	    } catch (InterruptedException e1) {
		e1.printStackTrace();
	    }

	    // Presents invalid files
	    for (int i = 0; i < filesDontExistList.size(); i++) {
		nonFiles += (i + 1 + " - " + filesDontExistList.get(i) + "\n");
	    }
	    
	    if (nonFiles.equals("")) {
		// Warning will not appear
	    } 
	    
	    else if(nonFiles.contains("true") || nonFiles.contains("false")) {
		// DO NOTHING
	    }
	    
	    else
		JOptionPane.showMessageDialog(this, nonFiles, "INVALID FILE(s)", JOptionPane.WARNING_MESSAGE);
	} // END OF if statement
    }
    
    /*
     * ActionListeners
     */
    @Override
    public void actionPerformed(ActionEvent e) {
	// When user clicks the default button
	if (e.getSource() == defaultBtn) {
	    openDefaultFiles();
	}

	// When user clicks the set up default button
	else if (e.getSource() == setUpDefaultBtn) {
	    DefaultFileMenu dfm = new DefaultFileMenu(rootFilePath, defaultFileList, quickClickOn);
	    setUpDefaultBtn.setEnabled(false);
	    searchFileBtn.setEnabled(false);
	    try {
		dfm.presentDefaultFiles();
	    } catch (FileNotFoundException e1) {
		e1.printStackTrace();
	    }

	    // Enables button use again
	    dfm.addWindowListener(new java.awt.event.WindowAdapter() {
		@Override
		public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    setUpDefaultBtn.setEnabled(true);
		    searchFileBtn.setEnabled(true);
		}
	    });
	}

	// When user clicks the search files button
	else if (e.getSource() == searchFileBtn) {

	    // Calls SearchFileMenu and disables main window
	    SearchFilesMenu sfm = new SearchFilesMenu(rootFilePath, defaultFileList, fileFolderList);
	    setUpDefaultBtn.setEnabled(false);
	    searchFileBtn.setEnabled(false); 
	    sfm.presentFiles();

	    // Enables button use again
	    sfm.addWindowListener(new java.awt.event.WindowAdapter() {
		@Override
		public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    setUpDefaultBtn.setEnabled(true);
		    searchFileBtn.setEnabled(true);
		}
	    });
	}

	else if (e.getSource() == infoBtn) {	    
	    // Frame components
	    JFrame infoFrame = new JFrame("Info & How to");
	    
	    // JPanels
	    JPanel QCFolderInfoPanel = new JPanel();
	    JPanel panelHowTo = new JPanel();
	    	    
	    // JTextPane
	    JTextPane JTextQCFolder = new JTextPane();    
	    JTextPane JTextPaneHowTo = new JTextPane();
	    
	    // Font setup
	    Font font = new Font("Times New Roman", Font.PLAIN, 16);
	    
	    // Plain text attribute
	    SimpleAttributeSet attributeDefault = new SimpleAttributeSet();
	    
	    // Set Bold attribute
	    SimpleAttributeSet attributeSetBold = new SimpleAttributeSet();
	    StyleConstants.setFontFamily(attributeSetBold, "Helvetica");
            StyleConstants.setFontSize(attributeSetBold, 16);
            StyleConstants.setForeground(attributeSetBold, Color.BLACK);
            StyleConstants.setBold(attributeSetBold, true);
            StyleConstants.setItalic(attributeSetBold, true);
	    
	    Box vertBox = Box.createVerticalBox();
	    
	    // Window setup
	    infoFrame.setVisible(true);
	    infoFrame.setSize(475, 530);
	    infoFrame.setLocation(450, 50);
	    infoFrame.setResizable(false);
	    infoFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    	    	    
	    /* Explains program functions */
	    JTextPaneHowTo.setEditable(false);
	    JTextPaneHowTo.setPreferredSize(new Dimension(440, 220));
	    JTextPaneHowTo.setForeground(Color.BLACK);
	    JTextPaneHowTo.setFont(font);
	    Document docHowTo= JTextPaneHowTo.getStyledDocument();

	    try {
		docHowTo.insertString(docHowTo.getLength(), 
			"The ", attributeDefault);
		
		// Set Bold attribute to "Set Up Default Files"
		docHowTo.insertString(docHowTo.getLength(), 
			"Set Up Default Files", attributeSetBold);
		
		// continues in normal font
		docHowTo.insertString(docHowTo.getLength(), " section let's you store daily used files, programs, or websites for easy accessiblity. " 
			+ "You have the option to add, remove, or open them. \n\nThe ", attributeDefault);
		
		// Set Bold attribute to "Search Other Files"
		docHowTo.insertString(docHowTo.getLength(), 
			"Search Other Files", attributeSetBold);
		
		// continues in normal font
		docHowTo.insertString(docHowTo.getLength(), " section has the same function but it's " +
		"for files/programs that aren't used daily. Other purposes could be for fun, leasure time, personal, or for rare occasions. \n\nThe ", attributeDefault);
		
		// Set Bold attribute to "Search Other Files"
		docHowTo.insertString(docHowTo.getLength(), "Quick Click", attributeSetBold);
		
		// continues in normal font
		docHowTo.insertString(docHowTo.getLength(), " button opens your default files "
			+ "automatically when the QuickClick program is opened.", attributeDefault);
		
	    } catch (BadLocationException e2) {
		e2.printStackTrace();
	    }
	    
	    panelHowTo.setBorder(BorderFactory.createTitledBorder("How to Use & About the Program"));
	    
	    /* Explains QC folder */
	    // Anatomy of program text
	    JTextQCFolder.setEditable(false);
	    JTextQCFolder.setPreferredSize(new Dimension(440, 200));
	    JTextQCFolder.setForeground(Color.BLACK);
	    JTextQCFolder.setFont(font);
	    
	    Document docQCFolderInfo = JTextQCFolder.getStyledDocument();
	    
	    try {
		docQCFolderInfo.insertString(docQCFolderInfo.getLength(), 
			"This program uses a ", attributeDefault);
		
		// set Bold attribute to "Root Folder"
		docQCFolderInfo.insertString(docQCFolderInfo.getLength(), 
			"Root Folder" , attributeSetBold);
		
		// continues in normal font
		docQCFolderInfo.insertString(docQCFolderInfo.getLength(), " which holds the ", attributeDefault); 
		
		// set Bold attribute to "File Folders"
		docQCFolderInfo.insertString(docQCFolderInfo.getLength(), 
			"Default File Folder" , attributeSetBold);
		
		// continues in normal font
		docQCFolderInfo.insertString(docQCFolderInfo.getLength(), ". This is where your default files are held.\n\nThe user can also add additional folders to the Root Folder. These folders hold ", attributeDefault);
		
		// set Bold attribute to "File Path Storages"
		docQCFolderInfo.insertString(docQCFolderInfo.getLength(), 
			"File Path Storages" , attributeSetBold);
			
		// continues in normal font
		docQCFolderInfo.insertString(docQCFolderInfo.getLength(), ". This is where non default ", attributeDefault);
		
		// set Bold attribute to "File Path Objects"
		docQCFolderInfo.insertString(docQCFolderInfo.getLength(), "Files", attributeSetBold);
		
		// continues in normal font
		docQCFolderInfo.insertString(docQCFolderInfo.getLength()," are held.\n\nSelecting a File Path Storage will display the files, programs, websites, etc that you've stored.", attributeDefault);
	    
	    } catch (BadLocationException e1) {
		e1.printStackTrace();
	    }
	    
	    QCFolderInfoPanel.setBorder(BorderFactory.createTitledBorder("The QC Folder"));
	    QCFolderInfoPanel.add(JTextQCFolder);
	    panelHowTo.add(JTextPaneHowTo);
	    
	    // Add info components to frame
	    vertBox.add(panelHowTo);
	    vertBox.add(QCFolderInfoPanel);
	    
	    // Added to infoFrame
	    infoFrame.add(vertBox);	    
	}
    } // END OF actionPerformed(ActionEvent e)

    /*
     * Main Method
     */
    public static void main(String[] args) {
	QuickClickMainStart qc = new QuickClickMainStart();
    }

}
