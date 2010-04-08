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
import static edu.uara.parser.integrated.ParserUtils.trimDate;
import static edu.uara.parser.integrated.ParserUtils.trimString;

import java.util.ArrayList;
import java.util.List;

import edu.uara.exceptions.UARAException;

/**
 * DIS parser class
 * @author dustin
 */
class DISParser implements Parser {
    private int year;

    public DISParser() {
        year = -1;
    }

    /**
     * Constructor requires the year to be specified since the file itself does
     * not specify the year.
     * @param year
     */
    public DISParser(int year) {
        this.year = year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // TODO get year the file was generated ... this may be difficult for historical data

    /**
     * The parse function inside of this class runs through the EIS file
     * it pulls out the data in the corosponding collumns and puts the values
     * into a 2D list of integers and returns that list.
     * @param path: The path to the DIS file
     * @return List<List<Integer>> is a 2D array containing the data that was in the DIS file
     * @throws edu.uara.exceptions.UARAException
     */
	public synchronized List<RawData> parse(String line) throws UARAException
    {
        if (year < 0) {
            throw new UARAException("Year for DIS Parser has not been set!");
        }
        
        Integer intTemp = null;
        ArrayList<RawData> subList = new ArrayList<RawData>();

        // Year -- inserted from the constructor because the file does not
        // specify a year
        subList.add(new IntegerRawData(this.year));
        
        // Student ID
        subList.add(new StringRawData(trimString(line.substring(0, 9))));
        
        //Institution code
        subList.add(new IntegerRawData(transform(line.substring(9, 11))));

        // Name
        subList.add(new StringRawData(trimString(line.substring(11, 43))));

        //Birthdate...
        subList.add(new DateRawData(trimDate(line.substring(43, 51))));

        //Residency
        subList.add(new StringRawData(trimString(line.substring(51, 53))));

        //Institutional Admit Type
        subList.add(new StringRawData(trimString(line.substring(53, 55))));

        //Original Admit Type
        subList.add(new StringRawData(trimString(line.substring(55, 56))));

        //Original Admit Year
        subList.add(new IntegerRawData(transform(line.substring(56, 60))));

        //Original Admit Term
        subList.add(new IntegerRawData(transform(line.substring(60,61))));

        //Readmit type
        //If readmited 1, else 0
                subList.add(new StringRawData(trimString(line.substring(76,82))));

        //Readmit year
        subList.add(new IntegerRawData(transform(line.substring(62,66))));

        // Readmit term
        subList.add(new IntegerRawData(transform(line.substring(66,67))));

        //Beginning the part of the past education part

        //School type
        //H (highschool) 0
        //C (college) 1
        subList.add(new StringRawData(trimString(line.substring(67, 68))));

        //school code
        subList.add(new StringRawData(trimString(line.substring(68, 75))));

        //Grade Scheme
        subList.add(new IntegerRawData(transform(line.substring(75, 76))));

        //GPA
        intTemp = transform(line.substring(76,82));
        if(intTemp == null) { subList.add(new DoubleRawData(null)); }
        else { subList.add(new DoubleRawData(intTemp / 1000.0)); }

        //end date ccyymm
        subList.add(new DateRawData(trimDate(line.substring(82, 88))));

        //Test score section

        //verbal sat
        subList.add(new IntegerRawData(transform(line.substring(88, 92))));

        //math sat
        subList.add(new IntegerRawData(transform(line.substring(92, 96))));

        //grev
        subList.add(new IntegerRawData(transform(line.substring(96, 100))));

        //greq
        subList.add(new IntegerRawData(transform(line.substring(100, 104))));

        //grea
        subList.add(new IntegerRawData(transform(line.substring(104, 108))));


        //cumulative term
        subList.add(new IntegerRawData(transform(line.substring(108,113))));

        //hours earned
        intTemp = transform(line.substring(113,120));
        if(intTemp == null) { subList.add(new DoubleRawData(null)); }
        else { subList.add(new DoubleRawData(intTemp / 100.0)); }

        //gpa earned
        intTemp = transform(line.substring(120,126));
        if(intTemp == null) { subList.add(new DoubleRawData(null)); }
        else { subList.add(new DoubleRawData(intTemp / 1000.0)); }

        //hours transfered
        intTemp = transform(line.substring(127,133));
        if(intTemp == null) { subList.add(new DoubleRawData(null)); }
        else { subList.add(new DoubleRawData(intTemp / 100.0)); }

        //mehc data
        //month awarded
        subList.add(new IntegerRawData(transform(line.substring(133, 135))));

        //year awarded
        subList.add(new IntegerRawData(transform(line.substring(135, 139))));

        //std id
        //subList.add(new IntegerRawData(transform(line.substring(139, 148))));

        //sex
        subList.add(new IntegerRawData(transform(line.substring(148, 149))));

        //race
        subList.add(new IntegerRawData(transform(line.substring(149, 150))));

        //degree info

        //deg 1
        subList.add(new IntegerRawData(transform(line.substring(150, 152))));

        //taxonomy code deg 1
        subList.add(new IntegerRawData(transform(line.substring(152, 158))));

        //deg 2
        subList.add(new IntegerRawData(transform(line.substring(158, 160))));

        //taxonomy code deg 2
        subList.add(new IntegerRawData(transform(line.substring(160, 166))));

        //deg 3
        subList.add(new IntegerRawData(transform(line.substring(166, 168))));

        //taxonomy code deg 3
        subList.add(new IntegerRawData(transform(line.substring(168, 174))));

        //deg 4
        subList.add(new IntegerRawData(transform(line.substring(174, 176))));

        //taxonomy code deg 4
        subList.add(new IntegerRawData(transform(line.substring(176, 182))));

        //deg 5
        subList.add(new IntegerRawData(transform(line.substring(182, 184))));

        //taxonomy code deg 5
        subList.add(new IntegerRawData(transform(line.substring(184, 190))));

        //deg 1 code awarded
        subList.add(new StringRawData(trimString(line.substring(190, 193))));

        //deg 1 major 1
        subList.add(new StringRawData(trimString(line.substring(193, 197))));

        //deg 1 major 2
        subList.add(new StringRawData(trimString(line.substring(197, 201))));

        //deg 1 minor
        subList.add(new StringRawData(trimString(line.substring(201, 205))));

        //deg 1 award cert
        subList.add(new StringRawData(trimString(line.substring(205, 209))));

        //deg 1 college code
        subList.add(new StringRawData(trimString(line.substring(209, 211))));

        //degree 1  date ccyymm
        subList.add(new DateRawData(trimDate(line.substring(211, 217))));

        //degree 1 gpa
        intTemp = transform(line.substring(217,223));
        if(intTemp == null) { subList.add(new DoubleRawData(null)); }
        else { subList.add(new DoubleRawData(intTemp / 1000.0)); }

        //deg 2
        //deg 2 code awarded
        subList.add(new StringRawData(trimString(line.substring(223, 226))));

        //deg 2 major 1
        subList.add(new StringRawData(trimString(line.substring(226, 230))));

        //deg 2 major 2
        subList.add(new StringRawData(trimString(line.substring(230, 234))));

        //deg 2 minor
        subList.add(new StringRawData(trimString(line.substring(234, 238))));

        //deg 2 award cert
        subList.add(new StringRawData(trimString(line.substring(238, 242))));

        //deg 2 college code
        subList.add(new StringRawData(trimString(line.substring(242, 244))));

        //degree 2  date ccyymm
        subList.add(new DateRawData(trimDate(line.substring(244, 250))));

        //degree 2 gpa
        intTemp = transform(line.substring(250,256));
        if(intTemp == null) { subList.add(new DoubleRawData(null)); }
        else { subList.add(new DoubleRawData(intTemp / 1000.0)); }

        //degree 3
        //deg 3 code awarded
        subList.add(new StringRawData(trimString(line.substring(256, 259))));

        //deg 3 major 1
        subList.add(new StringRawData(trimString(line.substring(258, 263))));

        //deg 3 major 2
        subList.add(new StringRawData(trimString(line.substring(263, 267))));

        //deg 3 minor
        subList.add(new StringRawData(trimString(line.substring(267, 271))));

        //deg 3 award cert
        subList.add(new StringRawData(trimString(line.substring(271, 275))));

        //deg 3 college code
        subList.add(new StringRawData(trimString(line.substring(275, 277))));

        //degree 3  date ccyymm
        subList.add(new DateRawData(trimDate(line.substring(277, 283))));

        //degree 3 gpa
        intTemp = transform(line.substring(283,289));
        if(intTemp == null) { subList.add(new DoubleRawData(null)); }
        else { subList.add(new DoubleRawData(intTemp / 1000.0)); }

        //primary concentration
        subList.add(new StringRawData(trimString(line.substring(289, 293))));

        //primary track
        subList.add(new StringRawData(trimString(line.substring(293, 297))));

        //academic catalog
        subList.add(new StringRawData(trimString(line.substring(298, 302))));

        //country of origin
        subList.add(new IntegerRawData(transform(line.substring(277, 283))));
                                  
        return subList;
    }
    
    /**
     * toString method.
     * @return A string representation of this class's name.
     */
    public String toString() {
        return "DISParser";
    }
    
}