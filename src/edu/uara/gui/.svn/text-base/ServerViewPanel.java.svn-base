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
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.uara.db.profiles.ProfileManager;
import edu.uara.gui.dataviewer.DataViewerFrame;
import java.awt.event.MouseListener;

/**
 * This class presents a view of the files on the server to the user and allows them
 * to interact with it.
 * @author jeff
 */
public class ServerViewPanel extends JPanel implements ListSelectionListener {
    private JList filesList;
    private List<String> listData = new LinkedList<String>();
    private List<String> internalData = new ArrayList<String>();
    private ArrayList<String> selectedFiles = new ArrayList<String>();
    private JScrollPane listScroller;
    
    /**
     * Creates the graphical elements of this panel.
     */
    public ServerViewPanel() {
        super();
        this.setLayout(new BorderLayout());

        getFileList();

        filesList = new JList(listData.toArray());
        addMouseListenerToList();

        filesList.addListSelectionListener(this);
        
        listScroller = new JScrollPane(filesList);
        this.add(listScroller, BorderLayout.CENTER);
    }

    /**
     * Adds the mouse listener to the JList. Necessary because it gets
     * removed when updateList is called.
     */
    private void addMouseListenerToList() {
        filesList.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    List<String> files = getSelectedFiles();
                    for (String file : files) {
                        String[] serverFile = file.split(";");
                        new DataViewerFrame(serverFile[0], serverFile[1]);
                    }
                }
            }

            public void mousePressed(MouseEvent arg0) {}
            public void mouseReleased(MouseEvent arg0) {}
            public void mouseEntered(MouseEvent arg0) {}
            public void mouseExited(MouseEvent arg0) {}
        });
    }

    /**
     * Clears out the currently selected file paths and then updates them
     * with the new data.
     * @param e - The list selection event, fired by changing items in the list.
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            selectedFiles.clear();
            for (int i : filesList.getSelectedIndices()) {
                selectedFiles.add(internalData.get(i));
            }
        }
    }
    
    /**
     * Gets the currently selected file paths in this panel.
     * @return The list of currently selected paths.
     */
    public List<String> getSelectedFiles() {
        return selectedFiles;
    }
    
    /**
     * Retrieves a list of "files" from the database server.
     */
    private void getFileList() {
        if (ProfileManager.CURRENT_PROFILE != null) {
            ArrayList<String> list = new ArrayList<String>();
            try {
                System.out.println("Handling eet");
                Connection conn = ProfileManager.CURRENT_PROFILE.getConnection();
                PreparedStatement stmt = conn.prepareStatement("select * from Files", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery();

                while (rs.next() != false) {
                    String fileName = rs.getString(1).trim();
                    String fileYear = rs.getString(2).trim();
                    String fileStatus = rs.getString(4).trim();
                    String listEntry = fileName + " file (Year " + fileYear + ")";
                    String internalEntry = fileName + ";" + fileYear;

                    if (fileStatus.equals("UNFINISHED"))
                        listEntry += " -- CORRUPTED";

                    listData.add(listEntry);
                    internalData.add(internalEntry);
                }
            }
            catch (Exception e) {
                System.err.println("Error displaying server view: " + e);
            }
        } //end profile if
        else System.err.println("Can't load server list without a profile!");
    }
    
    /**
     * Forces the list to update with a new set of information from the server.
     */
    public void updateList() {
        this.remove(listScroller); 
        internalData.clear();
        listData.clear();
        getFileList();
        filesList = new JList(listData.toArray());
        filesList.addListSelectionListener(this);
        addMouseListenerToList();
        
        listScroller = new JScrollPane(filesList);        
        this.add(listScroller, BorderLayout.CENTER);
        this.validate();

    }
}
