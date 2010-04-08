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
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.IntervalXYDataset;


/**
 * IBarChart interface
 * Purpose: Provide function interface to generate charts for factbook generator
 * Represents Bar chart class. implemented by CustomBarChart
 * @author Tu Hoang
 */
public interface IBarChart 
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
     * set range gridLine color
     * @param color: gridline color
     */
    public void setYGridLineColor(Color color);
    /**
     * set bar color in each series
     * @param series: series subscript
     * @param color: color to set
     */
    public void setSeriesColor(int series, Color color);
    /**
     * Specify whether to draw bar outline
     * @param draw: true to draw, false otherwise
     */
    public void setDrawBarOuntline(boolean draw);
    /**
     * Set margin--space between bars in each series
     * @param margin
     */
    public void setBarMargin(double margin);
    /**
     * set item label generator
     * @param formatStr: string contains format for lables
     */
    public void setItemLabelGenerator(String formatStr, int size);
    /**
     * Turn item labels on and off
     * @param visible: true-on/false-off
     */
    public void setItemLabelVisible(boolean visible);
    /**
     * Draw the chart on a frame
     */
    public ChartFrame drawChart(String frameTitle);
    /**
     * Generate regular barchart
     * @param dataset: categoryDataset used to render barChart. Usually
     * this is DefaultCategoryDataset type
     */
    public void generateBarChart(CategoryDataset dataset);
    /**
     * Generate 3D bar chart
     * @param dataset: categoryDataset used to render barChart. Usually
     * this is DefaultCategoryDataset type
     */
    public void generate3DBarChart(CategoryDataset dataset);
    /**
     * Generate stacked bar chart
     * @param dataset: categoryDataset used to render barChart. Usually
     * this is DefaultCategoryDataset type
     */
    public void generateStackedBarChart(CategoryDataset dataset);
    /**
     * set subcategory axis for bar chart
     * @param AxisLabel
     * @param subCategoryLabels
     */
    public void setSubCategoryAxis (String AxisLabel, String[] subCategoryLabels);
    /**
     * gererate group stacked bar chart, which contains subcategory
     * @param dataset: category dataset used to generate chart
     */
    public void generateGroupBarChart(CategoryDataset dataset,
                                             KeyToGroupMap map);
 
    /**
     * Generate 3D stacked bar chart
     * @param dataset: categoryDataset used to render barChart. Usually
     * this is DefaultCategoryDataset type
     */
    public void generate3DStackedBarChart(CategoryDataset dataset);
    /**
     * Generate Bar chart by Series of data
     * @param dataset: InteravalXYDatasetXYBarDataset is a dataset wrapper class
     * that can convert any XYDataset into an IntervalXYDataset (so that the
     * dataset can be used with renderers that require this extended interface,
     * such as the XYBarRenderer class). This class extends 
     * AbstractIntervalXYDataset.
     */
    public void generateBarChartWithXYDataset(IntervalXYDataset dataset);
    /**
     * Get current dataset type that is being used
     * @return enumerator data represent the dataset type
     */
    public CustomJFreeChart.DatasetTypes getDatasetType();
}
