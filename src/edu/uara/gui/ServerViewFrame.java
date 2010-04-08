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
import edu.uara.db.profiles.DatabaseProfile;
import edu.uara.db.profiles.ProfileManager;
import edu.uara.gui.datawizard.UploadWizard;
import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.event.*;
import java.util.List;
/**
 * This class encapsulates a ServerViewPanel, for interaction by the users. It
 * contains buttons for deleting selected files and uploading more files. Double
 * clicking on a file in the list brings up the data viewer, which allows the user
 * to see what's in the database.
 * @author jh44695
 */
public class ServerViewFrame extends JFrame {
    private ServerViewPanel serverPanel;
    private JButton deleteButton;
    private JButton uploadButton;
    private JButton clearDatabaseButton;

    /**
     * Creates a new server view frame.
     */
    public ServerViewFrame() {
        super("Server View");

        String connLabelText = "";

        if (ProfileManager.CURRENT_PROFILE.isConnected()) {
            DatabaseProfile p = ProfileManager.CURRENT_PROFILE;
            String connUrl = p.getConnectionURL();

            //remove everything before the // in the JDBC url so we just get
            //the host name and DB name.
            String dispUrl = connUrl.substring(connUrl.indexOf("//") + 2);
            connLabelText = p.getUsername() + "@" + dispUrl;
        }
        else {
            connLabelText = "Not connected";
        }

        super.setLayout(new BorderLayout());
        
        //Server view panel
        serverPanel = new ServerViewPanel();        

        //Buttons panel
        JPanel buttonsPanel = new JPanel();
        deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!ProfileManager.CURRENT_PROFILE.isConnected()) {
                    Utilities.displayError(ServerViewFrame.this, "Unable to Delete",
                            "You are not connected to an FBG database server. " +
                            "Please login with the server menu first.");
                    return;
                }
                List<String> filesToDelete = serverPanel.getSelectedFiles();
                DeleteServerFileWorker dsfw = new DeleteServerFileWorker();

                for (String file : filesToDelete) {
                    //[0] is name, [1] is year
                    String[] serverFile = file.split(";");
                    dsfw.addFile(serverFile[0], serverFile[1]);

                }
                dsfw.setParentView(serverPanel);
                dsfw.execute();
            }
        });

        uploadButton = new JButton("Upload More Files");
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!ProfileManager.CURRENT_PROFILE.isConnected()) {
                    Utilities.displayError(ServerViewFrame.this, "Unable to Upload",
                            "You are not connected to an FBG database server. " +
                            "Please login with the server menu first.");
                    return;
                }
                new UploadWizard(ServerViewFrame.this, serverPanel);
            }
        });
        
        clearDatabaseButton = new JButton("Clear Database");
        clearDatabaseButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                if (!ProfileManager.CURRENT_PROFILE.isConnected()) {
                    System.err.print("Can't clear database without a connection!");
                    return;
                }            
                new ClearDatabaseWorker(serverPanel).execute();
            }
        });

        buttonsPanel.add(deleteButton);
        buttonsPanel.add(uploadButton);
        buttonsPanel.add(clearDatabaseButton);
        
        super.add(buttonsPanel, BorderLayout.NORTH);
        super.add(serverPanel, BorderLayout.CENTER);
        super.add(new JLabel("Connected to: " + connLabelText), BorderLayout.SOUTH);
        
        super.setSize(400, 400);
        super.setVisible(true);
    }
}
