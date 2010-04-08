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
package edu.uara.gui;

import edu.uara.FBGConstants;
import edu.uara.FBGFlow;
import edu.uara.project.FactbookProject;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class allows the user to enter information about a new factbook project
 * they wish to create.
 * @author jh44695
 */
public class NewProjectDialog extends JDialog {
    private JTextField projectName;
    private JTextArea description;
    private boolean created = false;

    public NewProjectDialog(JFrame parent) {
        super(parent, true);

        super.setSize(300, 250);
        super.setLayout(new BorderLayout());

        //Project name panel--at top of window
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        projectName = new JTextField();
        projectName.setColumns(20);
        namePanel.add(new JLabel("Project Name: "));
        namePanel.add(projectName, BorderLayout.NORTH);

        //Description panel--in middle
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BorderLayout());
        description = new JTextArea();
        description.setRows(8);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        JScrollPane descriptionScroller = new JScrollPane(description);
        descriptionPanel.add(new JLabel("Description: "), BorderLayout.NORTH);
        descriptionPanel.add(descriptionScroller, BorderLayout.CENTER);
        descriptionPanel.add(new JPanel(), BorderLayout.WEST);
        descriptionPanel.add(new JPanel(), BorderLayout.EAST);
        descriptionPanel.add(new JPanel(), BorderLayout.SOUTH);

        //Buttons--at bottom
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Create Project");

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewFactbook();
            }
        });

        buttonPanel.add(saveButton);

        super.add(namePanel, BorderLayout.NORTH);
        super.add(descriptionPanel, BorderLayout.CENTER);
        super.add(buttonPanel, BorderLayout.SOUTH);

        super.pack();
        super.setVisible(true);
    }
    
    private void createNewFactbook() {
        //error checking
        if (projectName.getText().length() == 0 || description.getText().length() == 0) {
            Utilities.displayError(null, "Error", "Please enter both a name and description.");
            return;
        }

        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(FBGConstants.USER_FILE_PATH));
        jfc.setFileFilter(new ProjectFileFilter());

        int choice = jfc.showSaveDialog(NewProjectDialog.this);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();

            //Overwrite confirmation.
            if (f.exists()) {
                int response = JOptionPane.showConfirmDialog (null, "This project already exists. Would you like to overwrite it?","Confirm Project Overwrite",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) return;
            }

            //We can now proceed with saving.
            FactbookProject p = new FactbookProject();
            p.setName(projectName.getText());
            p.setDescription(description.getText());

            String filename = f.getAbsolutePath();
            if (!filename.endsWith(".fbp"))
                filename += ".fbp";

            p.save(filename);
            FBGFlow.setOpenProject(p);
            NewProjectDialog.this.dispose();
            created = true;
        }
    }

    public boolean isCreated() {
        return created;
    }
    
    public static void main(String[] args) {
        new NewProjectDialog(null);
    }
}
