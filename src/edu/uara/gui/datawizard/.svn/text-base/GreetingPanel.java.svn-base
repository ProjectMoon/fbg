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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * This is the panel that users are greeted with at the start of the wizard. Very
 * simple class that just has a label in it containing the message.
 * @author jeff
 */
public class GreetingPanel extends JPanel {
    private String greetingText = "Welcome to the Upload Files wizard. This wizard" +
            " will assist you in uploading one or more raw text files to the database" +
            " you are currently connected to.\n\nYou will first be presented with a list" +
            " of years to select from. You will then be asked to provide files for each" +
            " year. The program will then take care of uploading the files to the server.";
    
    public GreetingPanel() {
        super.setLayout(new BorderLayout());
        JTextArea label = new JTextArea(greetingText);
        label.setEditable(false);
        label.setOpaque(false);
        label.setLineWrap(true);
        label.setWrapStyleWord(true);
        super.add(new JLabel("File Upload Wizard"), BorderLayout.NORTH);
        super.add(label, BorderLayout.CENTER);
    }
}
