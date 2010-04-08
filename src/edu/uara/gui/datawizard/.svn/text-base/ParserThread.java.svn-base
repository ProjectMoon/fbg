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
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import edu.uara.gui.ServerViewPanel;
import edu.uara.parser.integrated.*;

/**
 * This class runs the parsers as a separate thread. Used by the graphic interface
 * so it doesn't get locked up. It can handle parsing of one to all of the files
 * for a given year.
 * @author jeff
 */
public class ParserThread implements Runnable {
    private int year;
    private String title;
    private Stack<ParseableFile> filesToParse = new Stack<ParseableFile>();
    private ServerViewPanel signalPanel;

    public ParserThread(ServerViewPanel signalPanel) {
        this.signalPanel = signalPanel;
    }

    /**
     * Displays an IndeterminateProgressFrame to signify to the users that stuff is happening,
     * and calls the parseFiles method of FBGFlow.
     */
    public void run() {
        System.out.println("Beginning upload...");
        createUploadFrame();
    }
    
    private void createUploadFrame() {
        JFrame f = new JFrame("Uploading files...");
        JProgressBar bar = new JProgressBar();
        bar.setValue(0);
        f.setLayout(new BorderLayout());
        f.add(new JLabel("Uploading files..."), BorderLayout.CENTER);
        f.add(bar, BorderLayout.SOUTH);
        f.pack();
        f.setSize(400, f.getHeight());
        f.setVisible(true);
        
        IntegratedParserManager pm = new IntegratedParserManager(filesToParse);
        pm.parse();

        while (pm.isParsing()) {
            try {
                Thread.sleep(50);
                bar.setValue(pm.getTotalProgress());
            } catch (InterruptedException ex) {
                Logger.getLogger(IntegratedParserManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        f.dispose();
        if (signalPanel != null)
            signalPanel.updateList();
    }
    
    /**
     * Add a file to the parse stack.
     * @param f
     */
    public void addFile(ParseableFile f) {
        filesToParse.push(f);
    }
    
    /**
     * Set the year these parsed files are from.
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }
    
    /**
     * Sets the title of the progress bar while the parser is running.
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
