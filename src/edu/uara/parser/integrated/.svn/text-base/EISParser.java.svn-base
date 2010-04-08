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
import static edu.uara.parser.integrated.ParserUtils.trimString;

import java.util.ArrayList;
import java.util.List;

import edu.uara.exceptions.UARAException;

/**
 * EIS parser class
 * @author aloysius
 */
class EISParser implements Parser {
    /**
     * The parse function inside of this class runs through the EIS file
     * it pulls out the data in the corosponding collumns and puts the values
     * into a 2D list of integers and returns that list.
     * @param path: The path to the EIS file
     * @return List<List<Integer>> is a 2D array containing the data that was in the EIS file
     * @throws edu.uara.exceptions.UARAException
     */
    public synchronized List<RawData> parse(String value) throws UARAException {
        char career;
        Integer year;

        ArrayList<RawData> dataLine = new ArrayList<RawData>();
        // Session Code
        dataLine.add(new IntegerRawData(transform(value.substring(0,2))));
        // Year
        year = transform(value.substring(2,4));
        if(year > 50) { year += 1900; }
        else if (year <= 50) { year += 2000; }
        dataLine.add(new IntegerRawData(year));
        // FICE Number College
        dataLine.add(new IntegerRawData(transform(value.substring(4,10))));
        // Campus Code Position 11
        dataLine.add(new StringRawData(trimString(value.substring(10,11))));
        // SSN
        dataLine.add(new StringRawData(trimString(value.substring(11,20))));
        // Blank Position 20
        // Gender
        dataLine.add(new IntegerRawData(transform(value.substring(21,22))));
        // Ethnicity
        dataLine.add(new IntegerRawData(transform(value.substring(22,23))));
        // Birth Year
        dataLine.add(new IntegerRawData(transform(value.substring(23,27))));
        // Geographic Origin
        dataLine.add(new IntegerRawData(transform(value.substring(27,30))));
        // Tuition Status
        dataLine.add(new IntegerRawData(transform(value.substring(30,31))));
        // Blank Positions 31 - 34
        // Program Code
        dataLine.add(new IntegerRawData(transform(value.substring(35,41))));
        // Degree Saught
        dataLine.add(new IntegerRawData(transform(value.substring(41,43))));
        // Advanced Graduate Cirtificate
        // First-Time Status
        dataLine.add(new IntegerRawData(transform(value.substring(43,44))));
        // Credit Hour Load
        dataLine.add(new DoubleRawData(transform(value.substring(44,48))/100.0));
        // Attendance Catagory
        dataLine.add(new IntegerRawData(transform(value.substring(48,49))));
        // Student Level
        dataLine.add(new IntegerRawData(transform(value.substring(49,51))));
        // Carreer
        dataLine.add(new StringRawData(trimString(value.substring(51,52))));
        // SAT Verbal
        dataLine.add(new IntegerRawData(transform(value.substring(52,55))));
        //SAT Math
        dataLine.add(new IntegerRawData(transform(value.substring(55,58))));
        // Blank Position 58
        // Reginal Center Activitie
        dataLine.add(new IntegerRawData(transform(value.substring(59,60))));
        // Reginal Center Institution
        dataLine.add(new IntegerRawData(transform(value.substring(60,62))));
        // The rest are blank

        return dataLine;
    }
    
    /**
     * toString method.
     * @return A string representation of this class's name.
     */
    public String toString() {
        return "EISParser";
    }    
}