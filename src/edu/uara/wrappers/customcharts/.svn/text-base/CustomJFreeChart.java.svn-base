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

import edu.uara.tableeditor.ITableObject;
import java.awt.Color;
import java.io.Serializable;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;


/**
 *CustomJFreeChart class
 * Purpose: generate charts for factbook generator
 * This is an abstract class to be extended by specific sub class to 
 * implement the specific type of charts.
 * @author Tu Hoang
 */
public abstract class CustomJFreeChart implements Serializable
{
    public static final long serialVersionUID = 1;
    /*
     * Protected member holds instance of JFreeChart object
     */
    protected JFreeChart chart = null;
    
    /*
     * CustomeChartTypes Enum used to determine chart types
     */
    public static enum CustomChartTypes
    {
       JPieChart,
       JLineChart,
       JBarChart,
       JCombinedChart; 
    }
    /**
     * Indicates which dataset is being used
     */
    public static enum DatasetTypes
    {
        PieDataset,
        XYDataset,
        CategoryDataset;
    }
    protected DatasetTypes currentDatasetType;
    protected CustomChartTypes currentChartType;

    public CustomChartTypes getCurrentChartType() {
        return currentChartType;
    }
    
    /*
     * Default constructor
     */ 
    public CustomJFreeChart()
    {
        
    }
    /**
     * write chart to png image
     */
    public void writeChartImage()
    {
        //chart.createBufferedImage(arg0, arg1)
    }
    /*
     * Getter return chart instance
     */
    public JFreeChart getChart()
    {
        return chart;
    }
    /**
     * redraw chart with updated data
     */
    public abstract void updateChart(CustomDatasetTable dsTable, ITableObject source);
    /**
     * change series color
     * @param series
     * @param color
     */
    public abstract void setSeriesColor(int series, Color color);
    /**
     * get current series color
     * @param series
     * @return
     */
    public abstract Color getSeriesColor(int series);
    /**
     * draw chart on a frame
     * @param frameTitle
     * @return
     */
    public ChartFrame drawChart(String frameTitle)
    {
        try
        {
            ChartFrame frame = new ChartFrame(frameTitle, chart);
            frame.pack();
            //frame.setVisible(true);
            return frame;
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            return null;
        }
                
    }
    /**
     * Get current dataset type that is being used
     * @return enumerator data represent the dataset type
     */
    public CustomJFreeChart.DatasetTypes getDatasetType()
    {
        return currentDatasetType;
    }
    /**
     * Get underline plot instance in JFreeChart object
     * @return subplot instance type Plot
     */
    public Plot getPlot()
    {
        return chart.getPlot();
    }
    //public abstract void generateChart();
    
    


}
