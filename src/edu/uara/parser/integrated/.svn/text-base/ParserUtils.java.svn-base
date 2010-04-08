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

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Utility class of helper methods for the parsers and other
 * related packages. It contains methods to automatically trim data and handle
 * blank values, as well as convert dates from the convoluted data file formats to
 * something standardized for the program.
 * @author jeff
 */
public class ParserUtils {
        /**
         * Takes care of transforming an individual piece of data from a string
         * to an int.
         * @param data - the string representation of this piece of data.
         * @return An Integer representation of this piece of data.
         */
        public static Integer transform(String data) {
            //i implemented this because it seems that not all of the space in each
            //data field is necessarily used up. --Jeff
            String trimmedData = data.trim();
            //if (trimmedData.length() != data.length())
              //  System.err.println("transform() warning: data \"" + data + "\" was trimmed!");
            
            try {
                int i = Integer.parseInt(trimmedData);
                return new Integer(i);
            }
            catch (NumberFormatException e) {
                //System.err.println("transform() warning: the data was blank, substituting null.");
                return null;
            }
        }

        /**
         * Trims string data and if it is blank returns null
         * @param data
         * @return Trimmed string, or null if its blank
         */
        public static String trimString(String data) {
            String trimmedData = data.trim();
            //if (trimmedData.length() != data.length())
           //     System.err.println("trimString() warning: data\""+ data +"\" was trimmed!");
            
            if(trimmedData.length() == 0)
                return null;
            else
                return trimmedData;
        }
        
        /**
         * Takes care of changing the dates found in the different data files
         * to a standard java.sql.Date. The method currently takes the following
         * date formats: "yyyyMMdd", "yyyMM", "MMyy", where "M" is month, "y" is year,
         * and "d" is day.
         * @param data - The String containing one of the 
         * @return A standard-format java.sql.Date
         */
        public static Date trimDate(String data) {
            String trimmedData=data.trim();
            if(trimmedData.length() == 0) {
                return null;
            }
            else {
                SimpleDateFormat sdf = null;
                switch(trimmedData.length()) {
                    case 8: sdf = new SimpleDateFormat("yyyyMMdd"); break;
                    case 6: sdf = new SimpleDateFormat("yyyyMM"); break;
                    case 4: sdf = new SimpleDateFormat("MMyy"); break;
                    default: return null;
                }
                try {
                    return new Date(sdf.parse(trimmedData).getTime());
                }
                catch(ParseException e) {
                    return null;
                }
            }
        }       
}
