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
package edu.uara.db.structure;

import java.util.Comparator;

/**
 * This Comparator is used by the TableData class to ensure that the columns are
 * added to the table in the order the the addIntColumn and addStringColumn methods
 * were called. Since the compare method always returns 1, the next element in the
 * set is guaranteed to be "greater than" the previous one.
 * 
 * This class uses the default access modifier to prevent outside packages from
 * accessing it, as they have no need to.
 * @author jeff
 */
class TableNameComparator implements Comparator {
    /**
     * Always returns 1, no matter what. This guarantees that the columns will be
     * in the order specified.
     * @param o1
     * @param o2
     * @return
     */
    public int compare(Object o1, Object o2) {
        return 1;
    }
}
