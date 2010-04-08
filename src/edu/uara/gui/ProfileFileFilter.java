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

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * This class is responsible for making sure only .fdp (Factbook Generator Database Profiles) and
 * directories show up on the select dialogue for loading profiles.
 * @author jeff
 */
public class ProfileFileFilter extends FileFilter {
    public boolean accept(File f) {
        if (f.getName().endsWith(".fdp") || f.isDirectory())
            return true;
        else
            return false;
    }
    
    public String getDescription() {
        return "FBG Profiles (*.fdp)";
    }
}
