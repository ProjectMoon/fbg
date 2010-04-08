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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class allows the (enterprising) user to edit the table generation code.
 * If we can sufficiently abstract/hide the SQL, it may be possible for the UARA
 * people to use this functionality to create/edit their tables.
 * NOTE: Presently deprectated since table generation code is compiled into the jar.
 * @author jeff
 */
public class TableEditorFrame extends JFrame {
    private JTextArea editorArea;
    
    /**
     * Creates a new editor frame by loading the file from the specified path.
     * @param filePath - The script file to load.
     */
    public TableEditorFrame(String filePath) {
        super("Editing Table " + filePath);
        super.setSize(700, 700);
        super.setLayout(new BorderLayout());
        editorArea = new JTextArea();
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
            
            while ((line = reader.readLine()) != null) {
                editorArea.append(line);
                editorArea.append("\n");
            }
        }
        catch (Exception e) { System.err.print("There was an error reading the table code.\n"); }
        
        JScrollPane editorScroller = new JScrollPane(editorArea);
        super.add(editorScroller, BorderLayout.CENTER);
        
        //set up bottom buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JButton("Save"));
        bottomPanel.add(new JButton("Cancel"));
        super.add(bottomPanel, BorderLayout.SOUTH);
        super.setVisible(true);
    }
    
}
