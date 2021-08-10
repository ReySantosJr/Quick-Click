package guicode;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class DefaultFileMenu extends JFrame {
    /*
     * Fields
     */
    protected ArrayList<FilePathObject> defaultFileList = new ArrayList<>();
    protected SortFiles sortFile;
    protected String rootFilePath;
    protected String filePath = "";
    protected String quickClick = "";

    // Main panel
    protected JPanel mainPanel = new JPanel();

    // Layout
    protected GridBagConstraints gc = new GridBagConstraints();

    // Tree Nodes
    protected DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode("File List");
    protected JTree treeRootNode = new JTree(defaultMutableTreeNode);
    protected DefaultMutableTreeNode defaultFileNode;
    protected JScrollPane treeRootNodeScrollPane = new JScrollPane(treeRootNode);
    protected JPanel treeRootNodePanel = new JPanel();
    protected DefaultTreeModel model;
    protected DefaultMutableTreeNode selectedNode;
    protected TreePath path;
    protected DefaultMutableTreeNode root;

    // Vars to display file name and path and to open
    protected Box titleAndSortingBox = Box.createHorizontalBox();
    protected Box fileNameBox = Box.createHorizontalBox();
    protected Box fileSizeBox = Box.createHorizontalBox();
    protected Box dateModifiedBox = Box.createHorizontalBox();
    protected Box filePathBox = Box.createVerticalBox();

    // Sorting buttons
    protected ButtonGroup sortBtnGroup = new ButtonGroup();
    protected JRadioButton sortByNameAscBtn = new JRadioButton("⬆-Alpha");
    protected JRadioButton sortByNameDescBtn = new JRadioButton("⬇-Alpha");
    protected JRadioButton sortByFileSizeBtn = new JRadioButton("File Size");
    protected JRadioButton sortByDateBtn = new JRadioButton("Last Modified");
    protected JPanel topPanel = new JPanel();

    protected JLabel filePathLbl = new JLabel("File Path: ");
    protected JTextField filePathJTextFld = new JTextField(30);
    protected JLabel fileSizeLbl = new JLabel("File Size: ");
    protected JTextField fileSizeJTextFld = new JTextField(30);
    protected JLabel dateModifiedLbl = new JLabel("Date Last Modified: ");
    protected JTextField dateModifiedJTextFld = new JTextField(24);

    protected JButton deleteBtn = new JButton("delete");
    protected JPanel deleteBtnPanel = new JPanel();
    protected JButton openBtn = new JButton("open");
    protected JPanel openBtnPanel = new JPanel();
    protected JButton addBtn = new JButton("add");
    protected JPanel addBtnPanel = new JPanel();

    /*
     * TODO
     * 
     * Make file size and date last modified optional buttons
     */

    FilePathObject file_path_object;

    /*
     * Constructor
     */
    DefaultFileMenu(String rootFilePath, ArrayList<FilePathObject> defaultFileList, String quickClick) {
	// Window Settings
	super("Default File Menu");
	this.rootFilePath = rootFilePath;
	this.defaultFileList = defaultFileList;
	setSize(380, 420);
	setLocation(50, 225);
	setResizable(false);
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	this.quickClick = quickClick;

	// treeRootNodeScrollPane bar setup. Added to makeWorldTreePanel
	treeRootNodeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	treeRootNodeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	treeRootNodeScrollPane.setPreferredSize(new Dimension(340, 180));
	treeRootNodeScrollPane.setBorder(BorderFactory.createEtchedBorder());
	treeRootNodePanel.setBorder(BorderFactory.createTitledBorder("Select Folder ↓Click to See Files↓"));
	treeRootNodePanel.add(treeRootNodeScrollPane);

	// Make button group
	sortBtnGroup.add(sortByNameAscBtn);
	sortBtnGroup.add(sortByNameDescBtn);
	sortBtnGroup.add(sortByFileSizeBtn);
	sortBtnGroup.add(sortByDateBtn);

	// Make button panel
	topPanel.setBorder(BorderFactory.createTitledBorder("Sorting"));
	topPanel.add(sortByNameAscBtn);
	topPanel.add(sortByNameDescBtn);
	topPanel.add(sortByFileSizeBtn);
	topPanel.add(sortByDateBtn);
	titleAndSortingBox.add(topPanel);

	dateModifiedBox.add(dateModifiedLbl);
	dateModifiedBox.add(dateModifiedJTextFld);
	dateModifiedJTextFld.setEditable(false);
	dateModifiedBox.setBorder(BorderFactory.createEtchedBorder());

	fileSizeBox.add(fileSizeLbl);
	fileSizeBox.add(fileSizeJTextFld);
	fileSizeJTextFld.setEditable(false);
	fileSizeBox.setBorder(BorderFactory.createEtchedBorder());

	fileNameBox.add(filePathLbl);
	fileNameBox.add(filePathJTextFld);
	filePathJTextFld.setEditable(false);
	filePathBox.setBorder(BorderFactory.createEtchedBorder());
	filePathBox.add(fileNameBox);

	mainPanel.setLayout(new GridBagLayout());

	gc.anchor = GridBagConstraints.NORTH;
	gc.weightx = 0.5;
	gc.weighty = 0.5;
	gc.gridx = 0;
	gc.gridy = 0;
	mainPanel.add(treeRootNodePanel, gc);
	
	gc.anchor = GridBagConstraints.LINE_START;
	gc.weightx = 0.5;
	gc.weighty = 0.5;
	gc.gridx = 0;
	gc.gridy = 1;
	mainPanel.add(titleAndSortingBox, gc);

	gc.anchor = GridBagConstraints.LINE_START;
	gc.weightx = 0.5;
	gc.weighty = 0.5;
	gc.gridx = 0;
	gc.gridy = 2;
	mainPanel.add(filePathBox, gc);

	gc.anchor = GridBagConstraints.LINE_START;
	gc.weightx = 0.5;
	gc.weighty = 0.5;
	gc.gridx = 0;
	gc.gridy = 3;
	mainPanel.add(fileSizeBox, gc);

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

	add(mainPanel);
	setVisible(true);

    } // END OF CONSTRUCTOR

    public void presentDefaultFiles() throws FileNotFoundException {
	// Create JTree
	treeRootNode.addTreeSelectionListener(d -> {
	    for (FilePathObject fpo : defaultFileList) {
		defaultFileNode = new DefaultMutableTreeNode(fpo.getFileName());
		defaultMutableTreeNode.add(defaultFileNode);
	    }
	});

	// Show file path, last date modified, and file size
	treeRootNode.getSelectionModel().addTreeSelectionListener(x -> {
	    selectedNode = (DefaultMutableTreeNode) treeRootNode.getLastSelectedPathComponent();

	    for (FilePathObject fpo2 : defaultFileList) {
		if (selectedNode.getUserObject().toString().contains(fpo2.getFileName())) {
		    filePath = fpo2.getFilePath();
		    filePathJTextFld.setText(filePath);
		    DecimalFormat df = new DecimalFormat(".##");
		    df.setRoundingMode(RoundingMode.CEILING);
		    double fileSize = 0, folderSize = 0, kiloBytes = 0, megaBytes = 0, gigaBytes = 0;

		    File file = new File(filePath);
		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss");
		    dateModifiedJTextFld.setText(sdf.format(file.lastModified()));

		    if (file.isFile()) {
			fileSize = file.length();

			if (fileSize < 1000) {
			    fileSizeJTextFld.setText(String.valueOf(df.format(fileSize) + " bytes"));
			}

			if (fileSize >= 1000) {
			    kiloBytes = fileSize / 1000;
			    fileSizeJTextFld.setText(String.valueOf(df.format(kiloBytes) + " kilobytes"));
			}

			if (fileSize >= 1000000) {
			    megaBytes = fileSize / 1000000;
			    fileSizeJTextFld.setText(String.valueOf(df.format(megaBytes) + " megabytes"));
			}

			if (fileSize >= 1000000000) {
			    gigaBytes = fileSize / 1000000;
			    fileSizeJTextFld.setText(String.valueOf(df.format(gigaBytes) + " gigabytes"));
			}

		    }

		    else if (file.isDirectory()) {
			try {
			    folderSize = Files.walk(new File(filePath).toPath()).map(f -> f.toFile())
				    .filter(f -> f.isFile()).mapToLong(f -> f.length()).sum();

			    if (folderSize < 1000) {
				fileSizeJTextFld.setText(String.valueOf(df.format(folderSize) + " bytes"));
			    }

			    if (folderSize > 1000) {
				kiloBytes = folderSize / 1000;
				fileSizeJTextFld.setText(String.valueOf(df.format(kiloBytes) + " kilobytes"));
			    }

			    if (folderSize > 1000000) {
				megaBytes = folderSize / 1000000;
				fileSizeJTextFld.setText(String.valueOf(df.format(megaBytes) + " megabytes"));
			    }

			    if (folderSize > 1000000000) {
				gigaBytes = folderSize / 1000000000;
				fileSizeJTextFld.setText(String.valueOf(df.format(gigaBytes) + " gigabytes"));
			    }

			} catch (IOException e) {

			    e.printStackTrace();
			}
		    }
		    
		    else if (file.toString().contains(".com") || 
			    file.toString().contains("https://")){
			fileSizeJTextFld.setText("Website");
			dateModifiedJTextFld.setText("");
		    }
		    
		    else {
			fileSizeJTextFld.setText("ERROR");
			dateModifiedJTextFld.setText("ERROR");
		    }
		}
	    }
	});

	// Sort by name
	sortByNameAscBtn.addItemListener(sn -> {
	    // Intialize vars for sorting
	    selectedNode = defaultMutableTreeNode;
	    path = new TreePath(selectedNode.getPath());

	    // Removes children
	    model = (DefaultTreeModel) treeRootNode.getModel();
	    root = (DefaultMutableTreeNode) model.getRoot();
	    root.removeAllChildren();

	    // Sorts file
	    sortFile = new SortFiles();
	    Collections.sort(defaultFileList, sortFile.sortAlphaAscendingFile());

	    // Creates new nodes with the newly sorted list
	    for (FilePathObject fpo : defaultFileList) {
		defaultFileNode = new DefaultMutableTreeNode(fpo.getFileName());
		defaultMutableTreeNode.add(defaultFileNode);
	    }

	    // Reloads the JTree
	    model.reload();
	});

	sortByNameDescBtn.addItemListener(sn -> {
	    // Intialize vars for sorting
	    selectedNode = defaultMutableTreeNode;
	    path = new TreePath(selectedNode.getPath());

	    // Removes children
	    model = (DefaultTreeModel) treeRootNode.getModel();
	    root = (DefaultMutableTreeNode) model.getRoot();
	    root.removeAllChildren();

	    // Sorts file
	    sortFile = new SortFiles();
	    Collections.sort(defaultFileList, sortFile.sortAlphaDescendingFile());

	    // Creates new nodes with the newly sorted list
	    for (FilePathObject fpo : defaultFileList) {
		defaultFileNode = new DefaultMutableTreeNode(fpo.getFileName());
		defaultMutableTreeNode.add(defaultFileNode);
	    }

	    // Reloads the JTree
	    model.reload();
	});

	// Sort by file size
	sortByFileSizeBtn.addItemListener(sn -> {
	    // Intialize vars for sorting
	    selectedNode = defaultMutableTreeNode;
	    path = new TreePath(selectedNode.getPath());

	    // Removes children
	    model = (DefaultTreeModel) treeRootNode.getModel();
	    root = (DefaultMutableTreeNode) model.getRoot();
	    root.removeAllChildren();

	    // Sorts file
	    sortFile = new SortFiles();
	    Collections.sort(defaultFileList, sortFile.sortFileSizeFile());

	    // Creates new nodes with the newly sorted list
	    for (FilePathObject fpo : defaultFileList) {
		defaultFileNode = new DefaultMutableTreeNode(fpo.getFileName());
		defaultMutableTreeNode.add(defaultFileNode);
	    }

	    // Reloads the JTree
	    model.reload();
	});

	// Sort by date
	sortByDateBtn.addItemListener(sn -> {
	    // Intialize vars for sorting
	    selectedNode = defaultMutableTreeNode;
	    path = new TreePath(selectedNode.getPath());

	    // Removes children
	    model = (DefaultTreeModel) treeRootNode.getModel();
	    root = (DefaultMutableTreeNode) model.getRoot();
	    root.removeAllChildren();

	    // Sorts file
	    sortFile = new SortFiles();
	    Collections.sort(defaultFileList, sortFile.sortByDateModifiedFile());

	    // Creates new nodes with the newly sorted list
	    for (FilePathObject fpo : defaultFileList) {
		defaultFileNode = new DefaultMutableTreeNode(fpo.getFileName());
		defaultMutableTreeNode.add(defaultFileNode);
	    }

	    // Reloads the JTree
	    model.reload();
	});

	// Open file
	openBtn.addActionListener(b -> {
	    DefaultMutableTreeNode selectedNodeToOpen = (DefaultMutableTreeNode) treeRootNode
		    .getLastSelectedPathComponent();
	    for (int i = 0; i < defaultFileList.size(); i++) {
		if (selectedNodeToOpen.getUserObject().toString().equals(defaultFileList.get(i).getFileName())) {
		    defaultFileList.get(i).openFile(this);
		}
	    }
	});

	// Delete file
	deleteBtn.addActionListener(dl -> {
	    if (filePath.isBlank()) {
		JOptionPane.showMessageDialog(this, "No file was selected", "ATTENTION",
			JOptionPane.INFORMATION_MESSAGE);
	    }

	    else {
		for (int i = 0; i < defaultFileList.size(); i++) {
		    if (filePath == defaultFileList.get(i).getFilePath()) {
			int selection = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete " + defaultFileList.get(i).getFileName() + "?",
				"Confirm Selection", JOptionPane.YES_NO_CANCEL_OPTION);

			switch (selection) {
			case 0:
			    String oldFile = defaultFileList.get(i).getFileName();
			    defaultFileList.remove(defaultFileList.get(i));

			    try {
				FileWriter writeNewFilePath = new FileWriter(
					rootFilePath + "\\0-Default Files\\Default-File Storage.txt");
				BufferedWriter buffWriteNewFilePath = new BufferedWriter(writeNewFilePath);

				for (int m = 0; m < (defaultFileList.size()); m++) {
				    buffWriteNewFilePath.write(defaultFileList.get(m).getFileName() + ","
					    + defaultFileList.get(m).getFilePath());
				    buffWriteNewFilePath.newLine();
				}

				buffWriteNewFilePath.write(quickClick + ",null");

				buffWriteNewFilePath.close();

			    } catch (IOException e) {
				e.printStackTrace();
			    }

			    JOptionPane.showMessageDialog(this, "Reclick the Default File to refresh.",
				    oldFile + ", deleted", JOptionPane.INFORMATION_MESSAGE);

			    DefaultTreeModel model = (DefaultTreeModel) treeRootNode.getModel();
			    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			    root.removeAllChildren();
			    model.reload();

			    break;

			case 1:
			    JOptionPane.showMessageDialog(this, defaultFileList.get(i).getFileName() + ", NOT Deleted",
				    "Attention", JOptionPane.INFORMATION_MESSAGE);
			    break;
			case 2:
			    break;
			}
		    }

		}
	    }

	}); // END OF deleteBtn.addActionListener

	// Add file
	addBtn.addActionListener(ad -> {
	    // Variables needed
	    JFrame addFilePathJFrame = new JFrame("Add New file/folder:");
	    JPanel mainPanel = new JPanel();
	    JPanel filePathPanel = new JPanel();
	    JPanel fileNamePanel = new JPanel();
	    Box filePathBox = Box.createHorizontalBox();
	    Box fileNameBox = Box.createHorizontalBox();

	    JLabel addFilePathLbl = new JLabel("File Path: ");
	    JTextField addFilePathTextField = new JTextField(25);
	    JLabel addFileName = new JLabel("Name: ");
	    JTextField addFileNameTextField = new JTextField(26);
	    JButton addFileBtn = new JButton("Add File");
	    JButton JFileChooserBtn = new JButton("Browse Computer");
	    setEnabled(false);

	    // Window setup
	    addFilePathJFrame.setVisible(true);
	    addFilePathJFrame.setSize(100, 150);
	    addFilePathJFrame.setLocation(500, 225);
	    addFilePathJFrame.setResizable(false);

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

	    addFilePathJFrame.add(mainPanel);
	    addFilePathJFrame.setVisible(true);

	    addFileBtn.addActionListener(a -> {
		if (addFileNameTextField.getText().contentEquals("")
			|| addFilePathTextField.getText().contentEquals("")) {
		    JOptionPane.showMessageDialog(addFileBtn, "Information is not complete.", "Attention",
			    JOptionPane.INFORMATION_MESSAGE);
		}
		else {

		    // Add the new file
		    file_path_object = new FilePathObject(addFileNameTextField.getText(),
			    addFilePathTextField.getText());
		    defaultFileList.add(file_path_object);

		    // Update the default file list
		    try {
			FileWriter writeNewFilePath = new FileWriter(
				rootFilePath + "\\0-Default Files\\Default-File Storage.txt");
			BufferedWriter buffWriteNewFilePath = new BufferedWriter(writeNewFilePath);

			for (int m = 0; m < (defaultFileList.size()); m++) {
			    buffWriteNewFilePath.write(
				    defaultFileList.get(m).getFileName() + "," + defaultFileList.get(m).getFilePath());
			    buffWriteNewFilePath.newLine();
			}

			buffWriteNewFilePath.write(quickClick + ",null");

			buffWriteNewFilePath.close();

		    } catch (IOException e) {
			e.printStackTrace();
		    }

		    addFileNameTextField.setText("");
		    addFilePathTextField.setText("");

		    JOptionPane.showMessageDialog(addFileBtn, "Reclick the Default File to refresh.",
			    addFileNameTextField.getText() + " added to default list", JOptionPane.INFORMATION_MESSAGE);

		    DefaultTreeModel model = (DefaultTreeModel) treeRootNode.getModel();
		    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		    root.removeAllChildren();
		    model.reload();

		    // Enables search file window again
		    addFilePathJFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			    setEnabled(true);
			}
		    }); 
			
		    // Enables search file window again
		    addFilePathJFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			    setEnabled(true);
			}
		    });
		}
	    }); // END OF addFileBtn.addActionListener(a -> {
	    
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
	    addFilePathJFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		@Override
		public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    setEnabled(true);
		    System.out.println("Hello!!!");
		}
	    });

	}); // END OF addBtn.addActionListener
    }

}
