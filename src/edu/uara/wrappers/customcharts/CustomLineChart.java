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

import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.TableOrder;
/**
 * CustomLineChart
 * Purpose: generate charts for factbook generator
 * Represents LineChart class
 * @author Tu Hoang
 */
public class CustomLineChart extends CustomJFreeChart implements ILineChart
{   
    
    private String title;
    private boolean legend;
    private String domainAxisLabel, rangeAxisLabel;
    private PlotOrientation orientation;
    private CategoryDataset ds = null;
    
    
    /*
     * Constructor
     */
    public CustomLineChart(String chartTitle, 
                          String domainLabel,
                          String rangeLabel,
                          PlotOrientation chartOrientation,
                          boolean chartLegend)
    {
        title = chartTitle;
        domainAxisLabel = domainLabel;
        rangeAxisLabel = rangeLabel;
        orientation = chartOrientation;
        legend = chartLegend;
        this.currentChartType = CustomChartTypes.JLineChart;
    }

    public CategoryDataset getDataset() {
        return ds;
    }
    
    @Override
    public void updateChart(CustomDatasetTable dsTable, ITableObject source)
    {
        if(ds != null)//
        {
            TableOrder tableOrder = TableOrder.BY_ROW;
            DefaultCategoryDataset dataset = (DefaultCategoryDataset)ds;
            
            List rows = dataset.getRowKeys();
            List cols = dataset.getColumnKeys();
            
            String[] rowLabels = dsTable.getRowLabels();
            String[] columnLabels = dsTable.getColumnLabels(source);
            
            for(int i = 0; i < rows.size(); i++ )
            {
                if(!rows.get(i).toString().equals(rowLabels))
                {
                    tableOrder = TableOrder.BY_COLUMN;
                    break;
                }
            }
                
            if(tableOrder == TableOrder.BY_ROW)
            { 
                double[][] values = dsTable.getTableContentAsValue(source);
                for(int r = 0; r < rowLabels.length; r++)
                {
                    for(int c = 0; c < columnLabels.length; c++)
                    {
                        dataset.setValue(values[r][c],
                                        rowLabels[r], columnLabels[c]);

                    }
                }
            
            }
            else
            {
                double[][] values = dsTable.getTableContentAsValueTranspose(source);
                for(int r = 0; r < rows.size(); r++)
                {
                    for(int c = 0; c < cols.size(); c++)
                    {
                        dataset.setValue(values[r][c],
                                        rows.get(r).toString(),
                                        cols.get(c).toString());

                    }
                }
            }
        }
    }
    @Override
    public void setBackGroundColor(Color color)
    {
        chart.setBackgroundPaint(color);
    }
    /**
     * set plot background
     * @param color: plot background color
     */
    @Override
    public void setPlotBackGroundColor(Color color)
    {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(color);
    }
    /**
     * set range gridLine color
     * @param color: gridline color
     */
    @Override
    public void setYGridLineColor(Color color)
    {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRangeGridlinePaint(color);
    }
    /**
     * set domain gridline on or off
     * @param visible: true-on, false-off
     */
    @Override
    public void setDomainGridLineVisible(boolean visible)
    {
        CategoryPlot plot  = (CategoryPlot)chart.getPlot();
        plot.setDomainGridlinesVisible(visible);
    }
    /**
     * set range gridline on or off
     * @param visible: true-on, false-off
     */
    @Override
    public void setRangeGridLineVisible(boolean visible)
    {
        CategoryPlot plot  = (CategoryPlot)chart.getPlot();
        plot.setRangeGridlinesVisible(visible);
    }
    /**
     * set both domain and range gridline on or off
     * @param visible: true-on, false-off
     */
    @Override
    public void setGridLineVisible(boolean visible)
    {
        CategoryPlot plot  = (CategoryPlot)chart.getPlot();
        plot.setDomainGridlinesVisible(visible);
        plot.setRangeGridlinesVisible(visible);
    }
    /**
     * set domain gridLine color
     * @param color: gridline color
     */
    @Override
    public void setXGridLineColor(Color color)
    {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDomainGridlinePaint(color);
    }
     /**
     * Switch on and off the fill shape visibility
     * @param visibility: true -- on, false--off
     */
    @Override
    public void setFillShapeVisible(boolean visibility)
    {
        // customise the renderer...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        LineAndShapeRenderer renderer
        = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(visibility);
        renderer.setDrawOutlines(visibility);
        renderer.setUseFillPaint(visibility);
        
    }
    /**
     * Change color of fill shape
     * @param visibility: true -- on, false--off
     */
    @Override
    public void setFillShapeColor(Color color)
    {
        // customise the renderer...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        LineAndShapeRenderer renderer
        = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseFillPaint(color);
        
    }
    /**
     * get series color
     * @param series: index of series
     * @return: color of provided series
     */
    @Override
    public Color getSeriesColor(int series)
    {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        return (Color) renderer.getSeriesPaint(series);
    }
    /**
     * set series color
     * @param series: index of series in dataset
     * @param color: color used to set to the series
     */
    @Override
    public void setSeriesColor(int series, Color color)
    {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(series, color);
    }
    /**
     * Set range axis to only display integer
     */
    @Override
    public void setTickIntegerUnit()
    {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    }
    
