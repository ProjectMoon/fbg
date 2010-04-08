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

import edu.uara.gui.ServerViewPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.uara.parser.integrated.ParseableFile;

/**
 * This class represents the "wizard" part of the new file uploader. It has the 
 * following distinct steps: greeting screen, year selection, and then file selection
 * for each year. The wizard ends after files for all selected years have been
 * selected. The wizard then returns a list of ParseableFile objects and forwards
 * it to the parser and uploader.
 * @author jeff
 */
public class UploadWizard extends JDialog implements ActionListener {
    //The server view to update
    private ServerViewPanel signalPanel;

    //Steps to the wizard
    private GreetingPanel greetingStep;
    private YearSelectionPanel yearSelectionStep;
    private FileSelectionPanel fileSelectionStep;
    
    //Final result of parseable files
    private ArrayList<String> filesToParse;
    
    //Wizard control buttons
    private JButton backButton;
    private JButton nextButton;
    private JButton cancelButton;
    
    //Wizard layout panels
    private JPanel currentStep;
    private JPanel controlPanel; //where the buttons go

    
    public UploadWizard(JFrame parentFrame, ServerViewPanel signalPanel) {
        super(parentFrame, true);
        super.setSize(600, 400);

        super.setLocationRelativeTo(parentFrame);
        this.signalPanel = signalPanel;
        
        filesToParse = new ArrayList<String>();
        greetingStep = new GreetingPanel();
        yearSelectionStep = new YearSelectionPanel();
        fileSelectionStep = new FileSelectionPanel();
        
        backButton = new JButton("<< Back");
        backButton.addActionListener(this);
        backButton.setEnabled(false);
        nextButton = new JButton("Next >>");
        nextButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        
        //Set the current step to the greeting
        currentStep = greetingStep;
        
        //Set up control panel
        controlPanel = new JPanel();
        controlPanel.add(backButton);
        controlPanel.add(nextButton);
        controlPanel.add(cancelButton);
        
        //Set up UI
        super.setLayout(new BorderLayout());
        super.add(currentStep, BorderLayout.CENTER);
        super.add(controlPanel, BorderLayout.SOUTH);
        super.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            nextStep();
            if (currentStep != greetingStep)
                backButton.setEnabled(true);
        }
        else if (e.getSource() == backButton) {
            previousStep();
            if (currentStep == greetingStep)
                backButton.setEnabled(false);
        }
        else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }

    /**
     * This method controls moving forward in the wizard based on a series of
     * if statements.
     */
    private void nextStep() {
        //Move from greeting to year selection
        if (currentStep == greetingStep)
            switchToPanel(yearSelectionStep);
        
        //Move from year selection to file selection:
        //This has two cases assuming years are selected:
        //1. If the file selection step was already visited (i.e. has years), we
        //      add new years to the step and remove old ones.
        //2. If we're visiting it for the first time, we just dump all the years in.
        else if (currentStep == yearSelectionStep) {
            int[] years = yearSelectionStep.getSelectedYears();
            if (years != null) {
                if (fileSelectionStep.hasYears()) {
                    for (int year : years)
                        fileSelectionStep.addYear(year);
                    
                    System.out.println("Trimming years...");
                    fileSelectionStep.trimToYears(years);
                    
                }
                else
                    fileSelectionStep.setYearSet(years);
                
                if (fileSelectionStep.getYearNumber() == 1)
                    nextButton.setText("FINISH");
                
                switchToPanel(fileSelectionStep);
            }
        }
        //Move through file selection steps and eventually to finish.
        //If there are more years to handle, the fileSelectionStep updates
        //itself with the new year.
        //If there are no more years, we move on to wizardDone().
        else if (currentStep == fileSelectionStep) {
            if (fileSelectionStep.hasMoreYears()) {
                fileSelectionStep.advanceYear(); 
                if (fileSelectionStep.hasMoreYears() == false)
                    nextButton.setText("FINISH");
            }
            else
                wizardDone();
        }
            
    }

    /**
     * Controls going backward through the wizard via a series of if statements.
     */
    private void previousStep() {
        //If we're at the file selection step, we move backwards through available
        //years until we run out--in which case we move back to the year selection
        //step.
        if (currentStep == fileSelectionStep) {
            if (fileSelectionStep.canRecede())
                fileSelectionStep.recedeYear();
            else
                switchToPanel(yearSelectionStep);
            
            nextButton.setText("Next >>");
        }
        //If we're on year selection, we just bounce back to greeting. Can't go any
        //further.
        else if (currentStep == yearSelectionStep)
            switchToPanel(greetingStep);
    }
    
    /**
     * Finishes the wizard by forwarding selected files on to the uploader.
     * From here, control is passed to a ParserThread.
     */
    private void wizardDone() {
        System.out.println("Wizard done.");
        ParseableFile[] files = fileSelectionStep.getFileList();
        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(this, "You need to select some files!", "Wizard Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        this.dispose();
        
        ParserThread npt = new ParserThread(signalPanel);
        
        npt.setTitle("Parsing Selected Data Files");
        for (ParseableFile f : files)
            npt.addFile(f);
        
        new Thread(npt).start();
    }
    
    private void switchToPanel(JPanel panel) {
        super.remove(currentStep);
        currentStep = panel;
        super.add(panel, BorderLayout.CENTER);
        super.validate();
        super.repaint();
    }
}
