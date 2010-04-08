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
 * POP parser class
 * @author paul
 */
class POPParser implements Parser {
    /**
     * The parse function inside of this class runs through the POP file
     * it pulls out the data in the corosponding collumns and puts the values
     * into a 2D list of integers and returns that list.
     * @param path: The path to the POP file
     * @return List<List<RawData>> is a 2D array containing the data that was in the DIS file
     * @throws edu.uara.exceptions.UARAException
     */
    public synchronized List<RawData> parse(String value) throws UARAException {

        char career;
        Integer year;
        Integer intTemp = null;
        ArrayList<RawData> dataLine = new ArrayList<RawData>();

        // Student ID
        dataLine.add(new StringRawData(trimString(value.substring(0,9))));
        
        // Institution ID
        dataLine.add(new IntegerRawData(transform(value.substring(9,11))));

        // Term Year
        dataLine.add(new IntegerRawData(transform(value.substring(11,15))));

        // Term Code
        dataLine.add(new IntegerRawData(transform(value.substring(15,16))));

        // Date Prepared
        dataLine.add(new DateRawData(trimDate(value.substring(16,24))));

        // Birth Date
        dataLine.add(new DateRawData(trimDate(value.substring(24,32))));

        // Gender (MHEC)
        dataLine.add(new IntegerRawData(transform(value.substring(69,70))));

        // Marital Status
        dataLine.add(new StringRawData(trimString(value.substring(33,34))));

        // Ethnicity
        dataLine.add(new IntegerRawData(transform(value.substring(70,71))));

        // Native Tongue
        dataLine.add(new StringRawData(trimString(value.substring(35,37))));

        // Religion
        dataLine.add(new StringRawData(trimString(value.substring(37,39))));

        // Citizenship
        dataLine.add(new StringRawData(trimString(value.substring(39,41))));

        // State
        //dataLine.add(new StringRawData(trimString(value.substring(41,43))));

        // County
        //dataLine.add(new StringRawData(trimString(value.substring(43,46))));

        // Geographic Origin (MHEC)
        dataLine.add(new IntegerRawData(transform(value.substring(75,78))));

        // Residency Term (MHEC)
        dataLine.add(new IntegerRawData(transform(value.substring(78,79))));

        // Handicap Type
        dataLine.add(new StringRawData(trimString(value.substring(48,49))));

        // Visa Type
        dataLine.add(new StringRawData(trimString(value.substring(49,51))));

        // Foreign Student
        dataLine.add(new StringRawData(trimString(value.substring(51,52))));

        // Co-op Student
        dataLine.add(new StringRawData(trimString(value.substring(52,53))));

        // Veteran Code
        dataLine.add(new StringRawData(trimString(value.substring(53,55))));

        // Veteran Benefit
        dataLine.add(new IntegerRawData(transform(value.substring(55,57))));

        // GI Certificate Type Term
        dataLine.add(new StringRawData(trimString(value.substring(57,58))));

        // NCAA Athletic Code
        dataLine.add(new StringRawData(trimString(value.substring(58,60))));

        // Perminent Zip
        dataLine.add(new IntegerRawData(transform(value.substring(60,69))));

        // Program Code
        dataLine.add(new IntegerRawData(transform(value.substring(79,85))));

        // Degree Saught
        dataLine.add(new IntegerRawData(transform(value.substring(85,87))));

        // First-Time Status
        dataLine.add(new IntegerRawData(transform(value.substring(87,88))));

        // Credit Hour Load
        intTemp = transform(value.substring(88,92));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Current Status
        dataLine.add(new IntegerRawData(transform(value.substring(92,93))));

        // Student Level
        dataLine.add(new IntegerRawData(transform(value.substring(93,95))));

        // Original Enrollment Year
        dataLine.add(new IntegerRawData(transform(value.substring(95,99))));

        // Original Enrollment Term
        dataLine.add(new IntegerRawData(transform(value.substring(99,100))));

        // Original Enrollment Type
        dataLine.add(new StringRawData(trimString(value.substring(100,101))));

        // Last ReAdmit Year
        dataLine.add(new IntegerRawData(transform(value.substring(101,105))));

        // Last ReAdmit Term
        dataLine.add(new IntegerRawData(transform(value.substring(105,106))));

        // Last ReAdmit Type
        dataLine.add(new StringRawData(trimString(value.substring(106,107))));

        // Institutional Admit Type
        dataLine.add(new StringRawData(trimString(value.substring(107,109))));

        // Last Institution Type
        dataLine.add(new StringRawData(trimString(value.substring(109,110))));

        // Last School Code
        dataLine.add(new StringRawData(trimString(value.substring(111,118))));

        // Last Grade Scheme
        dataLine.add(new IntegerRawData(transform(value.substring(118,119))));

        // Last GPA
        intTemp = transform(value.substring(119,125));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 1000.0)); }

        // Last Degree Saught
        dataLine.add(new StringRawData(trimString(value.substring(125,128))));

        // Last Degree Recieved
        dataLine.add(new StringRawData(trimString(value.substring(128,129))));

        // Last End Date
        dataLine.add(new DateRawData(trimDate(value.substring(129,135))));

        // SAT Verbal
        dataLine.add(new IntegerRawData(transform(value.substring(135,139))));

        // SAT Math
        dataLine.add(new IntegerRawData(transform(value.substring(139,143))));

        // SAT Reading
        dataLine.add(new IntegerRawData(transform(value.substring(143,147))));

        // SAT Vocab
        dataLine.add(new IntegerRawData(transform(value.substring(147,151))));

        // TSWE Score
        dataLine.add(new IntegerRawData(transform(value.substring(151,155))));

        // Highest GREV
        dataLine.add(new IntegerRawData(transform(value.substring(155,159))));

        // Highest GREQ
        dataLine.add(new IntegerRawData(transform(value.substring(159,163))));

        // Highest GREA
        dataLine.add(new IntegerRawData(transform(value.substring(163,167))));

        // Highest LSAT
        dataLine.add(new IntegerRawData(transform(value.substring(167,171))));

        // Highest GMAT
        dataLine.add(new IntegerRawData(transform(value.substring(171,175))));

        // Highest TOEFL
        dataLine.add(new IntegerRawData(transform(value.substring(175,179))));

        // Highest NTEC
        dataLine.add(new IntegerRawData(transform(value.substring(179,183))));

        // Highest NTEG
        dataLine.add(new IntegerRawData(transform(value.substring(183,187))));

        // Highest NTEP
        dataLine.add(new IntegerRawData(transform(value.substring(187,191))));

        // Highest NJPRE Reading
        dataLine.add(new IntegerRawData(transform(value.substring(191,195))));

        // Highest NJPRE Sentence
        dataLine.add(new IntegerRawData(transform(value.substring(195,199))));

        // Highest NJPRE Essay
        dataLine.add(new IntegerRawData(transform(value.substring(199,203))));

        // Highest NJPRE Comp
        dataLine.add(new IntegerRawData(transform(value.substring(203,207))));

        // Highest NJPRE Algebra
        dataLine.add(new IntegerRawData(transform(value.substring(207,211))));

        // Highest NJPST Reading
        dataLine.add(new IntegerRawData(transform(value.substring(211,215))));

        // Highest NJPST Sentence
        dataLine.add(new IntegerRawData(transform(value.substring(215,219))));

        // Highest NJPST Essay
        dataLine.add(new IntegerRawData(transform(value.substring(219,223))));

        // Highest NJPST Comp
        dataLine.add(new IntegerRawData(transform(value.substring(223,227))));

        // Highest NJPST Algebra
        dataLine.add(new IntegerRawData(transform(value.substring(227,231))));

        // MHEC Student ID
        //dataLine.add(new StringRawData(trimString(value.substring(253,262))));

        // Remedial
        dataLine.add(new StringRawData(trimString(value.substring(262,263))));

        // Previous Term Year
        dataLine.add(new IntegerRawData(transform(value.substring(263,267))));

        // Previous Term
        dataLine.add(new IntegerRawData(transform(value.substring(267,268))));

        // Previous Attempted Cumulative Hours
        intTemp = transform(value.substring(268,275));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Previous Earned Cumulative Hours
        intTemp = transform(value.substring(275,282));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Previous Earned Quality Hours
        intTemp = transform(value.substring(282,289));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Previous Earned Quality Points
        intTemp = transform(value.substring(289,296));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Previous Cumulative GPA
        intTemp = transform(value.substring(296,302));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 1000.0)); }

        // FullPart
        dataLine.add(new StringRawData(trimString(value.substring(302,303))));

        // Career
        dataLine.add(new StringRawData(trimString(value.substring(303,305))));

        // College academic program
        dataLine.add(new StringRawData(trimString(value.substring(305,307))));

        // Classification
        dataLine.add(new StringRawData(trimString(value.substring(307,310))));

        // Primary Degree
        dataLine.add(new StringRawData(trimString(value.substring(310,313))));

        // Major One
        dataLine.add(new StringRawData(trimString(value.substring(313,317))));

        // Major Two
        dataLine.add(new StringRawData(trimString(value.substring(317,321))));

        // Primary Concentration
        dataLine.add(new StringRawData(trimString(value.substring(321,325))));

        // Secondary Concentration
        dataLine.add(new StringRawData(trimString(value.substring(325,329))));

        // Primary Minor
        dataLine.add(new StringRawData(trimString(value.substring(329,333))));

        // Certificate
        dataLine.add(new StringRawData(trimString(value.substring(333,337))));

        // Special Program
        dataLine.add(new StringRawData(trimString(value.substring(337,340))));

        // Registration Type
        dataLine.add(new StringRawData(trimString(value.substring(340,343))));

        // Room Type
        dataLine.add(new StringRawData(trimString(value.substring(343,344))));

        // Dorm Code
        dataLine.add(new StringRawData(trimString(value.substring(344,349))));

        // Attempted Hours
        intTemp = transform(value.substring(349,355));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Earned Hours
        intTemp = transform(value.substring(355,361));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Earned Quality
        intTemp = transform(value.substring(361,367));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Quality Points
        intTemp = transform(value.substring(367,374));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // GPA
        intTemp = transform(value.substring(374,380));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 1000.0)); }

        // Deans List
        dataLine.add(new StringRawData(trimString(value.substring(380,382))));

        // Academic Action
        dataLine.add(new StringRawData(trimString(value.substring(382,384))));

        // Withdraw Code
        dataLine.add(new StringRawData(trimString(value.substring(384,385))));

        // Withdraw Date
        dataLine.add(new DateRawData(trimDate(value.substring(385,393))));

        // Graduation Code
        dataLine.add(new StringRawData(trimString(value.substring(393,394))));

        // Post Grades
        dataLine.add(new StringRawData(trimString(value.substring(394,397))));

        // Cumulative Term Year
        dataLine.add(new IntegerRawData(transform(value.substring(397,401))));

        // Cumulative Term
        dataLine.add(new IntegerRawData(transform(value.substring(401,402))));

        // Cumulative Attempted Hours
        intTemp = transform(value.substring(402,409));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Cumulative Earned Hours
        intTemp = transform(value.substring(409,416));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Cumulative Earned Quality Hours
        intTemp = transform(value.substring(416,423));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Cumulative Quality Points
        intTemp = transform(value.substring(423,430));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Cumulative GPA
        intTemp = transform(value.substring(430,436));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 1000.0)); }

        // Total Hours Transfered
        intTemp = transform(value.substring(436,443));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Total Hours Toward Graduation
        intTemp = transform(value.substring(443,450));
        if(intTemp == null) { dataLine.add(new DoubleRawData(null)); }
        else { dataLine.add(new DoubleRawData(intTemp / 100.0)); }

        // Financial Aid Award Year
        dataLine.add(new IntegerRawData(transform(value.substring(450,452))));

        // Financial Aid Award Period
        dataLine.add(new StringRawData(trimString(value.substring(452,453))));

        // Financial Aid Application
        dataLine.add(new StringRawData(trimString(value.substring(453,454))));

        // Financial Aid Total Budget
        dataLine.add(new IntegerRawData(transform(value.substring(454,459))));

        // Financial Aid Total Need
        dataLine.add(new IntegerRawData(transform(value.substring(459,464))));

        // Financial Aid Family Contribution
        dataLine.add(new IntegerRawData(transform(value.substring(464,469))));

        // Federal Income Student
        dataLine.add(new IntegerRawData(transform(value.substring(469,476))));

        // Federal Income Parent
        dataLine.add(new IntegerRawData(transform(value.substring(476,483))));

        // Dependancy Status
        dataLine.add(new StringRawData(trimString(value.substring(483,484))));

        // Financial Aid Total Awarded
        dataLine.add(new IntegerRawData(transform(value.substring(484,489))));

        // Grant Amount
        dataLine.add(new IntegerRawData(transform(value.substring(489,494))));

        // Loan Amount
        dataLine.add(new IntegerRawData(transform(value.substring(494,499))));

        // Job Amount
        dataLine.add(new IntegerRawData(transform(value.substring(499,504))));

        // Outside Aid Amount
        dataLine.add(new IntegerRawData(transform(value.substring(504,509))));

        // Institution Aid Amount
        dataLine.add(new IntegerRawData(transform(value.substring(509,514))));

        // Student Aid State Amount
        dataLine.add(new IntegerRawData(transform(value.substring(514,519))));

        // Student Aid Federal Amount
        dataLine.add(new IntegerRawData(transform(value.substring(519,524))));

        // Source Unknown Amount
        dataLine.add(new IntegerRawData(transform(value.substring(524,529))));

        // Campus Based Aid Amount
        dataLine.add(new IntegerRawData(transform(value.substring(529,534))));

        // Minority Aid Amount
        dataLine.add(new IntegerRawData(transform(value.substring(534,539))));

        // Athletic Aid Amount
        dataLine.add(new IntegerRawData(transform(value.substring(539,544))));

        // Tuition Waiver Amount
        dataLine.add(new IntegerRawData(transform(value.substring(544,549))));

        // Pell Amount
        dataLine.add(new IntegerRawData(transform(value.substring(549,554))));

        // GSL Amount
        dataLine.add(new IntegerRawData(transform(value.substring(554,559))));

        // Other Aid Amount
        dataLine.add(new IntegerRawData(transform(value.substring(559,564))));

        // EMPLD
        dataLine.add(new StringRawData(trimString(value.substring(568,575))));

        // Fall Sport Code
        dataLine.add(new StringRawData(trimString(value.substring(575,578))));

        // Winter Sport Code
        dataLine.add(new StringRawData(trimString(value.substring(578,581))));

        // Spring Sport Code
        dataLine.add(new StringRawData(trimString(value.substring(581,584))));

        // Sem AA GI Cert Term
        dataLine.add(new StringRawData(trimString(value.substring(590,595))));

        // Sem AA GI Cert Type
        dataLine.add(new StringRawData(trimString(value.substring(595,596))));

        // Sem Current term Primary Track
        dataLine.add(new StringRawData(trimString(value.substring(596,600))));


        return dataLine;
    }
    
    /**
     * toString method.
     * @return A string representation of this class's name.
     */
    public String toString() {
        return "POPParser";
    }
    
}