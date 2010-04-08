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
import org.jfree.data.xy.XYDataset;

/**
 * PieChart interface
 * Purpose: provides interface to generate charts for factbook generator
 * Represents LineChart class. implemented by CustomLineChart
 * @author Tu Hoang
 */
public interface ILineChart 
{
     /**
     * Set background color for the chart
     * @param color: color for background
     */
    public void setBackGroundColor(Color color);
    /**
     * set plot background
     * @param color: plot background color
     */
    public void setPlotBackGroundColor(Color color);
    /**
     * set range gridLine color
     * @param color: gridline color
     */
    public void setYGridLineColor(Color color);
    /**
     * set domain gridline on or off
     * @param visible: true-on, false-off
     */
    public void setDomainGridLineVisible(boolean visible);
    /**
     * set range gridline on or off
     * @param visible: true-on, false-off
     */
    public void setRangeGridLineVisible(boolean visible);
    /**
     * set both domain and range gridline on or off
     * @param visible: true-on, false-off
     */
    public void setGridLineVisible(boolean visible);
    /**
     * set domain gridLine color
     * @param color: gridline color
     */
    public void setXGridLineColor(Color color);
    /**
     * Switch on and off the fill shape visibility
     * @param visibility: true -- on, false--off
     */
    public void setFillShapeVisible(boolean visibility);
    /**
     * Change color of fill shape
     * @param visibility: true -- on, false--off
     */
    public void setFillShapeColor(Color color);
    /**
     * Set range axis to only display integer
     */
    public void setTickIntegerUnit();
    /**
     * Generate regular line chart with category dataset
     * @param dataset: CategoryDataset
     */
    /**
     * Draw the chart on a frame
     */
    public ChartFrame drawChart(String frameTitle);
    /**
     * Generate General line chart with category Dataset
     * @param dataset: category dataset
     */
    public void generateLineChart(CategoryDataset dataset);
    /**
     * Generate 3D line chart with category dataset
     * @param dataset: CategoryDataset
     */
    public void generate3DLineChart(CategoryDataset dataset);
    /**
     * Generate regular line chart with XYDataset which can be used to generate
     * time series chart(multiple-line chart) 
     * @param dataset: CategoryDataset
     */
    public void generateLineChartWithXYDataset(XYDataset dataset);
    /**
     * Get current dataset type that is being used
     * @return enumerator data represent the dataset type
     */
    public CustomJFreeChart.DatasetTypes getDatasetType();
}
