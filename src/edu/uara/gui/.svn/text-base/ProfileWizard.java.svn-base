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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.uara.FBGConstants;
import edu.uara.FBGFlow;
import edu.uara.db.profiles.DatabaseProfile;
import edu.uara.db.profiles.ProfileCreator;
import edu.uara.db.profiles.ProfileManager;
import edu.uara.db.profiles.ProfileTemplate;

/**
 * This class handles profile creation when the GUI is running. It is simple;
 * and has textboxes with default values for the user to enter information in.
 * @author jeff
 */
public class ProfileWizard extends JFrame implements ActionListener {
    //Set up the required info variables and their default values.
    //These will change in production.
    private JTextField profileName = new JTextField("sdfs");
    private JTextField driver = new JTextField("sdfs");
    private JTextField jdbcURL = new JTextField();
    private JTextField dbInstance = new JTextField();
    private JTextField extraSettings = new JTextField();
    private JTextField username = new JTextField();
    private JPasswordField password = new JPasswordField();
    
    private static final int TEXT_BOX_WIDTH = 15;
    
    private JComboBox templateMenu;
    private ProfileTemplate selectedTemplate;
    
    private JButton okButton = new JButton("Ok");

    /**
     * Creates the graphical elements of this frame.
     *
     */
    public ProfileWizard() {
        super.setTitle("Create New Database Profile");
        //super.setSize(400, 300);
       // super.setResizable(false);
        super.setLayout(new BoxLayout(super.getContentPane(), BoxLayout.Y_AXIS));
        if (FBGFlow.getState().equals("loading"))
            super.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
        else
            super.setDefaultCloseOperation(super.DISPOSE_ON_CLOSE);
        
        //Set up the text box sizes
        profileName.setColumns(TEXT_BOX_WIDTH);
        driver.setColumns(TEXT_BOX_WIDTH);
        jdbcURL.setColumns(TEXT_BOX_WIDTH);
        dbInstance.setColumns(TEXT_BOX_WIDTH);
        extraSettings.setColumns(TEXT_BOX_WIDTH);
        username.setColumns(TEXT_BOX_WIDTH);
        password.setColumns(TEXT_BOX_WIDTH);
        templateMenu = new JComboBox();
        templateMenu.addActionListener(this);
        
        setupTemplates();
        super.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));
        
        panel.add(new JLabel("Template Menu:"));
        panel.add(templateMenu);
        
        panel.add(new JLabel("Name:"));
        panel.add(profileName);
        
        panel.add(new JLabel("Driver:"));
        panel.add(driver);
        
        panel.add(new JLabel("Database URL:"));
        panel.add(jdbcURL);
        
        panel.add(new JLabel("Instance:"));
        panel.add(dbInstance);
        
        panel.add(new JLabel("Extra Settings:"));
        panel.add(extraSettings);
        
        panel.add(new JLabel("User:"));
        panel.add(username);
        
        panel.add(new JLabel("Password:"));
        panel.add(password);
        

        //Create profile via both hitting OK button and pressing enter at PW
        //field
        password.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    createProfile();
                }
            }
        });
        okButton.addActionListener(this);
        
        //Add the panel and the OK button for a nice clean(ish) interface.
        super.add(panel, BorderLayout.CENTER);
        super.add(okButton, BorderLayout.SOUTH);
        super.pack();
        super.setVisible(true);
    }

    /**
     * Responds to the OK button being pressed, and then saves the profile.
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(templateMenu)) {
            selectedTemplate = (ProfileTemplate)templateMenu.getSelectedItem();
            profileName.setText(selectedTemplate.getDatabaseName());
            driver.setText(selectedTemplate.getDriverString());
            extraSettings.setText(selectedTemplate.getURLSuffix());
        }

        if (e.getSource().equals(okButton)) {
            createProfile();
        }

    }

    /**
     * Centralized helper method to actually create the profile from info
     * gathered by the wizard.
     * @throws edu.uara.exceptions.ProfileGenerationException
     */
    private void createProfile() {
        System.out.println("Creating a new profile...");
        
        //Create the full connection string
        String connectionString = selectedTemplate.getConnectionURLPrefix() + jdbcURL.getText() + "/" + dbInstance.getText() + extraSettings.getText();
        System.out.println("connection string: " + connectionString);
        String pw = new String(password.getPassword());

        ProfileCreator pc = new ProfileCreator(driver.getText(), connectionString, username.getText(), pw);
        System.out.println("Generating profile...");
        DatabaseProfile p = new DatabaseProfile(profileName.getText());
        try {
            p = pc.generateProfile(profileName.getText());
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "There is an error with this profile:\n\n" + e.getMessage(), "Profile Generation Error", JOptionPane.ERROR_MESSAGE);
        }

        
        ProfileManager.CURRENT_PROFILE = p;
        ProfileManager.saveProfile(profileName.getText() + ".fdp", p);
        System.out.println("Profile generation complete.");
        
        //TODO: Would rather have a "continue anyway" interface instead of not closing the wizard.
        if (p != null && p.isConnected()) {
            //Continue on with reloading the GUI if necessary, but we won't run this again.
            if (FBGFlow.getState().equals("loading"))
                new LoadFrame(false);
                
            this.dispose();
        }
    }

    private void setupTemplates() {
        String sep = System.getProperty("file.separator");
        String templatePath = FBGConstants.USER_FILE_PATH + "profiles" + sep + "templates";
        System.out.println(templatePath);
        File dir = new File(templatePath);
        File[] files = dir.listFiles();
        
        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(new JFrame(), "No profile templates found. Making some for you.", "Making Templates", JOptionPane.INFORMATION_MESSAGE);
            ProfileTemplate.makeTemplates();
            files = dir.listFiles(); //repopulate the file list. gets rid of null pointer exception
        }
        
        for (int c = 0; c < files.length; c++) {
            if (files[c].getName().endsWith(".fdt")) {
                System.out.println("Loading Template " + files[c]);
                ProfileTemplate template = ProfileManager.loadTemplate(files[c]);
                templateMenu.addItem(template);
            }
        }
    }
    
    public static void main(String[] args) {
        new ProfileWizard();
    }
}
