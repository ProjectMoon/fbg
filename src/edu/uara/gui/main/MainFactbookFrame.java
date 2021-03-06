/*
 * This file is part of the Factbook Generator.
 *
 * The Factbook Generator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Factbook Generator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with The Factbook Generator.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2008, 2009 Bradley Brown, Dustin Yourstone, Jeffrey Hair, Paul Halvorsen, Tu Hoang
 */
package edu.uara.gui.main;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.uara.FBGConstants;
import edu.uara.FBGFlow;
import edu.uara.FactbookCompiler;
import edu.uara.config.Config;
import edu.uara.cutnpaste.QueryBuilderTuple;
import edu.uara.cutnpaste.TableSelection;
import edu.uara.db.profiles.ProfileManager;
import edu.uara.gui.*;
import edu.uara.gui.custom.BorderChooser;
import edu.uara.gui.custom.CloseTabbedPane;
import edu.uara.gui.custom.FontChooser;
import edu.uara.gui.datawizard.UploadWizard;
import edu.uara.gui.tableeditor.ChartGenerationFrame;
import edu.uara.project.FactbookProject;
import edu.uara.tableeditor.TableObject;
import edu.uara.tableeditor.query.NonEditableQueryBuilder;
import edu.uara.gui.tableeditor.calculator.CalculatorGUI;
import edu.uara.gui.tableeditor.constantswindow.ConstantsFrame;
import edu.uara.gui.tableeditor.querybuilder.QueryBuilderFrame;
import edu.uara.tableeditor.ITableObject;
import edu.uara.tableeditor.TableFigure;
import edu.uara.wrappers.CustomBorderGenerator;
import edu.uara.wrappers.CustomJTable;
import edu.uara.wrappers.CustomTableCellRenderer;
import edu.uara.wrappers.customcharts.CustomDatasetTable;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import java.util.HashMap;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelListener;



public class MainFactbookFrame extends JFrame {

    public static final String FRAME_TITLE = "Factbook Editor: ";
    private JPopupMenu tablePopupMenu;
    private JPopupMenu treePopupMenu;
    private JTextField equationTextBox;
    private JTree projectTree;
    private JTabbedPane mainTabbedPane;
    private JScrollPane treeScrollPane;

    private UndoBuffer undoBuffer;

    /**
     * table object and collection used for table generation
     */
    private ArrayList<TableObject> tabbedList;

