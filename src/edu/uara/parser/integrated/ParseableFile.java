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
package edu.uara.parser.integrated;

import java.io.File;

/**
 * A class that wraps around a File object in order to maintain more information
 * about it like file type (AIS, DIS, etc) and the year of the file.
 * @author jeff
 */
public class ParseableFile {
    private File file;
    private int year;
    private String type;
    
    public ParseableFile(String type, File file, int year) {
        this.file = file;
        this.year = year;
        this.type = type;
    }
    
    public File getFile() {
        return file;
    }
    
    public int getYear() {
        return year;
    }
    
    public String getType() {
        return type;
    }
    
    public String toString() {
        return file.toString() + " (" + type + ")";
    }
}
