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

import static edu.uara.FBG.noInit;

import java.io.File;
import java.sql.SQLException;

import edu.uara.config.Config;
import edu.uara.db.DBInit;
import edu.uara.db.profiles.ProfileManager;
import edu.uara.project.FactbookProject;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.WriteAbortedException;


/**
 * This class contains a number of static methods that relate to the actual operations
 * of the program. It has discrete steps: parsing, uploading, generating the Factbook,
 * etc. It is called by the FBGConsole and FBGFrame classes as necessary to make the
 * program work. The FBGConsole program runs through it all at once. The graphic interface
 * waits for user input.
 * @author jeff
 */
public class FBGFlow {
    private static FactbookProject openProject;
    private static String programState;
    
    /**
     * A simple method that loads the database profile. Creation of new profiles
     * are handled elsewhere; in FBGConsole and the ProfileWizard, specifically.
     */
    public static void setupProfile() {
        String profileName = Config.getProperty("defaultProfile");
        if (profileName == null || profileName.equals(""))
            profileName = FBGConstants.PROFILE_PATH + "db.profile";
        
        System.out.println("Loading database profile: " + profileName);
        ProfileManager.CURRENT_PROFILE = ProfileManager.loadProfile(profileName);
        if (ProfileManager.CURRENT_PROFILE != null)
            ProfileManager.CURRENT_PROFILE.login();
    }
    
    /**
     * A simple method that loads the database profile. Creation of new profiles
     * are handled elsewhere; in FBGConsole and the ProfileWizard, specifically.
     */
    public static void setupProfile(String profilePath) {
        System.out.println("Loading database profile: " + profilePath);
        ProfileManager.CURRENT_PROFILE = ProfileManager.loadProfile(profilePath);
        if (ProfileManager.CURRENT_PROFILE != null)
            ProfileManager.CURRENT_PROFILE.login();
        
    }
    
    /**
     * A simple method that loads the database profile. Creation of new profiles
     * are handled elsewhere; in FBGConsole and the ProfileWizard, specifically.
     */
    public static void setupProfile(File profile) {
        System.out.println("Loading database profile: " + profile.getAbsolutePath());
        ProfileManager.CURRENT_PROFILE = ProfileManager.loadProfile(profile);
        if (ProfileManager.CURRENT_PROFILE != null)
            ProfileManager.CURRENT_PROFILE.login();
    }
        
    /**
     * This is the initialization step of the Factbook Generator. It runs after
     * the profile has been loaded by the FBG class.
     */
    public static void init() {
        if (ProfileManager.CURRENT_PROFILE == null) {
            System.out.println("No profile. Can't run init!");
        }
        else {
            //Load the last open project should it exist
            if (Config.getProperty("lastOpen") != null) {
                try {
                    setOpenProject(FactbookProject.loadFactbook(Config.getProperty("lastOpen")));
                }
                catch (IOException e) {
                    System.err.println("---Error loading last open project. File was corrupt!---");
                    e.printStackTrace();
                }
            }
            
            if (noInit == false) {
                System.out.println("init: Verifying table structure...");

                try {
                    //Init the database
                    DBInit.init();
                }
                catch (SQLException e) {
                    System.err.println("Error during init: " + e);
                }

                System.out.println("init: Done.");
            }
            else System.out.println("init: Skipping all init stages.");
        }
    }

    public static void init(boolean loadProject) {
        if (loadProject)
            init();
        else {
            if (noInit == false) {
                System.out.println("init: Verifying table structure...");

                try {
                    //Init the database
                    DBInit.init();
                } catch (SQLException e) {
                    System.err.println("Error during init: " + e);
                }

                System.out.println("init: Done.");
            }
            else System.out.println("init: Skipping all init stages.");
        }
    }
    
    /**
     * This method will probably be renamed once we move everything into class files.
     * It will be responsible for using reflection to enumerate the classes that generate
     * the factbook and then running those classes.
     */
    public static void compileFactbook() {
        System.out.println("Beginning generation of Factbook...");        
        System.out.println("Factbook has been compiled, but not published.");
    }
    
    /**
     * This method outputs help for the program
     */
    public static void consoleHelpMe() {
        System.out.println("---Factbook Generator " + FBG.versionString + "---");
        System.out.println("Generates the SU Factbook in PDF and Excel formats. Can also be used to generate");
        System.out.println("specific tables in several stages of compilation.");
        System.out.println();
        System.out.println("Command-line options (overrides config values):");
        System.out.println("-v, --version: Display version and license info and exit.");
        System.out.println("-c: Use console mode.");
        System.out.println("-g: Use the graphical interface.");
        System.out.println("-h, --help, -?: Display this help.");
        System.out.println("--make-profiles: Generates SQL Server 2005 profile templates.");
        System.out.println("--no-init: Don't run the init script.");
        System.out.println("-p <file>: Set the default database profile.");
        System.out.println("-q, --quiet: Don't start up the load frame (GUI only).");
        System.out.println("--ais <file>: Specify location of the AIS data file.");
        System.out.println("--pop <file>: Specify location of the POP data file.");
        System.out.println("--eds <file>: Specify location of the EDS data file.");
        System.out.println("--eis <file>: Specify location of the EIS data file.");
        System.out.println("--dis <file>: Specify location of the DIS data file.");
    }
    
    /**
     * This outputs version information for the program.
     */
    public static void consoleVersion() {
        System.out.println("Factbook Generator " + FBG.versionString);
        System.out.println("Licensed under the GPLv3 Open Source License");
    }
    
    public static void setupDirectories() {
        File dir = new File(FBGConstants.USER_FILE_PATH);
        dir.mkdir();
        dir = new File(FBGConstants.PROFILE_PATH);
        dir.mkdir();
        dir = new File(FBGConstants.TEMPLATE_PATH);
        dir.mkdir();
        dir = new File(FBGConstants.TABLE_IMAGE_PATH);
        dir.mkdir();
    }
    
    public static String getState() {
        if (programState == null)
            return "not_running";
        else
            return programState;
    }
    
    public static void setState(String s) {
        programState = s;
    }
    
    public static FactbookProject getOpenProject() {
        return openProject;
    }
    
    public static void setOpenProject(FactbookProject p) {
        openProject = p;
    }
}
