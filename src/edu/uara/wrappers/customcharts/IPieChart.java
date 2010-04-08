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

import java.awt.Color;

import org.jfree.chart.ChartFrame;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.TableOrder;

/**
 * PieChart interface
 * Purpose: generate charts for factbook generator
 * Represents Pie chart class. implemented by CustomPieChart
 * @author Tu Hoang
 */
public interface IPieChart 
{
    //1ntergalactic43
    //Functions to change chart properties
     /**
     * set plot background
     * @param color: plot background color
     */
    public void setPlotBackGroundColor(Color color);
     /**
     * Function to set change color of pie section
     * sectionName-- section to to change color
     * color-- color used to change section color
     */
     public void setSectionPaint(String sectionName, Color color);
    /**
     * Function to set label and label format
     * @param labelFormat: is to specify how to display label on the chart
     *              the format is using MessageFormat Subtistution
     *              Code: Description (as in PieDataset)
     *               {0} The item key.
     *               {1} The item value.
     *               {2} The item value as a percentage of the total.
      * @param numberFormat is used to format number display in the chart label
      * @param percentageFormat is used to format the percentage in the label
      */
    public void setCustomLabelFormat(String labelFormat, 
                         String numberFormat,
                         String percentageFormat);
     /**
     * set label format
     * @param index
     */
    public void setLabelFormat(int index);
    /**
     * Function to set exploded parts of the chart
     * @param key: category to be exploded from the chart
     * @param percent: percentage of the radius of the pie chart
     */
    public void setExplodePercent(String key, double percent);
    /**
     * Function to set out line visibility
     * @param outLine: boolean value to set visibility
     */
    public void setOutlineVisibility(boolean outLine);
    /**
     * Function to set ability to ignore 0 or null values in dataset
     * @param ignore: specify whether to ignore Null or Zero values
     */
    public void setIgnoreNullOrZeroValues(boolean ignore);
    /**
     * Function to set background color for the chart
     * @param color
     */
    public void setBackGroundColor(Color color);
    /**
     * Function to drawchart on a Frame
     * @param frameTitle
     */
    public ChartFrame drawChart(String frameTitle);
    //Functions for chart generation
    void generatePieChart(PieDataset dataset,
                           String seriesName,
                           TableOrder order);
    void generate3DPieChart(PieDataset dataset,
                           String seriesName,
                           TableOrder order);
    void generateMultiplePieChart(CategoryDataset dataset, TableOrder order);
    void generateMultiple3DPieChart(CategoryDataset dataset, TableOrder order);
    /**
     * Get current dataset type that is being used
     * @return enumerator data represent the dataset type
     */
    public CustomJFreeChart.DatasetTypes getDatasetType();
}
