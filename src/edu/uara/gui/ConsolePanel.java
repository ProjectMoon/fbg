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
import java.awt.Dimension;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.uara.FBG;

/**
 * A frame that acts as an output for the console in GUI mode. Runs hidden, and will become
 * visible when necessary or when the user selects it to be visible.
 * @author jeff
 */
public class ConsolePanel extends JPanel {
    /**
     * Creates the ConsoleFrame and redirects System.out.
     */
    public ConsolePanel() {
        if (FBG.hideLoadFrame == false) {
            JTextArea jt =  new JTextArea();
            //jt.setPreferredSize(new Dimension(500, 100));
            setLayout(new BorderLayout());
            JScrollPane scroller = new JScrollPane(jt);
            scroller.setAutoscrolls(true);
            super.add(scroller, BorderLayout.CENTER);

            //redirect System.out and System.err
            try {
                System.setOut(new PrintStream(new TextAreaOutputStream(jt)));
                //System.setErr(new PrintStream(new TextAreaOutputStream(jt)));
            }
            catch(Exception e) { System.err.print("Console frame error: " + e); }        
        }
    }    
}
