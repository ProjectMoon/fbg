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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * This class displays an indeterminate progress bar for as long as it's open.
 * Very simple. An indeterminate progress bar simply flows back and forth, as the
 * program does not know how long the task will take.
 * @author jeff
 */
public class IndeterminateProgressFrame extends JFrame {
    private JProgressBar progressBar;
    private JPanel thePanel;
    private JLabel fileLabel;
    
    /**
     * Initialize the bar with specific text to display as the title and
     * text inside the window.
     * @param progressText
     */
    public IndeterminateProgressFrame(String progressText) {
        super();
        setupUI(progressText);
    }
    
    /**
     * Helper method that takes care of setting up the GUI of this frame.
     * @param progressText - The title and text to display inside the window.
     */
    private void setupUI(String progressText) {
        super.setSize(400, 75);
        super.setLayout(new BorderLayout());
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel thePanel = new JPanel();
        thePanel.setLayout(new BorderLayout());
        super.setTitle(progressText);
        
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        fileLabel = new JLabel();
        fileLabel.setText(progressText);
        thePanel.add(fileLabel, BorderLayout.NORTH);
        thePanel.add(progressBar, BorderLayout.CENTER);
        super.add(thePanel, BorderLayout.NORTH);
        super.setVisible(true);        
    }
}
