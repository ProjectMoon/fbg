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

import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import edu.uara.tableeditor.TableObject;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * Simple dialog for entering a footnote for a TableObject.
 * @author jeff
 */
public class FootnoteDialog extends JDialog {
    
    private JTextArea footnote;
    private JButton closeButton;
    
    public FootnoteDialog(JFrame parent, TableObject to) {
        super(parent, true);
        footnote = new JTextArea(to.getFootnote());
        closeButton = new JButton("Ok");
        closeButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent arg0) {
                closeDialog();
            }
            
        });
        
        super.setSize(300, 200);
        super.setLayout(new BorderLayout());
        super.add(new JLabel("Enter a footnote: "), BorderLayout.NORTH);
        super.add(footnote, BorderLayout.CENTER);
        super.add(closeButton, BorderLayout.SOUTH);
        super.setTitle("Enter Footnote for " + to.getName());
        super.setVisible(true);
    }
    
    public String getNote() {
        return footnote.getText();
    }
    
    private void closeDialog() {
        this.dispose();
    }
}
