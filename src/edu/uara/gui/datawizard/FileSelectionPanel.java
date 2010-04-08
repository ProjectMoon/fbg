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
package edu.uara.gui.datawizard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.uara.parser.integrated.ParseableFile;
import java.awt.Font;

/**
 * This class represents the files to select for a given year.
 * @author jeff
 */
public class FileSelectionPanel extends JPanel implements ActionListener {
    //Year related information
    private ArrayList<Integer> yearList = new ArrayList<Integer>();
    private int[] years;
    private int yearPos;
    private int currentYear;
    
    private ArrayList<ParseableFile> fileList = new ArrayList<ParseableFile>();
    
    //Button constants: These are used by copyButton to initially
    //create buttons
    private static final JButton AIS_ADD = new JButton("Add AIS");
    private static final JButton DIS_ADD = new JButton("Add DIS");
    private static final JButton EDS_ADD = new JButton("Add EDS");
    private static final JButton EIS_ADD = new JButton("Add EIS");
    private static final JButton POP_ADD = new JButton("Add POP");
    
    //The "current" buttons. loadButtons will set these variables, so
    //actionPerformed is always acting on the right set of buttons
    private JButton aisButton;
    private JButton disButton;
    private JButton edsButton;
    private JButton eisButton;
    private JButton popButton;
    
    //The map of buttons for all years. This allows us to persist button states
    //when going backwards and forwards in the wizard.
    private HashMap<Integer, JButton[]> buttons = new HashMap<Integer, JButton[]>();
    
    //Label to display the year
    private JLabel yearLabel = new JLabel();
    
    //File chooser for choosing files!
    private JFileChooser fileChooser = new JFileChooser();
    
    //Displayed list of files.
    private JList displayedFiles;
    
    //Map of list models. This allows us to persist displayed file lists when
    //going backwards and forwards in the wizard.
    private HashMap<Integer, DefaultListModel> listModels = new HashMap<Integer, DefaultListModel>();
    
    //A panel to display the buttons and label on.
    private JPanel controlPanel = new JPanel();

    public FileSelectionPanel() {
        setupUI();
    }
    
    public FileSelectionPanel(int[] yearSet) {
        setYearSet(yearSet);
        setupUI();
    }
    
    private void setupUI() {
        super.setLayout(new BorderLayout());
        
        controlPanel.setLayout(new BorderLayout());
               
        displayedFiles = new JList();
        DefaultListModel dfm = new DefaultListModel();
        displayedFiles.setModel(dfm);
        super.add(new JScrollPane(displayedFiles), BorderLayout.CENTER);
        super.add(controlPanel, BorderLayout.NORTH); 
    }
    
    private void createButtons(int year) {
        JButton[] buttonArr = new JButton[5];
        buttonArr[0] = copyButton(AIS_ADD);
        buttonArr[1] = copyButton(DIS_ADD);
        buttonArr[2] = copyButton(EDS_ADD);
        buttonArr[3] = copyButton(EIS_ADD);
        buttonArr[4] = copyButton(POP_ADD);
        
        //Create action listeners
        buttonArr[0].addActionListener(this);
        buttonArr[1].addActionListener(this);
        buttonArr[2].addActionListener(this);
        buttonArr[3].addActionListener(this);
        buttonArr[4].addActionListener(this);
        
        buttons.put(year, buttonArr);
    }
    
    private void loadControlPanel(int year) {
        JButton[] buttonArr = buttons.get(year);
        aisButton = buttonArr[0];
        disButton = buttonArr[1];
        edsButton = buttonArr[2];
        eisButton = buttonArr[3];
        popButton = buttonArr[4];
        
        controlPanel.removeAll();
        JPanel buttonPanel = new JPanel();
        for (JButton b : buttonArr)
            buttonPanel.add(b);
        
        yearLabel.setText("Select files for <" + new Integer(currentYear).toString() + ">");
        Font curFont = yearLabel.getFont();
        yearLabel.setFont(new Font(curFont.getFontName(), curFont.getStyle(), 18));


        controlPanel.add(yearLabel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);
        controlPanel.validate();
        controlPanel.repaint();
    }
    
    private void saveButtons(int year) {
        JButton[] buttonArr = new JButton[5];
        buttonArr[0] = aisButton;
        buttonArr[1] = disButton;
        buttonArr[2] = edsButton;
        buttonArr[3] = eisButton;
        buttonArr[4] = popButton;
        
        buttons.put(year, buttonArr);
    }
    
    private JButton copyButton(JButton b) {
        
        JButton retButton = new JButton();
        retButton.setText(b.getText());
        return retButton;
    }
    
    public void setYearSet(int[] yearSet) {
        System.out.println("Setting year");
        yearList = new ArrayList<Integer>(yearSet.length);
        
        for (int year : yearSet) {
            yearList.add(year);
        }

        yearPos = 0;
        currentYear = yearSet[0];
      
        
        //Create the list models and buttons--this allows us to preserve data.
        for (int i : yearSet) {
            createButtons(i);
            DefaultListModel dfm = new DefaultListModel();
            listModels.put(i, dfm);
        }
                
        //Now set the current list model to the proper one.
        loadControlPanel(currentYear);
        displayedFiles.setModel(listModels.get(currentYear));
    }
    
