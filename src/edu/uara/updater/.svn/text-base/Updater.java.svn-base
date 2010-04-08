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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import edu.uara.config.Config;

/**
 * This class takes care of automatically updating the program. It connects to a server-side
 * "repository" (defined by the user). If the version listed is greater than the one in the
 * program, it will initiate a download.
 * @author jeff
 */
public class Updater implements Runnable {
    public static boolean updated = false;
    private static URLConnection fbgConn;
    
    private static void initConnection(String url) {
        try {
            URL fbgRepo = new URL(url);
            fbgConn = fbgRepo.openConnection();
        }
        catch(Exception e) { System.err.println("initConnection(): " + e); }
    }
    
    public static void main(String[] args) {
        new Updater().run();
    }
    
    private static String getCurrentVersion() {
	String version = Config.getProperty("version");
        if (version == null || version.equals("")) return null;
        return version;
    }
    
    public void run() {       
        String currVersion = getCurrentVersion();
        
        //we need to display the changelog
        initConnection("http://www.thermetics.net/fbg/changelog.txt");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(fbgConn.getInputStream()));
            String updateText = "";
            String line = "";
            String versionCheck = in.readLine();
            String currentVersion = getCurrentVersion();
            
            if (versionCheck.equals(currentVersion) == false) {
                while ((line = in.readLine()) != null)
                    updateText += line + "\n";

                in.close();
                Config.setProperty("version", versionCheck);
                Config.saveProperties();
                updateScript();
                new UpdatedFrame(updateText);
            }
        }
        catch (Exception e) { System.err.print("Updater error: " + e); }
    }

    /**
     * This is run to handle fixing issues between versions.
     */
    private void updateScript() {
        System.out.println("Nothing to update in this version!");
    }

    private static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

}
