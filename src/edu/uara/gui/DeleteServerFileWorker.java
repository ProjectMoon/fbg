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
import java.util.Stack;

import javax.swing.SwingWorker;

import edu.uara.db.manager.DatabaseManager;
import javax.swing.JFrame;

/**
 * This backgrounds the deletion of a file on the database server. This prevents the
 * GUI from locking up.
 * @author jeff
 */
public class DeleteServerFileWorker extends SwingWorker {
    private Stack<String> tableNames = new Stack<String>();
    private Stack<String> years = new Stack<String>();
    private ServerViewPanel signalPanel;
    
    /**
     * Creates a new worker with no files to delete.
     */
    public DeleteServerFileWorker() {}
    
    /**
     * Creates a new worker with the specified file to delete.
     * @param file - The filename (AIS, DIS, etc) to delete.
     * @param year - The year to delete (2008, 2009, etc).
     */
    public DeleteServerFileWorker(String file, String year) {
        tableNames.push(file);
        years.push(year);
    }
    
    /**
     * Handles invoking of methods to delete the "files."
     * @return null
     */
    @Override
    protected Object doInBackground() {
        DatabaseManager dbm = DatabaseManager.getInstance();
        while (tableNames.isEmpty() == false) {
            String tableName = tableNames.pop();
            String fileYear = years.pop();
            dbm.deleteServerFile(tableName, fileYear);
            signalPanel.updateList();
        }
        return null;
    }
    
    /**
     * Add a file to be deleted.
     * @param tableName - The file name (AIS, DIS, etc)
     * @param fileYear - The year (2008, 2009, etc)
     */
    public synchronized void addFile(String tableName, String fileYear) {
        tableNames.push(tableName);
        years.push(fileYear);
    }
    
    /**
     * Set the frame to signal when deletes are done.
     * @param f - The FBGFrame to signal.
     */
    public void setParentView(ServerViewPanel p) {
        signalPanel = p;
    }

}
