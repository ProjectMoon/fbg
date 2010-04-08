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
package edu.uara.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

import edu.uara.FBG;
import edu.uara.FBGConstants;

/**
 * This is a wrapper class for the properties object.
 * @author jeff
 */
public class Config {
    public static final String CONFIG_PATH = FBGConstants.USER_FILE_PATH + System.getProperty("file.separator") + "fbg.cfg";
    private static Properties config;
    
    private Config() {}
    
    private static void loadProperties() {
        config = new Properties();
        try {
            System.out.println("Loading FBG configuration.");
            System.out.println(FBGConstants.USER_FILE_PATH + "fbg.properties");
            FileInputStream fis = new FileInputStream(CONFIG_PATH);
            config.load(fis);
            fis.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Creating default config file in " + FBGConstants.USER_FILE_PATH + "fbg.cfg.");
            createConfigFile();
        }
        catch (Exception e) {
            System.err.println("There was an error loading the program configuration");
            e.printStackTrace();
            System.exit(1);
        }        
    }
    
    public static void load() {
        loadProperties();
    }
    
    
    public static String getProperty(String name) {
        return config.getProperty(name);
    }

    public static void removeProperty(String name) {
        config.remove(name);
    }
    
    public static void setProperty(String name, String value) {
        config.setProperty(name, value);
    }
    
    public static void saveProperties() {
        try {
            FileOutputStream fos = new FileOutputStream(CONFIG_PATH);
            config.store(fos, "FBG Config File");
            fos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void createConfigFile() {
        config.setProperty("mode", "normal");
        config.setProperty("autoUpdate", "true");
        
        try {
            String comments = "Factbook Generator " + FBG.versionString + " Config File\n" +
                    "****THINGS YOU SHOULD EDIT****\n" +
                    "autoUpdate: set to false if you don't want the program to check for updates.\n";
            FileOutputStream fos = new FileOutputStream(CONFIG_PATH);
            config.store(fos, comments);
            fos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }        
    }
}
