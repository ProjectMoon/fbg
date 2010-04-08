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
package edu.uara.gui.datawizard;

import java.awt.BorderLayout;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

/**
 * This class allows the user to select years for upload.
 * @author jeff
 */
public class YearSelectionPanel extends JPanel {
    private JList yearList;
    
    public YearSelectionPanel() {
        super.setLayout(new BorderLayout());
        super.add(new JLabel("Year Selection"), BorderLayout.NORTH);
        yearList = new JList(getYearList(1970));
        super.add(new JScrollPane(yearList), BorderLayout.CENTER);
        
    }
    
    private static Integer[] getYearList(int endYear) {
        //get the current year and set up the year box
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int yearsToGenerate = Math.abs(year - endYear) + 1;
        int c = 0; //need separate counter to populate list in a forward manner
        
        Integer[] years = new Integer[yearsToGenerate];
        
        //This is a slightly unusual variation on the usual for syntax...
        //apparently it's legal!
        //basically we are going backwards in years while going forwards
        //in array position.
        for (int currYear = year; currYear >= endYear && c < yearsToGenerate; currYear--, c++) {
            years[c] = new Integer(currYear);
        }
        
        return years;
    }
    
    public int[] getSelectedYears() {
        if (yearList.isSelectionEmpty()) return null;
        
        int[] indices = yearList.getSelectedIndices();
        int[] data = new int[indices.length];
        ListModel model = yearList.getModel();
        
        for (int c = 0; c < indices.length; c++) {
            Integer i = (Integer)model.getElementAt(indices[c]);
            data[c] = i.intValue();
        }
        
        return data;
    }
}