    @SuppressWarnings("serial")
    public MainFactbookFrame() {
        super();
        updateTitle("No Factbook Open");
        tabbedList = new ArrayList<TableObject>();
        this.setJMenuBar(createMenu());
        tablePopupMenu = createPopupMenu();
        treePopupMenu = createTreePopupMenu();
        undoBuffer = new UndoBuffer();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        getContentPane().add(createTreeView(), java.awt.BorderLayout.WEST);
        getContentPane().add(createEquationPanel(), java.awt.BorderLayout.NORTH);
        getContentPane().add(createMainPanel(), java.awt.BorderLayout.CENTER);

        loadLastOpenProject();

        pack();
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /*
     * Start of menu action events
     *
     */
    public void newProfile() {
        new ProfileWizard();
    }

    public void loadProfile() {
        JFileChooser profileChooser = new JFileChooser();
        profileChooser.setFileFilter(new ProfileFileFilter());
        profileChooser.setCurrentDirectory(new File(FBGConstants.PROFILE_PATH));

        int val = profileChooser.showOpenDialog(this);

        if (val == JFileChooser.APPROVE_OPTION) {
            File newProfile = profileChooser.getSelectedFile();


            FBGFlow.setupProfile(newProfile);
            Config.setProperty("defaultProfile", newProfile.getAbsolutePath());
            Config.saveProperties();

            if (ProfileManager.CURRENT_PROFILE.isConnected()) {
                FBGFlow.init(false);
                System.out.println("Connected with " + ProfileManager.CURRENT_PROFILE.getProfileName());
            }
        }
    }

    private void loadLastOpenProject() {
        if (Config.getProperty("lastOpen") != null) {
            try {
                FactbookProject p = FactbookProject.loadFactbook((String) Config.getProperty("lastOpen"));
                FBGFlow.setOpenProject(p);

                //We don't create a new JTree or anything because
                //setting the model is sufficient for updating the tree.
                projectTree.setModel(p.generateTree());
                treeScrollPane.repaint();
                updateTitle(p.getName());
            } catch (IOException e) {
                e.printStackTrace();
                Utilities.displayError(this, "Error Loading Project", "The project file was corrupt. aborted load.");
                Config.removeProperty("lastOpen");
                Config.saveProperties();
            }
        }
    }

    public void newFactbook() {
        NewProjectDialog d = new NewProjectDialog(this);

        if (d.isCreated()) {
            projectTree.setModel(FBGFlow.getOpenProject().generateTree());
            treeScrollPane.repaint();
            updateTitle(FBGFlow.getOpenProject().getName());
        }
    }

    public void saveFactbook() {
        JFileChooser projectChooser = new JFileChooser();
        projectChooser.setCurrentDirectory(new File(FBGConstants.USER_FILE_PATH));
        projectChooser.setFileFilter(new ProjectFileFilter());

        int val = projectChooser.showSaveDialog(this);
        if (val == JFileChooser.APPROVE_OPTION) {
            //Get the file and make sure it's a .fbp file.
            String save = projectChooser.getSelectedFile().getPath();
            if (save.endsWith(".fbp") == false) {
                save += ".fbp";
            }
            final String savePath = save;


            System.out.println("Saving Factbook to " + savePath);
            FBGFlow.getOpenProject().save(savePath);
            Config.setProperty("lastOpen", save);

            System.out.println("Completed save");
        }
    }

    public void loadFactbook() {
        JFileChooser projectChooser = new JFileChooser();
        projectChooser.setCurrentDirectory(new File(FBGConstants.USER_FILE_PATH));
        projectChooser.setFileFilter(new ProjectFileFilter());

        int val = projectChooser.showOpenDialog(this);
        if (val == JFileChooser.APPROVE_OPTION) {
            try {
                //First close all open tabs
                mainTabbedPane.removeAll();
                mainTabbedPane.validate();
                mainTabbedPane.repaint();

                //Now continue with loading
                FactbookProject p = FactbookProject.loadFactbook(projectChooser.getSelectedFile().getPath());
                Config.setProperty("lastOpen", projectChooser.getSelectedFile().getPath());
                Config.saveProperties();
                FBGFlow.setOpenProject(p);

                //We don't create a new JTree or anything because
                //setting the model is sufficient for updating the tree.
                projectTree.setModel(p.generateTree());
                treeScrollPane.repaint();
                System.out.println("Loaded Factbook: " + p.getName());
                updateTitle(p.getName());
            } catch (IOException e) {
                e.printStackTrace();
                Utilities.displayError(this, "Error Loading Project", "The project file was corrupted. Aborted loading.");
            }


        }
    }

    public void exitProgram() {
        System.exit(0);
    }

    public void login() {
        if (ProfileManager.CURRENT_PROFILE.isConnected() == false) {
            ProfileManager.CURRENT_PROFILE.login();
        }
    }

    public void uploadFiles() {
        new UploadWizard(this, null);
    }

    public void serverView() {
        new ServerViewFrame();
    }

    public void compileFactbook() {
        if (FBGFlow.getOpenProject() != null) {
            System.out.println("Compiling Factbook...");
            JFileChooser folderChooser = new JFileChooser();
            folderChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int value = folderChooser.showOpenDialog(this);
            if (value == JFileChooser.APPROVE_OPTION) {
                FactbookCompiler.compileFactbook(FBGFlow.getOpenProject(), folderChooser.getSelectedFile().getPath() + File.separator);
            }
            System.out.println("Finished Compiling!");
        }
    }

    public void compileTable() {
        if (FBGFlow.getOpenProject() != null) {
            System.out.println("Compiling Factbook Table...");
            JFileChooser folderChooser = new JFileChooser();
            folderChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int value = folderChooser.showOpenDialog(this);
            if (value == JFileChooser.APPROVE_OPTION) {
                FactbookCompiler.compileTable(this.getCurrentTableObject(), folderChooser.getSelectedFile().getPath() + File.separator);
            }
            System.out.println("Finished Compiling!");
        }
    }

    /**
     * Creates a new instance of the calculator to allow the user to generate a new equation.
     */
    public void addEquation() {
        final TableObject to = this.getCurrentTableObject();
        //UndoableTableEdit edit = new UndoableTableEdit(to);
        //undoManager.addEdit(edit);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int sCell[][] = to.getSelectedCells();
                try {
                    CalculatorGUI calcGui = new CalculatorGUI(to, sCell[0][0], sCell[0][1], null);
                    calcGui.setVisible(true);
                } catch (Exception e) {
                    System.out.println("No Cell Selected");
                }
            }
        });
    }

    /**
     * Creates a new instance of the QueryBuilder to allow the user to generate a new query.
     */
    public void addQuery() {
        TableObject to = this.getCurrentTableObject();
        int sCell[][] = to.getSelectedCells();
        try {
            QueryBuilderFrame qbf = new QueryBuilderFrame(to, sCell[0][0], sCell[0][1], FBGFlow.getOpenProject().getTableEditorConstants());
        } catch (Exception e) {
            System.out.println("No Cell Selected");
        }
    }

    /**
     * Creates a new instance of a JColorChooser to allow the user to change the background color of all selected cells
     */
    public void changeBackground() {
        TableObject to = this.getCurrentTableObject();
        CustomTableCellRenderer rend = to.getCustomTableCellRenderer();
        int sCell[][] = to.getSelectedCells();
        try {
            Color newColor = JColorChooser.showDialog(this, "Choose Background Color", Color.white);
            for(int r = 0; r < sCell.length; r++) {
                rend.setBackgroundColor(newColor, sCell[r][0], sCell[r][1]);
            }
            to.setCustomTableCellRenderer(rend);
        } catch (Exception e) {
            System.out.println("No Cell Selected");
        }
    }

    /**
     * Creates a new instance of JColorChooser to allow the user to change the font color of the selected cells
     */
    public void changeForeground() {
        TableObject to = this.getCurrentTableObject();
        CustomTableCellRenderer rend = to.getCustomTableCellRenderer();
        int sCell[][] = to.getSelectedCells();
        try {
            Color newColor = JColorChooser.showDialog(this, "Choose Background Color", Color.white);
            for(int r = 0; r < sCell.length; r++) {
                rend.setForegroundColor(newColor, sCell[r][0], sCell[r][1]);
            }
            to.setCustomTableCellRenderer(rend);
        } catch (Exception e) {
            System.out.println("No Cell Selected");
        }
    }

    /**
     * Creates a new instance of FontChooser to allow the user to set basic font properties of the selected cells
     */
    public void changeFont() {
        TableObject to  = this.getCurrentTableObject();
        CustomTableCellRenderer rend = to.getCustomTableCellRenderer();
        int sCell[][] = to.getSelectedCells();
        try {
            FontChooser fc = new FontChooser(this);
            fc.setSelectedFont(rend.getFontFace(sCell[0][0], sCell[0][1]));
            fc.show();
            Font newFont = fc.getSelectedFont();
            if(newFont != null) {
                for(int r = 0; r < sCell.length; r++) {
                    rend.setFontFace(newFont, sCell[r][0], sCell[r][1]);
                }
            }
            to.setCustomTableCellRenderer(rend);
        } catch (Exception e) {
            System.out.println("No Cell Selected");
        }
    }

    public void changeBorder() {
        TableObject to  = this.getCurrentTableObject();
        CustomTableCellRenderer rend = to.getCustomTableCellRenderer();
        int sCell[][] = to.getSelectedCells();
        try {
            BorderChooser bc = new BorderChooser(rend.getCustomBorderGen(sCell[0][0], sCell[0][1]),
                    rend.getForegroundColor(sCell[0][0], sCell[0][1]),
                    rend.getBackgroundColor(sCell[0][0], sCell[0][1]));
            bc.setVisible(true);
            CustomBorderGenerator newBorder = bc.getBorder();
            if(newBorder != null) {
                for(int r = 0; r < sCell.length; r++) {
                    CustomBorderGenerator gen = new CustomBorderGenerator(newBorder);
                    System.out.println(newBorder.getEdgeColor(CustomBorderGenerator.BorderLocation.Top));
                    rend.setCustomBorderGen(gen, sCell[r][0], sCell[r][1]);
                }
            }
            to.setCustomTableCellRenderer(rend);
        } catch (Exception e) {
            System.out.println("No Cell Selected");
        }
    }
    
    public void mergeEm() {
    	TableObject to = this.getCurrentTableObject();
    	int sCells[][] = to.getSelectedCells();
    	int minRow = 0, maxRow = 0;
    	int minColumn = 0, maxColumn = 0;
    	
    	if(sCells == null) {
    		System.out.println("No Cells Selected");
    		return;
    	}
    	if(sCells.length == 1)
    		return;
    	
    	minRow = sCells[0][0];
    	maxRow = sCells[0][0];
    	
    	minColumn = sCells[0][1];
    	maxColumn = sCells[0][1];
    	
    	for(int cellNum = 0; cellNum < sCells.length; cellNum++) {
    		if(minRow > sCells[cellNum][0])
    			minRow = sCells[cellNum][0];
    		if(maxRow < sCells[cellNum][0])
    			maxRow = sCells[cellNum][0];
    		
    		if(minColumn > sCells[cellNum][1])
    			minColumn = sCells[cellNum][1];
    		if(maxColumn < sCells[cellNum][1])
    			maxColumn = sCells[cellNum][1];
    	}
    	
    	//to.table.unmergeHorizontal(minRow, minColumn);
    	//to.table.unmergeVertical(minRow, minColumn);
    	
    	try {
    		to.table.mergeHorizontal(minRow, minColumn, maxColumn - minColumn +1);
    		//to.table.mergeVertical(minRow, minColumn, maxRow - minRow + 1);
    	}
    	catch(Exception e) {
    		System.out.println("Disjointed Cells");
    	}
    }
    
    public void unMergeEm() {
    	TableObject to = this.getCurrentTableObject();
    	int sCells[][] = to.getSelectedCells();
    	
    	try {
    		to.table.unmergeHorizontal(sCells[0][0], sCells[0][1]);
    		//to.table.unmergeVertical(sCells[0][0], sCells[0][1]);
    	}
    	catch (Exception e) {
    		System.out.println("Not Merged");
    	}
    }

    public void doAutoWidth() {
        TableObject to = this.getCurrentTableObject();
        CustomTableCellRenderer rend = to.getCustomTableCellRenderer();
        int sCell[][] = to.getSelectedCells();
        try {
            for(int r = 0; r < sCell.length; r++) {
                rend.autoWidth(to.table, sCell[r][1]);
            }
        } catch (Exception e) {
            System.out.println("No Cell Selected");
        }
    }

    public void newTable() {
        if (FBGFlow.getOpenProject() != null) {
            NewTableWizard wiz = new NewTableWizard(this);
            TableObject to = wiz.getCreatedTable();
            if (to != null)
                loadTable(to);
        } else {
            Utilities.displayError(this, "Can't do that!", "You cannot create a new table without creating or loading a project first!");
        }
    }

    public void newChart() {
        if (FBGFlow.getOpenProject() != null) {
            this.generateChart();
        } else {
            Utilities.displayError(this, "Can't do that!", "You cannot create a new chart without creating or loading a project first!");
        }
    }

    private void loadSelectedNode() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                this.projectTree.getLastSelectedPathComponent();
        if (node != null) {
            if (node.getUserObject() instanceof TableObject) {
                TableObject selectedTableObject = (TableObject) node.getUserObject();
                this.loadTable(selectedTableObject);
            }
            else if(node.getUserObject() instanceof TableFigure)
            {
                TableFigure selectedTableFigure = (TableFigure) node.getUserObject();
                selectedTableFigure.chartPreview();

            }
        }

    }

    private void mainTableViewMouseClicked(final java.awt.event.MouseEvent evt) {
        if (SwingUtilities.isLeftMouseButton(evt)) {
            syncTableContents();
        }
        if (evt.isMetaDown()) {
            int[][] selectedCells = this.getCurrentTableObject().getSelectedCells();
            if (selectedCells == null || selectedCells.length < 2) {
                try {
                    //Fire left click!
                    Robot r = new Robot();
                    r.mousePress(InputEvent.BUTTON1_MASK);
                    r.mouseRelease(InputEvent.BUTTON1_MASK);
                }
                catch (AWTException ex) {
                    Logger.getLogger(MainFactbookFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tablePopupMenu.show(getCurrentTableObject().table, evt.getX(), evt.getY());
                }
            });
        }
    }

    public void mainTableArrowKeyPressed() {
    	syncTableContents();
    }

    private void syncTableContents() {
        TableObject to = this.getCurrentTableObject();
        int sCell[][] = to.getSelectedCells();
        int r = sCell[0][0];
        int c = sCell[0][1];
        int lr = (int)to.getLastSelectedCell().getX();
        int lc = (int)to.getLastSelectedCell().getY();
        //Check to see if there was a previously selected cell
        if (to.getLastSelectedCell().getX() != -1) {
        	//If the cell is different from the last one
            if(!(r==lr && c==lc)) {
            	//put back in the table data
            	String toAddContents = to.getTableContents(lr, lc);
            	String newModelContents = (String) to.table.getModel().getValueAt(lr, lc);
            	if(newModelContents == null)
            		newModelContents = "";
            	if(toAddContents==null)
            		toAddContents = newModelContents;

            	//Yea sorry about this. its a simple way to sync everything up
            	//it does set stuff more than it needs though so its not the best
            	//or efficent code... yep not enough time to make it work nicer
            	if(!newModelContents.equals(toAddContents)) {
            		toAddContents = newModelContents;
            		if (to.getQueryBuilder(lr, lc) != null && (!(to.getQueryBuilder(lr, lc) instanceof NonEditableQueryBuilder))) {
                        int choice = JOptionPane.showConfirmDialog(this, "Current cell contains a query generated by the query builder, are you sure you want to continue?");
                        if(choice == JOptionPane.YES_OPTION) {
                        	to.addQueryBuilder(new NonEditableQueryBuilder(newModelContents, FBGFlow.getOpenProject().getTableEditorConstants()), lr, lc);
                        }
                        else {
                            to.addQueryBuilder(to.getQueryBuilder(lr, lc), lr, lc);
                            toAddContents = to.getTableContents(lr, lc);
                        }
                    }
            	}
            	if(toAddContents != null)
            		to.updateView(toAddContents, lr, lc);
            }
        }
        //Finish undo operations
        //TODO: This was in the table model listener... will move it there later--jeff
        //System.out.println("Pushing table state for " + to);
        //undoBuffer.pushTable(to);
        
        this.equationTextBox.setText(to.getTableContents(r, c));
        to.addCellContents(to.getTableContents(r, c), r, c);
        to.setLastSelectedCell(r, c);
    }

    private void updateEquation() {
    	if(this.tabbedList.size()>0) {
	    	TableObject to = this.getCurrentTableObject();
	    	int sCell[][] = to.getSelectedCells();
	    	if(sCell.length>0) {
	    		String toAddContents = to.getTableContents(sCell[0][0], sCell[0][1]);
	        	String newModelContents = this.equationTextBox.getText();
	        	if(newModelContents == null)
	        		newModelContents = "";
	        	if(toAddContents==null)
	        		toAddContents = newModelContents;
	        	if(!toAddContents.equals(newModelContents)) {
	        		if (to.getQueryBuilder(sCell[0][0], sCell[0][1]) != null && (!(to.getQueryBuilder(sCell[0][0], sCell[0][1]) instanceof NonEditableQueryBuilder))) {
	                    int choice = JOptionPane.showConfirmDialog(this, "Current cell contains a query generated by the query builder, are you sure you want to continue?");
	                    if(choice == JOptionPane.YES_OPTION) {
	                    	to.addQueryBuilder(new NonEditableQueryBuilder(newModelContents, FBGFlow.getOpenProject().getTableEditorConstants()), sCell[0][0], sCell[0][1]);
	                    }
	                    else {
	                        this.equationTextBox.setText(to.getTableContents(sCell[0][0], sCell[0][1]));
	                        return;
	                    }
	                }
	        		toAddContents = newModelContents;
	        	}

	        	if(toAddContents!=null) {
	        		to.addCellContents(toAddContents, sCell[0][0], sCell[0][1]);
	        	}
	    	}
	    	else {
	    		System.out.println("No Cell Selected!");
	    	}
    	}
    	else {
    		System.out.println("No Table Open!");
    	}
    }
    
    private void showConstantsWindow() {
    	if(FBGFlow.getOpenProject()!=null) {
	    	synchronized (FBGFlow.getOpenProject().getTableEditorConstants()) {
	    		if(!FBGFlow.getOpenProject().getTableEditorConstants().isShown()) {
	    			FBGFlow.getOpenProject().getTableEditorConstants().setShown(true);
	    			ConstantsFrame f = new ConstantsFrame(FBGFlow.getOpenProject().getTableEditorConstants(), this.tabbedList); 
	    		}
			}
    	}
    }
    
    private void prepost() {
    	TableObject to = this.getCurrentTableObject();
    	int sCell[][] = to.getSelectedCells();
    	if(sCell.length>0) {
    		PrePostFrame f = new PrePostFrame(sCell[0][0], sCell[0][1], to);
    	}
    }

    /*
     * End of menu action events
     *
     */
    /*
     * Start of gui generation method
     */
    private JMenuBar createMenu() {
        JMenuBar mainMenu = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem newProfile = new JMenuItem("New Profile");
        JMenuItem loadProfile = new JMenuItem("Load Profile");
        JMenuItem newFactbook = new JMenuItem("New Factbook");
        JMenuItem saveFactbook = new JMenuItem("Save Factbook");
        JMenuItem loadFactbook = new JMenuItem("Load Factbook");
        JMenuItem newTable = new JMenuItem("New Table");
        JMenuItem newChart = new JMenuItem("New Chart");
        JMenuItem exitProgram = new JMenuItem("Exit Program");
        file.add(newProfile);
        file.add(loadProfile);
        file.add(new JSeparator());
        file.add(newFactbook);
        file.add(saveFactbook);
        file.add(loadFactbook);
        file.add(new JSeparator());
        file.add(newTable);
        file.add(newChart);
        file.add(new JSeparator());
        file.add(exitProgram);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem copy = new JMenuItem("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        JMenuItem cut = new JMenuItem("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        JMenuItem paste = new JMenuItem("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem redo = new JMenuItem("Redo");
        editMenu.add(copy);
        editMenu.add(cut);
        editMenu.add(paste);
        //editMenu.add(undo);
        //editMenu.add(redo);

        JMenu server = new JMenu("Server");
        JMenuItem login = new JMenuItem("Login");
        JMenuItem uploadFiles = new JMenuItem("Upload File...");
        JMenuItem serverView = new JMenuItem("Server View");
        server.add(login);
        server.add(uploadFiles);
        server.add(serverView);

        JMenu factbook = new JMenu("Factbook");
        JMenuItem constantsWindow = new JMenuItem("Factbook Constants");
        JMenuItem compileFactbook = new JMenuItem("Compile Factbook");
        JMenu tables = new JMenu("Tables");
        JMenuItem compileTable = new JMenuItem("Compile Table");
        JMenuItem deleteTable = new JMenuItem("Delete Selected Table");
        tables.add(compileTable);
        tables.add(new JSeparator());
        tables.add(deleteTable);
        factbook.add(constantsWindow);
        factbook.add(compileFactbook);
        factbook.add(tables);
        
        constantsWindow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showConstantsWindow();
			}
        });

        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copyCommand();
            }
        });
        
        cut.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                cutCommand();
            }
        });

        paste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pasteCommand();
            }
        });


        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (undoBuffer.canUndo()) {
                    TableObject table = undoBuffer.undo();
                    JScrollPane tableScrollPane = createTabPane(table);
                    synchronized (this) {
                        String tabName = table.getName() + "      ";
                        System.out.println("tab name: " + tabName);
                        int index = mainTabbedPane.indexOfTab(tabName);
                        mainTabbedPane.removeTabAt(index);
                        //mainTabbedPane.add(tabName, tableScrollPane);

                        mainTabbedPane.add(tableScrollPane, index);
                        mainTabbedPane.setTitleAt(index, tabName);
                        //tabbedList.add(table);
                        mainTabbedPane.setSelectedIndex(index);
                    }
                }
                else
                    System.out.println("Can't undo!");
            }
        });

        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoBuffer.canRedo()) {
                    TableObject table = undoBuffer.redo();
                    JScrollPane tableScrollPane = createTabPane(table);
                    synchronized (this) {
                        String tabName = table.getName() + "      ";
                        System.out.println("tab name: " + tabName);
                        int index = mainTabbedPane.indexOfTab(tabName);
                        mainTabbedPane.removeTabAt(index);
                        //mainTabbedPane.add(tabName, tableScrollPane);

                        mainTabbedPane.add(tableScrollPane, index);
                        mainTabbedPane.setTitleAt(index, tabName);
                        //tabbedList.add(table);
                        mainTabbedPane.setSelectedIndex(index);
                    }
                }
                else
                    System.out.println("Can't redo!");
            }
        });

        newProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newProfile();
            }
        });
        newTable.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                newTable();
            }
        });
        newChart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                newChart();
            }
        });
        compileFactbook.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                compileFactbook();
            }
        });
        compileTable.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                compileTable();
            }
        });
        login.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                login();
            }
        });
        uploadFiles.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                uploadFiles();
            }
        });
        serverView.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                serverView();
            }
        });
        loadProfile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                loadProfile();
            }
        });
        newFactbook.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                newFactbook();
            }
        });
        saveFactbook.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                saveFactbook();
            }
        });
        loadFactbook.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                loadFactbook();
            }
        });
        exitProgram.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                exitProgram();
            }
        });
        deleteTable.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTable();
            }
        });
        mainMenu.add(file);
        mainMenu.add(editMenu);
        mainMenu.add(server);
        mainMenu.add(factbook);
        return mainMenu;
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenu add = new JMenu("Add...");
        JMenuItem addEquation = new JMenuItem("Add/Edit Equation");
        JMenuItem addQuery = new JMenuItem("Add/Edit Query");
        JMenuItem addChart = new JMenuItem("Create Chart");
        JMenu format = new JMenu("Format");
        JMenuItem changeBackground = new JMenuItem("Change Background Color");
        JMenuItem changeForeground = new JMenuItem("Change Foreground Color");
        JMenuItem changeFont = new JMenuItem("Change Font");
        JMenuItem changeBorder = new JMenuItem("Change Borders");
        JMenuItem autoWidth = new JMenuItem("Auto Width");
        JMenuItem prepost = new JMenuItem("Edit Pre/Post-fix");
        JMenuItem merge = new JMenuItem("Merge Cells");
        JMenuItem unMerge = new JMenuItem("UnMerge Cells");

        add.add(addEquation);
        add.add(addQuery);

        format.add(changeBackground);
        format.add(changeForeground);
        format.add(changeFont);
        format.add(changeBorder);
        format.add(merge);
        format.add(unMerge);
        format.add(autoWidth);
        format.add(prepost);

        popup.add(add);
        popup.add(format);
        popup.add(addChart);
        popup.add(new JSeparator());
        JMenuItem copy = new JMenuItem("Copy Selection");
        JMenuItem cut = new JMenuItem("Cut Selection");
        JMenuItem paste = new JMenuItem("Paste");
        popup.add(copy);
        popup.add(cut);
        popup.add(paste);
        
        merge.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
            	mergeEm();
            }

        });
        
        unMerge.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
            	unMergeEm();
            }

        });
        
        prepost.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
            	prepost();
            }

        });
        addChart.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                newChart();
            }

        });
        addEquation.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                addEquation();
            }
        });
        addQuery.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                addQuery();
            }
        });

        changeBackground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                changeBackground();
            }
        });

        changeForeground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                changeForeground();
            }
        });

        changeFont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                changeFont();
            }
        });

        changeBorder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                changeBorder();
            }
        });

        autoWidth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doAutoWidth();
            }
        });

        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyCommand();
            }
        });
        
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cutCommand();
            }
        });

        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pasteCommand();
            }
        });

        return popup;
    }

    private JPopupMenu createTreePopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem addFigure = new JMenuItem("Add Figure...");
        JMenuItem viewFigure = new JMenuItem("View Figure");
        JMenuItem deleteFigure = new JMenuItem("Remove Figure");
        JMenuItem editFigure = new JMenuItem("Edit Figure");
        JMenuItem updateChart = new JMenuItem("Update Chart");
        JMenuItem newTable = new JMenuItem("New Table...");
        JMenuItem deleteTable = new JMenuItem("Remove Table");
        JMenuItem footnote = new JMenuItem("Table Foonote...");
        
        
        popup.add(newTable);
        popup.add(deleteTable);
        popup.add(footnote);
        popup.add(new JSeparator());
        popup.add(addFigure);
        popup.add(viewFigure);
        popup.add(editFigure);
        popup.add(deleteFigure);
        popup.add(updateChart);

        updateChart.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                updateChart();
            }
            
        });
        //addFigure.setEnabled(false);//currently disabled
        editFigure.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
               editChart();
            }

        });
        deleteFigure.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                deleteChart();
            }
        });
        viewFigure.addActionListener(new ActionListener(){
             @Override
            public void actionPerformed(ActionEvent arg0) {
                viewChart();
            }
        });

        addFigure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                newChart();
            }
        });

        newTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                newTable();
            }
        });

        deleteTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTable();
            }
        });
        
        footnote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                footnoteCommand();
            }
        });


        return popup;
    }

    private JScrollPane createTreeView() {
        treeScrollPane = new JScrollPane();
        projectTree = new JTree();
        projectTree.setModel(null);
        projectTree.setPreferredSize(new java.awt.Dimension(150, 72));
        projectTree.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.isMetaDown()) {
                    try {
                        //Fire left click!
                        Robot r = new Robot();
                        r.mousePress(InputEvent.BUTTON1_MASK);
                        r.mouseRelease(InputEvent.BUTTON1_MASK);
                    }
                    catch (AWTException ex) {
                        Logger.getLogger(MainFactbookFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //Need invokeLater or else it won't actually select
                    //because the menu popping up blocks it or something.
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            treePopupMenu.show(projectTree, e.getX(), e.getY());
                        }
                    });

                }
                if (e.getClickCount() == 2)
                {
                   loadSelectedNode();
                }
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
            }
        });
        treeScrollPane.setMinimumSize(new java.awt.Dimension(100, 22));
        treeScrollPane.setViewportView(projectTree);
        return treeScrollPane;
    }

    public void updateProjectTree() {
        this.projectTree.setModel(FBGFlow.getOpenProject().generateTree());
        this.projectTree.expandPath(this.projectTree.getEditingPath());
        this.projectTree.repaint();
        this.treeScrollPane.validate();
        this.treeScrollPane.repaint();
    }

    private JPanel createEquationPanel() {
        JPanel equationPanel = new JPanel();
        JPanel eqWestPanel = new JPanel();
        JButton updateEq = new JButton("Update");
        JLabel eqLabel = new JLabel("Eq:");
        equationTextBox = new JTextField();

        equationPanel.setLayout(new java.awt.BorderLayout());
        equationPanel.setPreferredSize(new java.awt.Dimension(100, 20));

        eqWestPanel.setLayout(new java.awt.BorderLayout());

        updateEq.setFont(new java.awt.Font("Dialog", 0, 10));
        updateEq.setMaximumSize(new java.awt.Dimension(20, 20));
        updateEq.setMinimumSize(new java.awt.Dimension(20, 20));
        updateEq.setPreferredSize(new java.awt.Dimension(70, 20));

        eqWestPanel.add(updateEq, java.awt.BorderLayout.WEST);
        eqLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        eqWestPanel.add(eqLabel, java.awt.BorderLayout.EAST);


        equationTextBox.setBorder(BorderFactory.createLineBorder(Color.black));
        equationPanel.add(equationTextBox, java.awt.BorderLayout.CENTER);

        equationPanel.add(eqWestPanel, java.awt.BorderLayout.WEST);

        updateEq.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateEquation();
			}
        });
        equationTextBox.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					updateEquation();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

        });

        return equationPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        ConsolePanel consolePanel = new ConsolePanel();
        consolePanel.setVisible(true);
        JPanel consolePanelPanel = new JPanel();
        consolePanelPanel.setLayout(new BorderLayout());
        consolePanelPanel.setPreferredSize(new Dimension(500, 100));
        consolePanelPanel.add(consolePanel, BorderLayout.CENTER);
        mainTabbedPane = new CloseTabbedPane(tabbedList);
        mainTabbedPane.setPreferredSize(new Dimension(500, 200));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(mainTabbedPane, java.awt.BorderLayout.CENTER);
        mainPanel.add(consolePanelPanel, java.awt.BorderLayout.SOUTH);
        return mainPanel;
    }
    /*
     *
     * End of gui generation methods
     *
     */

    private TableObject getCurrentTableObject() {
        return tabbedList.get(mainTabbedPane.getSelectedIndex());
    }

    private JScrollPane createTabPane(TableObject table) {

        //Else we open a new table
        final CustomJTable mainTableView = table.table;

        //VERY IMPORTANT PROPERTY TO ALLOW SERIALIZATION
        mainTableView.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        //This is for right clicking and the popup menu
        mainTableView.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainTableViewMouseClicked(evt);
            }
        });

        //Handles copy and paste key bindings
        ActionListener copyListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyCommand();
            }
        };
        
        ActionListener cutListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cutCommand();
            }
        };

        ActionListener pasteListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pasteCommand();
            }
        };

        KeyStroke copyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
        KeyStroke cutStroke = KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK, false);
        KeyStroke pasteStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
        mainTableView.registerKeyboardAction(copyListener, "Copy", copyStroke, JComponent.WHEN_FOCUSED);
        mainTableView.registerKeyboardAction(cutListener, "Cut", cutStroke, JComponent.WHEN_FOCUSED);
        mainTableView.registerKeyboardAction(pasteListener, "Paste", pasteStroke, JComponent.WHEN_FOCUSED);
        //End copy and paste key bindings

        mainTableView.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    //Delete all selected cells.
                    //Necessary because otherwise we only delete 1 cell.
                    deleteSelectedCells();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_LEFT||e.getKeyCode()==KeyEvent.VK_RIGHT||
                                    e.getKeyCode()==KeyEvent.VK_UP||e.getKeyCode()==KeyEvent.VK_DOWN) {
                            mainTableArrowKeyPressed();
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB)
                    syncTableContents();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                    // TODO Auto-generated method stub

            }
        });      

        final int rowCount = mainTableView.getRowCount();
        ListModel rowHeaderModel = new AbstractListModel() {

            public int getSize() {
                return rowCount;
            }

            public Object getElementAt(int index) {
                return index + 1;
            }
        };

        final JScrollPane tableScrollPane = new JScrollPane();
        final JList rowHeader = new JList(rowHeaderModel);

        //This allows us to adjust the row sizes!
        MouseAdapter rowHeaderListener = new MouseAdapter() {
            private int startY;

            @Override
            public void mousePressed(MouseEvent e) {
                mainTableView.setExplicitSelectedRow(rowHeader.getSelectedIndex());
                startY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int index = mainTableView.getExplicitSelectedRow();
                int origHeight = mainTableView.getRowHeight(index);
                int yChange = e.getY() - startY;
                int height = origHeight + yChange;
                if (height < TableObject.MIN_ROW_HEIGHT) height = TableObject.MIN_ROW_HEIGHT;

                startY = e.getY();

                //This modifies the row height of the table itself
                mainTableView.setRowHeight(index, height);

                //This modifies the row header height...
                //TODO: Figure out better way to repaint the row header height.
                //Currently it just recreates based off table height. Massive failure.
                //RowHeaderRenderer r = (RowHeaderRenderer)rowHeader.getCellRenderer();
                //r.setCellHeight(index, height);
                rowHeader.setCellRenderer((new RowHeaderRenderer(mainTableView)));
                rowHeader.revalidate();
                rowHeader.repaint();
                //tableScrollPane.setRowHeaderView(rowHeader);
                //tableScrollPane.revalidate();
                //tableScrollPane.repaint();
            }
        };

        rowHeader.addMouseListener(rowHeaderListener);
        rowHeader.addMouseMotionListener(rowHeaderListener);

        rowHeader.setFixedCellWidth(50);
        //rowHeader.setFixedCellHeight(mainTableView.getRowHeight());
        rowHeader.setFixedCellHeight(-1);
        rowHeader.setCellRenderer(new RowHeaderRenderer(mainTableView));
        rowHeader.setBackground(tableScrollPane.getBackground());
        tableScrollPane.setRowHeaderView(rowHeader);
        tableScrollPane.setViewportView(mainTableView);

        return tableScrollPane;
    }

    @SuppressWarnings({"serial", "unused"})
    private void loadTable(TableObject table) {
        //if the table is open, only switch to it; don't open a new table.
        int index = 0;
        for (; index < mainTabbedPane.getTabCount(); index++) {
            if (tabbedList.get(index) == table) {
                break;
            }
        }
        //If it's less than, that means we found the tab
        if (index < mainTabbedPane.getTabCount()) {
            mainTabbedPane.setSelectedIndex(index);
            return;
        }

        JScrollPane tableScrollPane = createTabPane(table);

        synchronized (this) {
            String tabName = table.getName() + "      ";
            mainTabbedPane.add(tabName, tableScrollPane);
            tabbedList.add(table);
            table.refreshTableModel();
            mainTabbedPane.setSelectedIndex(mainTabbedPane.indexOfTab(tabName));
        }
    }

    private void deleteTable() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.projectTree.getLastSelectedPathComponent();
        if (node.getUserObject() instanceof TableObject) {
            TableObject selectedTableObject = (TableObject) node.getUserObject();
            String text = "Really delete table '" + selectedTableObject.getName() + "'?";

            if(JOptionPane.showConfirmDialog(null, text, "Confirm Delete", JOptionPane.OK_CANCEL_OPTION) == 0) {
                synchronized (this) {
                    if (node.getUserObject() instanceof TableObject) {
                        FBGFlow.getOpenProject().removeTable(selectedTableObject);
                        this.updateProjectTree();
                    }
                }
            }
        }
        else {
            Utilities.displayError(this, "Can't delete", "This is not a table!");
        }
    }
    
    private void footnoteCommand() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.projectTree.getLastSelectedPathComponent();
        if (node.getUserObject() instanceof TableObject) {
            TableObject selectedTableObject = (TableObject) node.getUserObject();
            FootnoteDialog dialog = new FootnoteDialog(this, selectedTableObject);
            selectedTableObject.setFootnote(dialog.getNote());
        }
    }
    
    public void deleteSelectedCells() {
        TableObject to = this.getCurrentTableObject();
        int[][] cells = to.getSelectedCells();
        for (int c = 0; c < cells.length; c++) {
            int row = cells[c][0];
            int col = cells [c][1];
            to.updateView("", row, col);
        }
    }

    /**
     * Copies stufff to the system clipboard.
     */
    public void copyCommand() {
        //This gets the string selection
        String cells = this.getCurrentTableObject().getSelectedCopyableCells();
        
        //This gets all the query builders we need to care about.
        QueryBuilderTuple[] queries = this.getCurrentTableObject().getSelectedQueryBuilders();
        
        if (cells != null) {
            System.out.println(cells);
            Clipboard clippy = Toolkit.getDefaultToolkit().getSystemClipboard();
            TableSelection selection = new TableSelection(cells, queries);
            //StringSelection selection = new StringSelection(cells);
            clippy.setContents(selection, null);
        }
        else {
            System.out.println("You aint got nothin to copy, busta.");
        }
    }
    
    /**
     * Copies stuff and then clears the selection.
     */
    public void cutCommand() {
        copyCommand();
        
        TableObject to = this.getCurrentTableObject();
        int[][] cells = to.getSelectedCells();
        for (int c = 0; c < cells.length; c++) {
            int row = cells[c][0];
            int col = cells[c][1];
            to.addCellContents("", row, col);
        }
    }

    /**
     * Pastes stuff from the system clipboard.
     */
    public void pasteCommand() {
        Clipboard clippy = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clippy.getContents(null);
        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)
                && contents.isDataFlavorSupported(TableSelection.queryFlavor)) {
            
            try {
                //This string is formatted with \ts between table columns and
                // \ns between table rows.
                String text = (String)contents.getTransferData(DataFlavor.stringFlavor);

                //Get the upper-left selected cell to begin
                //our paste from
                int[][] cells = MainFactbookFrame.this.getCurrentTableObject().getSelectedCells();
                int row = cells[0][0];
                int col = cells[0][1];

                //Now do the actual paste for the string contents.
                this.getCurrentTableObject().pasteTableContents(row, col, text);
                
                //Now we paste queries.
                this.getCurrentTableObject().pasteQueryBuilders(row, col, (QueryBuilderTuple[])contents.getTransferData(TableSelection.queryFlavor));
            }
            catch (UnsupportedFlavorException ex) {
                Utilities.displayError(MainFactbookFrame.this, "Paste Error", "You're trying to paste something\nthe program cannot understand.");
            }
            catch (IOException ex) {
                Utilities.displayError(MainFactbookFrame.this, "I/O Paste Error", "There was an I/O exception trying to paste.");
                ex.printStackTrace();
            }
        }
    }

    /**
     * delete current figure in tree
     */
    private void deleteChart()
    {
        DefaultMutableTreeNode node =
                 (DefaultMutableTreeNode) this.projectTree.getLastSelectedPathComponent();
        if (node.getUserObject() instanceof TableFigure)
        {
            if(JOptionPane.showConfirmDialog(null,
                "Delete this figure?", "Confirm Delete", JOptionPane.OK_CANCEL_OPTION) == 0)
            {
                synchronized (this)
                {

                    TableFigure selectedTableFigure = (TableFigure) node.getUserObject();
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();

                    if (parent.getUserObject() instanceof TableObject)
                    {
                        TableObject t = (TableObject) parent.getUserObject();
                        t.removeFigure(selectedTableFigure);
                        this.updateProjectTree();
                    }
                }
            }
        }
     }
    /**
     * preview chart in a frame
     */
    private void viewChart()
    {
        //to be implemented to view chart
         DefaultMutableTreeNode node =
                 (DefaultMutableTreeNode) this.projectTree.getLastSelectedPathComponent();
                if (node.getUserObject() instanceof TableFigure) {
                    TableFigure selectedTableFigure = (TableFigure) node.getUserObject();
                    selectedTableFigure.chartPreview();
                    //this.updateProjectTree();
                }
    }
    /**
     * Edit chart
     */
    private void editChart()
    {
        //edit chart
        //to be implemented to view chart
         DefaultMutableTreeNode node =
                 (DefaultMutableTreeNode) this.projectTree.getLastSelectedPathComponent();
        if (node.getUserObject() instanceof TableFigure)
        {
            final TableFigure selectedTableFigure = (TableFigure) node.getUserObject();

            DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();

            if (parent.getUserObject() instanceof TableObject)
            {
                final TableObject t = (TableObject) parent.getUserObject();
                java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ChartGenerationFrame(selectedTableFigure, t,
                            MainFactbookFrame.this).setVisible(true);
                        }
                    });
            }
        }
    }
    
    /**
     * update chart when necessary
     * new chart will use newly updated data from table
     */
    private void updateChart()
    {
        ITableObject tobj = this.getCurrentTableObject();
        for(TableFigure tf : tobj.getFigures())
        {
            tf.updateChart(tobj);
        }
    }


     /**
     * open chart creation frame
     */
    private void generateChart()
    {
        
        TableObject tobj = null;
        try
        {
            tobj = this.getCurrentTableObject();
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this,
                    "Please select a source table!");
            System.out.println("Table is not selected! ");
            return;
        }
        if(tobj.table.getSelectedRowCount() <= 0)
        {
            JOptionPane.showMessageDialog(this,
                    "Please select data source from table first!");
            return;
        }
        final CustomDatasetTable t =
                CustomDatasetTable.generateDataSource(tobj);
        if(t == null)
        {
            JOptionPane.showMessageDialog(this,"Error generating dataset for chart");
            return;
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ChartGenerationFrame(t, getCurrentTableObject(),
                        MainFactbookFrame.this).setVisible(true);
            }
        });
    }

    private void updateTitle(String title) {
        super.setTitle(FRAME_TITLE + title);
    }

    public static void main(String args[]) {
        TableObject to = new TableObject("table1", 10, 10);
        to.table.selectAll();
        to.getSelectedCells();
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFactbookFrame().setVisible(true);
            }
        });
    }
}

class RowHeaderRenderer extends JLabel implements ListCellRenderer {
    private HashMap<Integer, Integer> rowHeights;

    RowHeaderRenderer(JTable table) {
        rowHeights = new HashMap<Integer, Integer>();
        JTableHeader header = table.getTableHeader();
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(CENTER);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());

        //Set all cell heights based off of table row heights
        for (int c = 0; c < table.getRowCount(); c++) {
            setCellHeight(c, table.getRowHeight(c));
        }
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        this.setPreferredSize(new Dimension(50, getCellHeight(index)));
        setText((value == null) ? "" : value.toString());
        return this;
    }

    public void setCellHeight(int row, int height) {
        rowHeights.put(row, height);
    }

    public int getCellHeight(int row) {
        return rowHeights.get(row);
    }
}
