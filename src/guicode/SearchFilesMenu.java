package guicode;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class SearchFilesMenu extends DefaultFileMenu {
    /*
     * Fields
     */
    // Class needed
    private FilePathStorage f_p_s;

    // Files, File Arrays
    private File folderFile;
    private File[] filePathStorageDocsArray;

    private ArrayList<FileFolder> fileFolderList = new ArrayList<>();
    private ArrayList<FilePathStorage> filePathStorageList = new ArrayList<>();
    private ArrayList<FilePathObject> filePathObjectList = new ArrayList<>();
    private ArrayList<FilePathObject> filePathObjectListTwo = new ArrayList<>();

    private Box nodeTypeBox = Box.createHorizontalBox();
    private JTextField nodeTypeJTextFld = new JTextField(10);
    private JLabel nodeTypeLbl = new JLabel("Type: ");
    private JButton resetTreeBtn = new JButton("Reset Tree");

    // Tree Nodes
    private DefaultMutableTreeNode folderNode;
    private DefaultMutableTreeNode fileStorageNode;
    private DefaultMutableTreeNode fileObjectNode;

    /*
     * Constructor
     */
    SearchFilesMenu(String rootFilePath, ArrayList<FilePathObject> defaultFileList,
	    ArrayList<FileFolder> fileFolderList) {
	// Window Settings
	super(rootFilePath, defaultFileList, null);
	this.fileFolderList = fileFolderList;
	setTitle("Search File Menu");
	setSize(400, 440);

	// Removes default folder
	if (fileFolderList.get(0).getFolderName().equals("0-Default Files")) {
	    fileFolderList.remove(fileFolderList.get(0));
	}

	// Removes file size components
	sortBtnGroup.remove(sortByFileSizeBtn);
	topPanel.remove(sortByFileSizeBtn);

	// Adds additionally
	topPanel.add(resetTreeBtn);

	nodeTypeBox.setBorder(BorderFactory.createEtchedBorder());
	nodeTypeJTextFld.setEditable(false);
	nodeTypeBox.add(nodeTypeLbl);
	nodeTypeBox.add(nodeTypeJTextFld);

	mainPanel.remove(fileSizeBox);

	gc.anchor = GridBagConstraints.LINE_START;
	gc.weightx = 0.5;
	gc.weighty = 0.5;
	gc.gridx = 0;
	gc.gridy = 2;
	mainPanel.add(nodeTypeBox, gc);

	gc.anchor = GridBagConstraints.LINE_START;
	gc.weightx = 0.5;
	gc.weighty = 0.5;
	gc.gridx = 0;
	gc.gridy = 3;
	mainPanel.add(filePathBox, gc);

	gc.anchor = GridBagConstraints.LINE_START;
	gc.weightx = 0.5;
	gc.weighty = 0.5;
	gc.gridx = 0;
	gc.gridy = 4;
	mainPanel.add(dateModifiedBox, gc);

	deleteBtnPanel.setBorder(BorderFactory.createEtchedBorder());
	deleteBtnPanel.add(deleteBtn);
	gc.anchor = GridBagConstraints.LINE_START;
	gc.weightx = 0.5;
	gc.weighty = 0.5;
	gc.gridx = 0;
	gc.gridy = 5;
	mainPanel.add(deleteBtnPanel, gc);

	openBtnPanel.setBorder(BorderFactory.createLoweredBevelBorder());
	openBtnPanel.add(openBtn);
	gc.anchor = GridBagConstraints.CENTER;
	gc.weightx = 0.5;
	gc.weighty = 0.5;
	gc.gridx = 0;
	gc.gridy = 5;
	mainPanel.add(openBtnPanel, gc);

	addBtnPanel.setBorder(BorderFactory.createEtchedBorder());
	addBtnPanel.add(addBtn);
	gc.anchor = GridBagConstraints.LINE_END;
	gc.weightx = 0.5;
	gc.weighty = 0.5;
	gc.gridx = 0;
	gc.gridy = 5;
	mainPanel.add(addBtnPanel, gc);
	
	// Makes the JTree
	makeJTree() ;
	
    } // END OF CONSTRUCTOR

    // Create JTree
    public void makeJTree() {
	for (int i = 0; i < fileFolderList.size(); i++) {
	    // Add storage files to root node
	    folderNode = new DefaultMutableTreeNode(fileFolderList.get(i).getFolderName());
	    defaultMutableTreeNode.add(folderNode);

	    // Puts folder storage docs in the array: filePathStorageDocsArray
	    folderFile = new File(fileFolderList.get(i).getFolderFilePath());
	    filePathStorageDocsArray = folderFile.listFiles();

	    // Add storage docs to storage files
	    for (int j = 0; j < filePathStorageDocsArray.length; j++) {
		// Makes new FilePathStorage objects and adds them to the ArrayList:
		// filePathStorageList
		f_p_s = new FilePathStorage(filePathStorageDocsArray[j].getName(),
			filePathStorageDocsArray[j].getAbsolutePath());

		// Adds FilePath Storage objects to filePathStorageList
		filePathStorageList.add(f_p_s);

		// Add new file path storage node to folder nodes
		fileStorageNode = new DefaultMutableTreeNode(filePathStorageDocsArray[j].getName());
		folderNode.add(fileStorageNode);

		filePathObjectList = new ArrayList<>();
		filePathObjectList = f_p_s.getFilePathObjectList();

		// Make nodes from files of file path storage
		for (int k = 0; k < filePathObjectList.size(); k++) {
		    fileObjectNode = new DefaultMutableTreeNode(filePathObjectList.get(k).getFileName());
		    fileStorageNode.add(fileObjectNode);

		    // filePathObjectList is added to filePathObjectListTwo to display
		    // nodes properly when user selects them
		    filePathObjectListTwo.addAll(filePathObjectList);

		} // END OF for loop 'k'

	    } // END OF for loop 'j'

	} // END OF for loop 'i'
    }

    public void createFileObjectListTwo() {
	for (int i = 0; i < fileFolderList.size(); i++) {
	    // Puts folder storage docs in the array: filePathStorageDocsArray
	    folderFile = new File(fileFolderList.get(i).getFolderFilePath());
	    filePathStorageDocsArray = folderFile.listFiles();

	    // Add storage docs to storage files
	    for (int j = 0; j < filePathStorageDocsArray.length; j++) {
		// Makes new FilePathStorage objects and adds them to the ArrayList:
		// filePathStorageList
		f_p_s = new FilePathStorage(filePathStorageDocsArray[j].getName(),
			filePathStorageDocsArray[j].getAbsolutePath());

		// Adds FilePath Storage objects to filePathStorageList
		filePathStorageList.add(f_p_s);
		filePathObjectList = new ArrayList<>();
		filePathObjectList = f_p_s.getFilePathObjectList();

		for (int k = 0; k < filePathObjectList.size(); k++) {

		    if (!filePathObjectListTwo.contains(filePathObjectList.get(k))) {
			filePathObjectListTwo.add(filePathObjectList.get(k));
		    }

		} // END OF for loop 'k'

	    } // END OF for loop 'j'

	} // END OF for loop 'i'
    }

    /*
     * Creates JTree of the files and has other JTree interactions
     */
    public void presentFiles() {
	// Displays file path of files and adds and deletes files
	treeRootNode.getSelectionModel().addTreeSelectionListener(x -> {
	    // Initilize selectedNode
	    selectedNode = (DefaultMutableTreeNode) treeRootNode.getLastSelectedPathComponent();

	    // Displays if node is the root
	    if (selectedNode.isRoot()) {
		nodeTypeJTextFld.setText("Root Folder");
	    }

	    // Displays if node is a File Folder
	    for (FileFolder ff : fileFolderList) {
		if (selectedNode.getUserObject().equals(ff.getFolderName())) {
		    filePath = ff.getFolderFilePath();
		    nodeTypeJTextFld.setText("File Folder");
		}
	    }

	    // Displays if node is a File Path Storage
	    for (FilePathStorage fps : filePathStorageList) {
		if (selectedNode.getUserObject().equals(fps.getFilePathStorageName())) {
		    filePath = fps.getFilePathStorageFilePath();
		    nodeTypeJTextFld.setText("File Path Storage");
		}
	    }

	    // Display file path of files in filePathJText
	    for (FilePathObject fpo : filePathObjectListTwo) {
		if (selectedNode.getUserObject().toString().contains(fpo.getFileName())) {
		    filePath = fpo.getFilePath();
		    nodeTypeJTextFld.setText("File");
		    filePathJTextFld.setText(filePath);

		    if (selectedNode.getUserObject().toString().contains(fpo.getFileName())) {
			filePath = fpo.getFilePath();
			filePathJTextFld.setText(filePath);

			DecimalFormat df = new DecimalFormat(".##");
			df.setRoundingMode(RoundingMode.CEILING);
			File file = new File(filePath);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss");
			dateModifiedJTextFld.setText(sdf.format(file.lastModified()));
		    }
		}
	    }
	});

	/*
	 * Add button listeners, Add file path
	 */
	addBtn.addActionListener(ad -> {
	    selectedNode = (DefaultMutableTreeNode) treeRootNode.getLastSelectedPathComponent();

	    // Vars for add button JFrame
	    JPanel mainPanel = new JPanel();
	    JPanel filePathPanel = new JPanel();
	    JPanel fileNamePanel = new JPanel();
	    Box filePathBox = Box.createVerticalBox();
	    Box fileNameBox = Box.createHorizontalBox();

	    JLabel addFilePathLbl = new JLabel("File Path: ");
	    JTextField addFilePathTextField = new JTextField(25);
	    JLabel addFileName = new JLabel("Name: ");
	    JTextField addFileNameTextField = new JTextField(26);
	    JButton addFileBtn = new JButton("Add File");
	    JButton JFileChooserBtn = new JButton("Browse Computer");

	    // If user did not click root folder first
	    if (treeRootNode.isSelectionEmpty()) {
		JOptionPane.showMessageDialog(this, "Folder was not selected.", "ATTENTON",
			JOptionPane.WARNING_MESSAGE);
	    }

	    // Root Folder JFrame, and add button listener
	    if (selectedNode.isRoot()) {
		String filePathText = rootFilePath + "\\" + addFileNameTextField.getText();

		JFrame fileFolderJFrame = new JFrame("Add New File Folder:");
		// disables search file window again
		setEnabled(false);

		// Add button JFrame window setup
		fileFolderJFrame.setVisible(true);
		fileFolderJFrame.setSize(100, 150);
		fileFolderJFrame.setLocation(500, 225);
		fileFolderJFrame.setResizable(false);

		// Create Add Button JFrame
		filePathPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		filePathPanel.add(addFilePathLbl);
		filePathPanel.add(addFilePathTextField);
		fileNamePanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		fileNamePanel.add(addFileName);
		fileNamePanel.add(addFileNameTextField);

		addFilePathTextField.setText(filePathText);
		addFilePathTextField.setEditable(false);
		filePathBox.add(filePathPanel);
		filePathBox.add(fileNamePanel);
		mainPanel.add(filePathBox);
		mainPanel.add(fileNameBox);
		mainPanel.add(addFileBtn);

		fileFolderJFrame.add(mainPanel);
		fileFolderJFrame.setVisible(true);

		// Add button listener to add more file folders
		addFileBtn.addActionListener(a -> {
		    File newFolder = new File(filePathText + "\\" + addFileNameTextField.getText());
		    newFolder.mkdir();
		    File newFile = new File(
			    newFolder.toString().concat("\\" + addFileNameTextField.getText() + "-File Storage.txt"));

		    try {
			newFile.createNewFile();

			fileFolderList.add(new FileFolder(addFileNameTextField.getText(),
				filePathText + "\\" + addFileNameTextField.getText()));

			addFileNameTextField.setText("");

			JOptionPane.showMessageDialog(this, "Reclick the Root Folder to refresh.",
				addFileNameTextField.getText() + ", added", JOptionPane.INFORMATION_MESSAGE);

			DefaultTreeModel model = (DefaultTreeModel) treeRootNode.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			root.removeAllChildren();
			model.reload();

		    } catch (IOException e) {
			e.printStackTrace();
		    }

		}); // END OF addFileBtn.addActionListener

		// Enables search file window again
		fileFolderJFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			setEnabled(true);
		    }
		});

	    } // END OF if (selectedNode.isRoot())

	    for (FileFolder ff : fileFolderList) {
		if (selectedNode.getUserObject().equals(ff.getFolderName())) {
		    String filePathText = rootFilePath + "\\" + selectedNode.getUserObject();

		    // Create Add Button JFrame
		    JFrame filePathStorageJFrame = new JFrame("Add New Storage Doc:");
		    // disables search file window again
		    setEnabled(false);

		    // Add button JFrame window setup
		    filePathStorageJFrame.setVisible(true);
		    filePathStorageJFrame.setSize(100, 150);
		    filePathStorageJFrame.setLocation(500, 225);
		    filePathStorageJFrame.setResizable(false);

		    // Create Add Button JFrame
		    filePathPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		    filePathPanel.add(addFilePathLbl);
		    filePathPanel.add(addFilePathTextField);
		    fileNamePanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		    fileNamePanel.add(addFileName);
		    fileNamePanel.add(addFileNameTextField);

		    addFilePathTextField.setText(filePathText);
		    addFilePathTextField.setEditable(false);
		    filePathBox.add(filePathPanel);
		    fileNameBox.add(fileNamePanel);
		    mainPanel.add(filePathBox);
		    mainPanel.add(fileNameBox);
		    mainPanel.add(addFileBtn);

		    filePathStorageJFrame.add(mainPanel);
		    filePathStorageJFrame.setVisible(true);

		    // Add button listener to add more file storage file
		    addFileBtn.addActionListener(a -> {

			System.out.println(filePathText + "\\" + addFileNameTextField.getText() + "-File Storage.txt");

			File newStorageDoc = new File(
				filePathText + "\\" + addFileNameTextField.getText() + "-File Storage.txt");

			try {
			    newStorageDoc.createNewFile();

			    filePathStorageList.add(new FilePathStorage(addFileNameTextField.getText(),
				    filePathText + "\\" + addFileNameTextField.getText()));

			    addFileNameTextField.setText("");

			    DefaultTreeModel model = (DefaultTreeModel) treeRootNode.getModel();
			    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			    root.removeAllChildren();
			    model.reload();

			} catch (IOException e) {
			    e.printStackTrace();
			}

		    }); // END OF addFileBtn.addActionListener

		    // Enables search file window again
		    filePathStorageJFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			    setEnabled(true);
			}
		    });

		}
	    }

	    for (

	    FilePathStorage ff : filePathStorageList) {
		if (selectedNode.getUserObject().equals(ff.getFilePathStorageName())) {
		    // Create Add Button JFrame
		    JFrame filePathObjectFrame = new JFrame("Add New file/folder:");
		    // disables search file window again
		    setEnabled(false);

		    // Add button JFrame window setup
		    filePathObjectFrame.setVisible(true);
		    filePathObjectFrame.setSize(100, 150);
		    filePathObjectFrame.setLocation(500, 225);
		    filePathObjectFrame.setResizable(false);

		    // Create Add Button JFrame
		    filePathPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		    filePathPanel.add(addFilePathLbl);
		    filePathPanel.add(addFilePathTextField);
		    fileNamePanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		    fileNamePanel.add(addFileName);
		    fileNamePanel.add(addFileNameTextField);

		    filePathBox.add(filePathPanel);
		    fileNameBox.add(fileNamePanel);
		    mainPanel.add(filePathBox);
		    mainPanel.add(fileNameBox);
		    mainPanel.add(addFileBtn);
		    mainPanel.add(JFileChooserBtn);

		    filePathObjectFrame.add(mainPanel);
		    filePathObjectFrame.setVisible(true);

		    System.out.println(ff.getFilePathStorageFilePath());

		    // Add button listener to add more file storage file
		    addFileBtn.addActionListener(a -> {

			try {
			    File newFile = new File(ff.getFilePathStorageFilePath());
			    FileWriter writeNewFilePath = new FileWriter(newFile);
			    BufferedWriter buffWriteNewFilePath = new BufferedWriter(writeNewFilePath);

			    ff.getFilePathObjectList().add(
				    new FilePathObject(addFileNameTextField.getText(), addFilePathTextField.getText()));

			    for (int m = 0; m < (ff.getFilePathObjectList().size()); m++) {

				System.out.println(ff.showFilePathObjectToString(m));
				buffWriteNewFilePath.write(ff.showFilePathObjectToString(m));
				buffWriteNewFilePath.newLine();
			    }

			    buffWriteNewFilePath.close();

			    JOptionPane.showMessageDialog(this, "Reclick the Root Folder to refresh.",
				    addFileNameTextField.getText() + ", added to " + ff.getFilePathStorageName(),
				    JOptionPane.INFORMATION_MESSAGE);

			    addFilePathTextField.setText("");
			    addFileNameTextField.setText("");

			    DefaultTreeModel model = (DefaultTreeModel) treeRootNode.getModel();
			    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			    root.removeAllChildren();
			    model.reload();

			} catch (IOException e) {
			    e.printStackTrace();
			}

		    }); // END OF addFileBtn.addActionListener
		    
		    // Add file by JFileChooser
		    JFileChooserBtn.addActionListener(s -> {
			JFileChooser filePathSelected = new JFileChooser();
			String userDir = System.getProperty("user.home");
			
			filePathSelected.setCurrentDirectory(new File(userDir + "\\Desktop"));
			filePathSelected.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int result = filePathSelected.showOpenDialog(this);
			
			if(result == JFileChooser.APPROVE_OPTION) {
			    addFilePathTextField.setText(filePathSelected.getSelectedFile().toString());
			    addFileNameTextField.setText(filePathSelected.getSelectedFile().getName());
			}		
		    });
		    

		    // Enables search file window again
		    filePathObjectFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			    setEnabled(true);
			}
		    });

		    break;
		}
	    }
	}); // END OF addBtn.addActionListener(a->{

	// Opens selected file
	openBtn.addActionListener(b -> {
	    try {
		File fileToOpen = new File(filePath);
		Runtime open = Runtime.getRuntime();

		if (fileToOpen.isDirectory() || fileToOpen.isFile()) {
		    open.exec("explorer " + filePath);
		}

		else if (filePath.contains(".com") || filePath.contains(".org") || filePath.contains(".net")
			|| filePath.contains(".edu")) {
		    Desktop desktop = java.awt.Desktop.getDesktop();
		    URI oURL = new URI(filePath);
		    desktop.browse(oURL);
		}

		else {
		    open.exec(filePath);

		    Desktop desktop = java.awt.Desktop.getDesktop();
		    URI oURL = new URI("http://www.google.com");
		    desktop.browse(oURL);

		}
	    } catch (IOException | URISyntaxException ex) {
		ex.printStackTrace();
	    }
	});

	// Delete selected file
	deleteBtn.addActionListener(dl -> {
	    String nodeFilePath = "";
	    String nodeName = "";
	    int selection = 0;

	    if (filePath.isBlank()) {
		JOptionPane.showMessageDialog(this, "No file was selected", "ATTENTION",
			JOptionPane.INFORMATION_MESSAGE);
	    }

	    // Displays if node is a File Folder
	    for (FileFolder ff : fileFolderList) {
		if (selectedNode.getUserObject().equals(ff.getFolderName())) {
		    nodeFilePath = ff.getFolderFilePath();
		    nodeName = ff.getFolderName();

		    selection = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + nodeName + "?",
			    "Confirm Selection", JOptionPane.YES_NO_OPTION);

		    break;
		}
	    }

	    // Displays if node is a File Path Storage
	    for (FilePathStorage fps : filePathStorageList) {
		if (selectedNode.getUserObject().equals(fps.getFilePathStorageName())) {
		    nodeFilePath = fps.getFilePathStorageFilePath();
		    nodeName = fps.getFilePathStorageName();

		    selection = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + nodeName + "?",
			    "Confirm Selection", JOptionPane.YES_NO_OPTION);

		    break;
		}
	    }

	    // Display file path of files in filePathJText
	    for (FilePathObject fpo : filePathObjectListTwo) {
		if (selectedNode.getUserObject().toString().contains(fpo.getFileName())) {
		    nodeFilePath = fpo.getFilePath();
		    nodeName = fpo.getFileName();

		    selection = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + nodeName + "?",
			    "Confirm Selection", JOptionPane.YES_NO_OPTION);

		    break;
		}
	    }

	    // This is where the folder or file gets deleted
	    switch (selection) {
	    // If user wants to delete a folder or file
	    case 0:
		// If user wants to delete folder
		if (nodeTypeJTextFld.getText().equals("File Folder")) {
		    for (FileFolder ff : fileFolderList) {
			if (ff.getFolderName().equals(nodeName)) {

			    File folderToDelete = new File(nodeFilePath);
			    File[] filesInFolder = folderToDelete.listFiles();

			    for (int i = 0; 0 < filesInFolder.length;) {
				filesInFolder[i].delete();
				folderToDelete.delete();
				fileFolderList.remove(ff);

				JOptionPane.showMessageDialog(this, "Reclick root folder to refresh.",
					nodeName + ", deleted", JOptionPane.INFORMATION_MESSAGE);

				DefaultTreeModel model = (DefaultTreeModel) treeRootNode.getModel();
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
				root.removeAllChildren();
				model.reload();

				break;
			    }
			}
		    }
		} // END OF, if(nodeTypeJTextFld.getText().equals("File Folder"))

		// If user wants to delete a storage file
		else if (nodeTypeJTextFld.getText().equals("File Path Storage")) {
		    for (FilePathStorage fps : filePathStorageList) {
			if (fps.getFilePathStorageName().equals(nodeName)) {
			    File fileToDelete = new File(nodeFilePath);
			    fileToDelete.delete();

			    JOptionPane.showMessageDialog(this, "Reclick root folder to refresh.",
				    nodeName + ", deleted", JOptionPane.INFORMATION_MESSAGE);

			    DefaultTreeModel model = (DefaultTreeModel) treeRootNode.getModel();
			    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			    root.removeAllChildren();
			    model.reload();

			    break;
			}
		    }
		} // END OF, else if(nodeTypeJTextFld.getText().equals("File Path Storage"))

		// If user wants to delete a file
		else if (nodeTypeJTextFld.getText().equals("File")) {
		    for (int i = 0; i < filePathStorageList.size(); i++) {
			if (selectedNode.getParent().toString()
				.equals(filePathStorageList.get(i).getFilePathStorageName())) {
			    for (int j = 0; j < filePathStorageList.get(i).getFilePathObjectList().size(); j++) {
				if (selectedNode.getUserObject().toString().equals(
					filePathStorageList.get(i).getFilePathObjectList().get(j).getFileName())) {

				    // Remove file from filePathStorageList.get(i).getFilePathObjectList()
				    filePathStorageList.get(i).getFilePathObjectList()
					    .remove(filePathStorageList.get(i).getFilePathObjectList().get(j));

				    File f = new File(filePathStorageList.get(i).getFilePathStorageFilePath());
				    try {
					FileWriter fw = new FileWriter(f);
					BufferedWriter bw = new BufferedWriter(fw);

					for (int k = 0; k < filePathStorageList.get(i).getFilePathObjectList()
						.size(); k++) {

					    bw.write(filePathStorageList.get(i).getFilePathObjectList().get(k)
						    .getFileName() + ","
						    + filePathStorageList.get(i).getFilePathObjectList().get(k)
							    .getFilePath());

					    bw.newLine();

					}

					bw.close();

				    } catch (IOException e) {
					e.printStackTrace();
				    }

				    JOptionPane.showMessageDialog(this, "Reclick root folder to refresh.",
					    nodeName + ", deleted", JOptionPane.INFORMATION_MESSAGE);

				    DefaultTreeModel model = (DefaultTreeModel) treeRootNode.getModel();
				    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
				    root.removeAllChildren();
				    model.reload();

				    break;
				}

			    }

			} // END OFF, main 'i' for loop

		    }

		} // END OF, else if(nodeTypeJTextFld.getText().equals("File Path Storage"))

		break;
	    case 1:
		JOptionPane.showMessageDialog(this, ", NOT Deleted", "Attention", JOptionPane.INFORMATION_MESSAGE);

		break;
	    }

	}); // END OF deleteBtn.addActionListener

	/*
	 * Sorts the JTree
	 */
	// Sort A to Z
	sortByNameAscBtn.addActionListener(sn -> {
	    // Intialize vars for sorting
	    selectedNode = defaultMutableTreeNode;
	    path = new TreePath(selectedNode.getPath());
	    
	    // Removes children
	    model = (DefaultTreeModel) treeRootNode.getModel();
	    root = (DefaultMutableTreeNode) model.getRoot();
	    root.removeAllChildren();
	    filePathObjectListTwo.removeAll(filePathObjectListTwo);
	    
	    // Creates file object list
	    createFileObjectListTwo();

	    // Sorts file
	    sortFile = new SortFiles();
	    Collections.sort(filePathObjectListTwo, sortFile.sortAlphaAscendingFile());

	    for (FilePathObject fpo : filePathObjectListTwo) {
		fileObjectNode = new DefaultMutableTreeNode(fpo.getFileName());
		defaultMutableTreeNode.add(fileObjectNode);
	    }

	    // Reloads the JTree
	    model.reload();
	});

	// Sort Z to A
	sortByNameDescBtn.addItemListener(sn -> {
	    // Intialize vars for sorting
	    selectedNode = defaultMutableTreeNode;
	    path = new TreePath(selectedNode.getPath());
	    filePathObjectListTwo.removeAll(filePathObjectListTwo);

	    // Removes children
	    model = (DefaultTreeModel) treeRootNode.getModel();
	    root = (DefaultMutableTreeNode) model.getRoot();
	    root.removeAllChildren();

	    // Creates file object list
	    createFileObjectListTwo();

	    // Sorts file
	    sortFile = new SortFiles();
	    Collections.sort(filePathObjectListTwo, sortFile.sortAlphaDescendingFile());

	    for (FilePathObject fpo : filePathObjectListTwo) {
		fileObjectNode = new DefaultMutableTreeNode(fpo.getFileName());
		defaultMutableTreeNode.add(fileObjectNode);
	    }

	    // Reloads the JTree
	    model.reload();
	});

	// Sort by date modified
	sortByDateBtn.addItemListener(sn -> {
	    // Intialize vars for sorting
	    selectedNode = defaultMutableTreeNode;
	    path = new TreePath(selectedNode.getPath());
	    filePathObjectListTwo.removeAll(filePathObjectListTwo);

	    // Removes children
	    model = (DefaultTreeModel) treeRootNode.getModel();
	    root = (DefaultMutableTreeNode) model.getRoot();
	    root.removeAllChildren();

	    // Creates file object list
	    createFileObjectListTwo();

	    // Sorts file
	    sortFile = new SortFiles();
	    Collections.sort(filePathObjectListTwo, sortFile.sortByDateModifiedFile());

	    for (FilePathObject fpo : filePathObjectListTwo) {
		fileObjectNode = new DefaultMutableTreeNode(fpo.getFileName());
		defaultMutableTreeNode.add(fileObjectNode);
	    }

	    // Reloads the JTree
	    model.reload();
	});

	// Reset JTree to default
	resetTreeBtn.addActionListener(sn -> {
	    // Deselects radio buttons
	    sortBtnGroup.clearSelection();
	    
	    // Intialize vars for sorting
	    selectedNode = defaultMutableTreeNode;
	    path = new TreePath(selectedNode.getPath());
	    filePathObjectListTwo.removeAll(filePathObjectListTwo);

	    // Removes children
	    model = (DefaultTreeModel) treeRootNode.getModel();
	    root = (DefaultMutableTreeNode) model.getRoot();
	    root.removeAllChildren();

	    // Make the default tree
	    makeJTree();

	    // Reloads the JTree
	    model.reload();
	});

    }

}
