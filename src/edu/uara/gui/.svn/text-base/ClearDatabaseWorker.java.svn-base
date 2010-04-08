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

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import edu.uara.FBGFlow;
import edu.uara.db.manager.DatabaseManager;

/**
 * This class handles the background of test of COMPLETELY CLEARING THE DATABASE. It drops all tables
 * that are used by the program so that they can be re-created on the program's next run.
 * @author jeff
 */
public class ClearDatabaseWorker extends SwingWorker {
    private ServerViewPanel parent;

    /**
     * Create a new worker with the specified FBGFrame as the parent. The parent is
     * used to signal the server view list.
     * @param parent
     */
    public ClearDatabaseWorker(ServerViewPanel parent) {
        this.parent = parent;
    }

    /**
     * This method invokes the code necessary to clear out the database.
     * @return null, as this worker doesn't need to calculate anything.
     */
    @Override
    protected Object doInBackground() {
    JOptionPane pane = new JOptionPane("Clearing the database will delete EVERYTHING.\nAre you SURE you want to do this?");
    Object[] options = new String[] { "Yes", "No" };
    pane.setOptions(options);
    JDialog dialog = pane.createDialog(parent, "WARNING!");
    dialog.setVisible(true);
    Object o = pane.getValue();
    int result = -1;
    for (int k = 0; k < options.length; k++)
        if (options[k].equals(o))
            result = k;

    if (result == 0) {
        System.out.println("Proceeding with database clear...");
        DatabaseManager dbm = DatabaseManager.getInstance();
        dbm.dropAllTables();
        parent.updateList();
    }

    System.out.println("Re-creating tables from latest init...");
    FBGFlow.init();

    return null;
    }
}