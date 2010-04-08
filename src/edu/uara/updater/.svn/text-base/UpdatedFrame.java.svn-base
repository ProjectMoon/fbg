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
package edu.uara.updater;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * This frame is displayed when an update has been detected. It tells the user what was changed
 * and then shuts down the program.
 * @author jeff
 */
public class UpdatedFrame extends JFrame implements ActionListener {
    private JButton closeButton = new JButton("Close");
    public UpdatedFrame(String svnOutput) {
        super.setTitle("Factbook Generator Updated!");
        super.setSize(500, 400);
        super.setLayout(new BorderLayout());
        
        JTextArea output = new JTextArea("The Factbook Generator has been updated!\n---------------\n" + svnOutput);
        JScrollPane scroller = new JScrollPane(output);
        super.add(scroller, BorderLayout.CENTER);
        
        closeButton.addActionListener(this);
        super.add(closeButton, BorderLayout.SOUTH);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton)
            this.dispose();
    }

}
