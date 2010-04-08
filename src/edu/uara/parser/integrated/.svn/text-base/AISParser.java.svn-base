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

import static edu.uara.parser.integrated.ParserUtils.transform;

import java.util.ArrayList;
import java.util.List;

/**
 * AIS parser class
 * @author jeff
 */
class AISParser implements Parser {
    /**
     * The parse(String) method parses out the AIS data file and converts the data into
     * a usable format for the program--List<List<Integer>> to be specific.
     * @param path - the path of the AIS data file.
     * @return A List<List<Integer>> containing the data. The outer list is the list of records, with each sublist containing a list of numbers that are each individual piece of data.
     * @throws edu.uara.exceptions.UARAException
     */
    public synchronized List<RawData> parse(String line) throws edu.uara.exceptions.UARAException {
         //attempt to open the file and parse it.
        ArrayList<RawData> subList = new ArrayList<RawData>();
              
        //probably easiest just to use substring to add things to the list.
        //year
        subList.add(new IntegerRawData(transform(line.substring(0, 4))));
        //ssn
        subList.add(new IntegerRawData(transform(line.substring(4,13))));
        //sex
        subList.add(new IntegerRawData(transform(line.substring(13, 14))));
        //race
        subList.add(new IntegerRawData(transform(line.substring(14, 15))));
        //geographical origin
        subList.add(new IntegerRawData(transform(line.substring(15, 18))));
        //acceptance flag
        subList.add(new IntegerRawData(transform(line.substring(18, 19))));
        //enrollment flag
        subList.add(new IntegerRawData(transform(line.substring(19, 20))));
        //student level/status
        subList.add(new IntegerRawData(transform(line.substring(20, 21))));
        //intended major code
        subList.add(new IntegerRawData(transform(line.substring(21, 27))));
        //transfer FICE code
        subList.add(new IntegerRawData(transform(line.substring(27, 33))));
        //institution FICE code
        subList.add(new IntegerRawData(transform(line.substring(33, 39))));
        //residence application
        subList.add(new IntegerRawData(transform(line.substring(39, 40))));
        //residence enrollment
        subList.add(new IntegerRawData(transform(line.substring(40, 41))));

        return subList;
    }
        
    /**
     * toString method.
     * @return A string representation of this class's name.
     */
    public String toString() {
        return "AISParser";
    }
}