    public void addYear(int year) {
        if (yearList.contains(new Integer(year)) == false) {
            yearList.add(year);
            createButtons(year);
            listModels.put(year, new DefaultListModel());
        }
    }
    
    public boolean removeYear(int year) {
        return yearList.remove(new Integer(year));
    }
    
    public void trimToYears(int[] years) {
        for (Integer oYear : (Integer[])yearList.toArray(new Integer[0])) {
            int year = oYear.intValue();
            boolean inList = false;
            for (int c = 0; c < years.length; c++) {
                if (year == years[c]) {
                    inList = true;
                    break;
                }
            }
            
            if (!inList) {
                System.out.println("Removing " + oYear);
                yearList.remove(oYear);
                listModels.remove(oYear);
                buttons.remove(oYear);
            }
        }
        
        resetYears();
    }
    
    private void resetYears() {
        yearPos = 0;
        currentYear = yearList.get(yearPos);
        loadControlPanel(currentYear);
        displayedFiles.setModel(listModels.get(currentYear));
    }
        
    public void advanceYear() {
        if (hasMoreYears()) {
            yearPos++;
            //currentYear = years[yearPos];
            currentYear = yearList.get(yearPos);
            loadControlPanel(currentYear);
            displayedFiles.setModel(listModels.get(currentYear));
            displayedFiles.repaint();
            controlPanel.repaint();
            this.repaint();
        }
        else
            throw new ArrayIndexOutOfBoundsException("No more years to select files for!");
    }
    
    public void recedeYear() {
        if (yearPos > 0) {
            yearPos--;
            //currentYear = years[yearPos];
            currentYear = yearList.get(yearPos);
            loadControlPanel(currentYear);
            displayedFiles.setModel(listModels.get(currentYear));
            displayedFiles.repaint();
            controlPanel.repaint();
            this.repaint();            
        }
        else
            throw new ArrayIndexOutOfBoundsException("No more years to select files for!");
    }
    
    public boolean hasYears() {
        return (yearList.size() != 0);
    }
    
    public boolean hasMoreYears() {
        if (yearList == null || yearList.size() == 0) return false;
        else return (yearPos + 1 < yearList.size());
    }
    
    public int getYearNumber() {
        return yearList.size();
    }
    
    public boolean canRecede() {
        if (yearList == null || yearList.size() == 0) return false;
        else return (yearPos > 0);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == aisButton) {
            if (aisButton.getText().equals("Add AIS")) {
                if (chooseFile("AIS"))
                    aisButton.setText("Remove AIS");
            }
            else {
                removeFile("AIS");
                aisButton.setText("Add AIS");
            }
        }
        
        else if (e.getSource() == disButton) {
            if (disButton.getText().equals("Add DIS")) {
                if (chooseFile("DIS"))
                    disButton.setText("Remove DIS");
            }
            else {
                removeFile("DIS");
                disButton.setText("Add DIS");
            }
        }
        
        else if (e.getSource() == edsButton) {
            if (edsButton.getText().equals("Add EDS")) {
                chooseFile("EDS");
                edsButton.setText("Remove EDS");
            }
            else {
                removeFile("EDS");
                edsButton.setText("Add EDS");
            }
        }
        
        else if (e.getSource() == eisButton) {
            if (eisButton.getText().equals("Add EIS")) {
                if (chooseFile("EIS"))
                    eisButton.setText("Remove EIS");
            }
            else {
                removeFile("EIS");
                eisButton.setText("Add EIS");
            }
        }   
        
        else if (e.getSource() == popButton) {
            if (popButton.getText().equals("Add POP")) {
                if (chooseFile("POP"))
                    popButton.setText("Remove POP");
            }
            else {
                removeFile("POP");
                popButton.setText("Add POP");
            }
        }
        
        
        //Save the button state for this year.
        saveButtons(currentYear);
    }
    
    private void removeFile(String type) {
        DefaultListModel listModel = listModels.get(currentYear);
        ParseableFile fileToRemove = null;
        
        for (int c = 0; c < fileList.size(); c++) {
            ParseableFile f = fileList.get(c);
            if (f.getType().equals(type)) {
                fileToRemove = f;
                break;
            }
        }
        
        listModel.removeElement(fileToRemove);
        fileList.remove(fileToRemove);
        displayedFiles.setModel(listModel);
        listModels.put(currentYear, listModel);
        
    }
    
    private boolean chooseFile(String type) {
        int choice = fileChooser.showOpenDialog(this);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            ParseableFile fileToAdd = new ParseableFile(type, f, currentYear);
            
            DefaultListModel listModel = listModels.get(currentYear);
            listModel.addElement(fileToAdd);
            fileList.add(fileToAdd);

            listModels.put(currentYear, listModel);
            displayedFiles.setModel(listModel);
            displayedFiles.validate();
            displayedFiles.repaint();
            return true;
        }
        else {
            return false;
        }
    }
    
    public ParseableFile[] getFileList() {
        for (int c = 0; c < fileList.size(); c++) 
            System.out.println ("Hurr hurr im a file: " + fileList.get(c));
        
        if (fileList != null && fileList.size() > 0)
            return fileList.toArray(new ParseableFile[0]);
        else
            return null;
    }
}
