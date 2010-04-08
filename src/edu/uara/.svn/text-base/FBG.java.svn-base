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
package edu.uara;

import java.io.PrintStream;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.uara.config.Config;
import edu.uara.db.profiles.ProfileTemplate;
import edu.uara.gui.LoadFrame;
import edu.uara.gui.main.MainFactbookFrame;

/**
 * This class is the main class of the entire Factbook Generator program.
 * It handles parsing of command line options, and then branches into either
 * console or GUI mode.
 * @author jeff
 */
public class FBG {
    //variables relating to version
    public static String versionString = "Megatron (unstable)";
    
    //variables relating to commandline options.
    public static boolean guiStarted = true;
    public static boolean noInit = false;
    public static boolean update = true;
    public static boolean hideLoadFrame = false;
    public static boolean devMode = false;        
        
    //the frame
    public static MainFactbookFrame mainFrame;
    
    //original output streams
    public static PrintStream origOut = System.out;
    public static PrintStream origErr = System.err;
    
    private String args[];
    
    private FBG(String[] args) {
        this.args = args;
    }
    
    
    /**
     * This finds a file based on command line options passed to the program.
     * @param fileType - AIS, DIS, POP, EIS, or EDS.
     * @param args - The command line options array.
     * @return The path to the file, or the default path if it couldn't find it.
     */
    public static String findFile(String fileType, String[] args) {
        //There are two cases... specified on command line, or not.
        //If specified on command line we search through for the option and
        //then take the following argument immediately after as the file name.
        
        String filePath = null;
        String optStr = "--" + fileType.toLowerCase();
        //first search for command-line argument.
        for (int c = 0; c < args.length; c++) {
            if (args[c].equals(optStr)) {
                //first check the syntax
                if (c + 1 >= args.length) {
                    System.err.println(fileType + " file: File argument syntax error. Using default path.");
                    //exit only if we're in console mode, otherwise halt execution by returning
                    if (!guiStarted)
                        System.exit(1);
                }
                //otherwise, we're good
                else filePath = args[c + 1];
                break;
            }
        }
        
        //now, what if no option was specified?
        if (filePath == null) filePath = FBGConstants.USER_FILE_PATH + "data" + System.getProperty(("file.separator")) + fileType + ".dat";
        return filePath;
    }
        
    /**
     * The main methd of the program. This is where many important tasks happen. First, the
     * program loads the configuration file. It then processes command-line arguments, and overrides
     * various config options as necessary. It then branches into GUI mode (where further loading occurs)
     * or goes into console mode.
     * @param args
     */
    public static void main(String[] args) {
        //Set the program to the loading state.
        //Gets set to running by GUIThread.
        FBGFlow.setState("loading");
        
        //Add our shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(new CleanupHook()));
        
        //Load config
        Config.load();
        
        //Create directories should they not be around
        FBGFlow.setupDirectories();

        //Now, set up some options by using the using properties file.
        //They will be overriden by the console options, which is why we set them first.        
        if (Config.getProperty("autoUpdate").equals("true"))
            update = true;
        else
            update = false;
        
        if (Config.getProperty("mode").equals("development"))
            devMode = true;
                
        //Read in some console options the ghetto way.
        //First we start with the options that completely change the way the program
        //works, then we move on to the options that just change config options.
        for (int c = 0; c < args.length; c++) {
            if (args[c].equals("-v") || args[c].equals("--version")) {
                FBGFlow.consoleVersion();
                System.exit(0);
            }
            if (args[c].equals("-h") || args[c].equals("--help") || args[c].equals("-?")) {
                FBGFlow.consoleHelpMe();
                System.exit(0); 
            }
            if (args[c].equals("--make-profiles")) {
                System.out.println("Creating sample profile files.");
                ProfileTemplate.makeTemplates();
                System.out.println("Done.");
                System.exit(0);
            }
            if (args[c].equals("-p")) {
                System.out.println("Setting the default profile to " + args[c + 1]);
                Config.setProperty("defaultProfile", args[c + 1]);
            }
            
            if (args[c].equals("--no-init")) noInit = true;
            if (args[c].equals("--no-update")) update = false;
            if (args[c].equals("-q") || args[c].equals("--quiet")) hideLoadFrame = true;
        }        
        
        
        //Finally, load the graphical interface.
        
        //if we're using graphical interface, start the loading frame
        //as well as set the native L&F
        //attempt to set the Look and Feel to something native to the system.
        //if it fails, we just use the default java UI instead.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException e) {
            System.err.println("Warning: Unsupported UI look & feel.");
        }
        catch (ClassNotFoundException e) {
            System.err.println("Warning: The native look and feel doesn't appear to exist.");
        }
        catch (InstantiationException e) {
            System.err.println("Warning: There was an error attempting to use the native look and feel.");
        }
        catch (IllegalAccessException e) {
            System.out.println("Warning: There was a permissions error attempting to use the native look and feel.");
        }
            
        new LoadFrame();
    }
}
