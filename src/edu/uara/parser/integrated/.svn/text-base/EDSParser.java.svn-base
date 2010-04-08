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
 * EDS parser class
 * @author tu
 */
class EDSParser implements Parser {
     /**
     * The parse function inside of this class runs through the EDS file
     * it pulls out the data in the corosponding collumns and puts the values
     * into a 2D list of RawDatas and returns that list.
     * @param path: The path to the EDS file
     * @return List<List<RawData>> is a 2D array containing the data that was in the EDS file
     * @throws edu.uara.exceptions.UARAException
     */
    public synchronized List<RawData> parse(String line) throws UARAException
    {
        ArrayList<RawData> subList = new ArrayList<RawData>();
        //read year(0-4)
        RawData temp = new IntegerRawData(transform(line.substring(0, 4)));
        subList.add(temp);

        //read FICE (4-9)
        temp = new IntegerRawData(transform(line.substring(4, 10)));
        subList.add(temp);

        //read campuscode blank(10)
        subList.add(new StringRawData(trimString(line.substring(10,11))));

        //read employeeID(11-19)
        subList.add(new StringRawData(trimString(line.substring(11,20))));
       // temp = new IntegerRawData(transform(line.substring(11, 20)));
        //subList.add(temp);

        //blank(20)
        //Read sex(21)
        temp = new IntegerRawData(transform(line.substring(21,22)));
        subList.add(temp);

        //Read racial/ethinic(22)
        temp = new IntegerRawData(transform(line.substring(22,23)));
        subList.add(temp);

        //Read birthyear(23-26)
        temp = new IntegerRawData(transform(line.substring(23, 27)));
        subList.add(temp);

        //Read full/parttime status(27)
        temp = new IntegerRawData(transform(line.substring(27, 28)));
        subList.add(temp);

        //Read date of initial Employ(28-31)
        subList.add(new DateRawData(trimDate(line.substring(28,32))));

        //Date Employed in Curret Facutlt Rank/position (32-35)
        subList.add(new DateRawData(trimDate(line.substring(32,36))));

        //Blank (36)
        //Read Principal Occupational Assignment(37-38)
        temp = new IntegerRawData(transform(line.substring(37, 39)));
        subList.add(temp);

        //Academic Rank(39)
        temp = new IntegerRawData(transform(line.substring(39,40)));
        subList.add(temp);

        //Read Academic tenture status(40)
        temp = new IntegerRawData(transform(line.substring(40, 41)));
        subList.add(temp);

        //Read Program Assignment(41-42)
        temp = new IntegerRawData(transform(line.substring(41, 43)));
        subList.add(temp);

        //Read Faculty Program Assignment(43-48)
        temp = new IntegerRawData(transform(line.substring(43, 49)));
        subList.add(temp);

        //Read Contract(49)
        temp = new IntegerRawData(transform(line.substring(49, 50)));
        subList.add(temp);

        //Read Salary/wage(50-55)
        temp = new IntegerRawData(transform(line.substring(50, 56)));
        subList.add(temp);

        //Highest Degree Attained(56)
        temp = new IntegerRawData(transform(line.substring(56, 57)));
        subList.add(temp);

        //Read Promotion(57)
        temp = new IntegerRawData(transform(line.substring(57, 58)));
        subList.add(temp);

        //Appointment status(58)
        temp = new IntegerRawData(transform(line.substring(58, 59)));
        subList.add(temp);

        //Citizenship (59)
        temp = new IntegerRawData(transform(line.substring(59, 60)));
        subList.add(temp);

        //Blank (60,79)


        return subList;
    }
    
    /**
     * toString method.
     * @return A string representation of this class's name.
     */
    public String toString() {
        return "EDSParser";
    }
}