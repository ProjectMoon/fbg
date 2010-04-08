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
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.uara.FBG;
import edu.uara.FBGFlow;
import edu.uara.db.profiles.ProfileManager;
import edu.uara.updater.Updater;
import java.io.PrintStream;

//TODO: Fix updater race condition.

/**
 * A temporary frame that acts as a "console" for the graphical interface before
 * the main FBGFrame and ConsoleFrame take over. This class handles the calling of
 * the auto-updater and the profile creator for the GUI interface.
 * @author jeff
 */
public class LoadFrame extends JFrame {
    private boolean runProfileWizard;
    protected boolean waiting = false;
    
    /**
     * Creates the graphical elements of this load frame, and calls executeLoad().
     */
    public LoadFrame() {
        runProfileWizard = true;
        createLoadFrame();
        executeLoad();
    }

    /**
     * Creates the graphical elements of this load frame and calls executeLoad().
     * Also allows an explict setting of whether to run the profile wizard or not.
     * @param runProfileWizard If this is set to false, the profile wizard will not be run.
     */
    public LoadFrame(boolean runProfileWizard) {
        this.runProfileWizard = runProfileWizard;
        createLoadFrame();
        executeLoad();
    }

    /**
     * Creates the load frame's UI elements.
     */
    private void createLoadFrame() {
        if (FBG.hideLoadFrame == false) {
            super.setTitle("Factbook Generator: Loading...");
            super.setSize(400, 200);

            JTextArea jt =  new JTextArea();
            JScrollPane scroller = new JScrollPane(jt);
            super.add(scroller);

            //redirect System.out and System.err
            try {
                System.setOut(new PrintStream(new TextAreaOutputStream(jt)));
                //System.setErr(new PrintStream(new TextAreaOutputStream(jt)));
            }
            catch(Exception e) { e.printStackTrace(); }         
            super.setVisible(true);
        }
    }

    /**
     * Calls all methods relevant to loading in the following order:
     * updater, profile loading/creation, program init, and then finally
     * the main GUI thread.
     */
    private void executeLoad() {
        //If we're attempting to update, check that first in the background.
        //if (FBG.update) {
            //Thread t = new Thread(new Updater());
            //t.start();
        //}
       
        /*
         * We continue with the loading of the database profile. This is used to
         * make vendor-specific error codes transparent, and standardize certain
         * information such as the database's username and password.
         * 
         * If the profile is not present, we immediately enter the "Profile Wizard"
         * frame which allows the user to set up a new profile. The program will then
         * reload itself from the Profile Wizard frame and start up normally with
         * the loaded profile (by hitting the else clause).
         */
        FBGFlow.setupProfile();
        if (ProfileManager.CURRENT_PROFILE == null && runProfileWizard == true) {
            new ProfileWizard();
            this.dispose();
        }
        else {
            FBGFlow.init();

            //We're done with this loading frame.
            this.dispose();
            
            //Start a GUI thread
            Thread t = new Thread(new GUIThread());
            t.start();
            
        }
    }
}
