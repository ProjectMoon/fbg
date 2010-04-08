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

package edu.uara.wrappers.customcharts;

import java.util.List;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * CustomJFreeChartData
 * Purpose: this class is a helper class which generates dataset 
 * for chart generation
 * @author Tu Hoang
 */
public class CustomJFreeChartData 
{
    /**
     * Default Constructor
     */
    public CustomJFreeChartData()
    {}
    /**
     * create pieDataset
     * @param category
     * @param values
     * @return
     */
    public static PieDataset createPieDataset(String[] category, double[] values)
    {
        try
        {
            DefaultPieDataset dataset = new DefaultPieDataset();
            if(category.length != values.length)
                throw new Exception("category and values are not in same length");
            
            //generate pieDataset
            for(int i = 0; i < category.length; i++)
            {
                dataset.setValue(category[i], values[i]);
            }
            
            return dataset;
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return null;
        }
    }
    /**
     * Set up group map with patterns in dataset
     * @param dataset
     * @return
     */
    public static KeyToGroupMap generateGroupedMap(CategoryDataset dataset,
                                            String[] groupKeys)
    {
        //initialize default group map
        KeyToGroupMap keyMap = new KeyToGroupMap("G0");
        try
        {           
            List l =dataset.getRowKeys();
            int gIndex = 0;
            for(String s : groupKeys)
            {
                for(int i = 0; i < l.size(); i++)
                {
                    String rowLabel = l.get(i).toString();
                    if(rowLabel.contains(s))
                    {
                        keyMap.mapKeyToGroup(rowLabel, "G"+ gIndex);
                    }
                }
                gIndex++;
            }
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return null;
        }
        return keyMap;
    }
    /**
     * Create Category dataset
     * @param rowLabels: list of series labels
     * @param columnLabels: list of category labels
     * @param values: 2D array of values
     * @return
     */
    public static CategoryDataset createCategoryDataset(
            String rowLabels[],String columnLabels[], double values[][])
    {
        try
        {
            //implementing
            if(values.length != rowLabels.length)
                throw new Exception("Invalid table series");
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for(int i = 0; i < values.length; i++)//per series
            {
                for(int c = 0; c < values[i].length; c++)
                    dataset.addValue(values[i][c], rowLabels[i], columnLabels[c]);
            }
            return dataset;
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return null;
        }
    }
    /**
     * Create XYDataset
     * @param seriesLabels
     * @param seriesValues
     * @return XYDataset for bar and line charts
     */
    public static XYDataset createXYDataset(String seriesLabels[],
                                            double seriesValues[][])
    {
        XYSeriesCollection dataset = null;
        try
        {
            //implementing
           for(int i = 0; i < seriesLabels.length; i++)
           {
                XYSeries newSeries = new XYSeries(seriesLabels[i]);
                for(int c = 0; c < seriesValues[i].length; c ++)
                {
                    newSeries.add(c, seriesValues[i][c]);
                }
                dataset.addSeries(newSeries);
                
           }
           return dataset;
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return null;
        }
    }
    
     public static XYDataset createTimeSeriesDatasetByYearRange(String seriesLabels[],
                                            double seriesValues[][],
                                            int yearStart,
                                            int yearEnd)
    {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        
        try
        {
            int numOfYear = yearEnd - yearStart;
            //implementing
           for(int i = 0; i < seriesLabels.length; i++)
           {
                TimeSeries newSeries = new TimeSeries(seriesLabels[i], Year.class);
                
                for(int c = 0; c < numOfYear; c ++)
                {
                    newSeries.add(new Year(yearStart+c), seriesValues[i][c]);
                }
                dataset.addSeries(newSeries);
           }
           return dataset;
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return null;
        }
    }
}