    /**
     * Generate regular line chart with category dataset
     * @param dataset: CategoryDataset
     */
    @Override
    public void generateLineChart(CategoryDataset dataset)
    {
        ds = dataset;//store dataset reference to do update
        try
        {
            if(dataset == null)
                throw new Exception("No dataset provided");
            chart = ChartFactory.createLineChart(
                title, // chart title
                domainAxisLabel, // domain axis label
                rangeAxisLabel, // range axis label
                dataset, // data
                orientation, // orientation
                legend, // include legend
                false, // no tooltips
                false // URLs?
            );
            this.currentDatasetType = DatasetTypes.CategoryDataset;
        }
        catch(Exception ex)
        {
            //handle exception
        }
    }
    
     /**
     * Generate 3D line chart with category dataset
     * @param dataset: CategoryDataset
     */
    @Override
    public void generate3DLineChart(CategoryDataset dataset)
    {
        ds = dataset;//store dataset reference to do update
        try
        {
            if(dataset == null)
                throw new Exception("No dataset provided");
            chart = ChartFactory.createLineChart3D(
                title, // chart title
                domainAxisLabel, // domain axis label
                rangeAxisLabel, // range axis label
                dataset, // data
                orientation, // orientation
                legend, // include legend
                false, // no tooltips
                false // URLs?
            );
            this.currentDatasetType = DatasetTypes.CategoryDataset;
        }
        catch(Exception ex)
        {
            //handle exception
        }
    }
    /**
     * Generate regular line chart with XYDataset which can be used to generate
     * time series chart(multiple-line chart) 
     * @param dataset: CategoryDataset
     */
    @Override
    public void generateLineChartWithXYDataset(XYDataset dataset)
    {
        
        try
        {
            if(dataset == null)
                throw new Exception("No dataset provided");
            chart = ChartFactory.createXYLineChart(
                title, // chart title
                domainAxisLabel, // domain axis label
                rangeAxisLabel, // range axis label
                dataset, // data
                orientation, // orientation
                legend, // include legend
                false, // no tooltips
                false // URLs?
            );
            this.currentDatasetType = DatasetTypes.XYDataset;
        }
        catch(Exception ex)
        {
            //handle exception
        }
    }
    
    public static void main(String[] args) 
    {
        // create a dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "Row 1", "Column 1");
        dataset.addValue(5.0, "Row 1", "Column 2");
        dataset.addValue(3.0, "Row 1", "Column 3");
        dataset.addValue(2.0, "Row 2", "Column 1");
        dataset.addValue(3.0, "Row 2", "Column 2");
        dataset.addValue(2.0, "Row 2", "Column 3");
        // create a chart...
        ILineChart c = new CustomLineChart("Test Line chart", "Domain", "Range", 
                PlotOrientation.VERTICAL, true);
        c.generateLineChart(dataset);
        c.setBackGroundColor(Color.white);
        c.setPlotBackGroundColor(Color.white);
        c.drawChart("Chart Editor");
    }
}
