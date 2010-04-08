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

import java.awt.Font;
import java.text.DecimalFormat;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.util.TableOrder;
/**
 * CustomBarChart
 * Purpose: generate charts for factbook generator
 * Represents bar chart class
 * @author Tu Hoang
 */

public class CustomBarChart extends CustomJFreeChart implements IBarChart
{   
    private String title;
    private boolean legend;
    private String domainAxisLabel, rangeAxisLabel;
    private PlotOrientation orientation;
    private CategoryDataset ds = null;
    
    /**
     * Constructor
     */
    public CustomBarChart(String chartTitle, 
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
        this.currentChartType = CustomChartTypes.JBarChart;
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
    /**
     * Set background color for the chart
     * @param color: color for background
     */
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
     * set bar color in each series
     * @param rowName: bar name in each series
     * @param color: color to set
     */
    @Override
    public void setSeriesColor(int series, Color color)
    {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(series, color);
        
        
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
        CategoryItemRenderer renderer = plot.getRenderer();
        return (Color) renderer.getSeriesPaint(series);
    }
    /**
     * Specify whether to draw bar outline
     * @param draw: true to draw, false otherwise
     */
    @Override
    public void setDrawBarOuntline(boolean draw)
    {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(draw);
    }
    /**
     * Set margin--space between bars in each series
     * @param margin
     */
    @Override
    public void setBarMargin(double margin)
    {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(margin);
    }
    /**
     * set item label generator
     * @param formatStr: string contains format for lables
     */
    @Override
    public void setItemLabelGenerator(String formatStr, int size)
    {
        if(formatStr.contains("%"))
        {
            formatStr = formatStr.replace("%", "'%'");
        }
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setBaseItemLabelFont(new Font("Calibri", Font.BOLD, size));
        CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator(
        "{2}", new DecimalFormat(formatStr));
        renderer.setBaseItemLabelGenerator(generator);
    }
    /**
     * Turn item labels on and off
     * @param visible: true-on/false-off
     */
    @Override
    public void setItemLabelVisible(boolean visible)
    {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setBaseItemLabelsVisible(true);
    }
    @Override
    public void generateBarChart(CategoryDataset dataset)
    {
        this.ds = dataset;//store reference to dataset to do update
        try
        {
            if(dataset == null)
                throw new Exception("No dataset provided");
            chart = ChartFactory.createBarChart(
                title, // chart title
                domainAxisLabel, // domain axis label
                rangeAxisLabel, // range axis label
                dataset, // data
                orientation, // orientation
                legend, // include legend
                false, // no tooltips
                false // URLs?
            );
            currentDatasetType = DatasetTypes.CategoryDataset;
        }
        catch(Exception ex)
        {
            //handle exception
        }
    }
    @Override
    public void generate3DBarChart(CategoryDataset dataset)
    {
        this.ds = dataset;//store reference to dataset to do update
        try
        {
            if(dataset == null)
                throw new Exception("No dataset provided");
            chart = ChartFactory.createBarChart3D(
                title, // chart title
                domainAxisLabel, // domain axis label
                rangeAxisLabel, // range axis label
                dataset, // data
                orientation, // orientation
                legend, // include legend
                false, // no tooltips
                false // URLs?
            );
            currentDatasetType = DatasetTypes.CategoryDataset;
        }
        catch(Exception ex)
        {
            //handle exception
        }
    }
     
    @Override
    public void generateStackedBarChart(CategoryDataset dataset)
    {
        this.ds = dataset;//store reference to dataset to do update
        try
        {
            if(dataset == null)
                throw new Exception("No dataset provided");
            chart = ChartFactory.createStackedBarChart(
                title, // chart title
                domainAxisLabel, // domain axis label
                rangeAxisLabel, // range axis label
                dataset, // data
                orientation, // orientation
                legend, // include legend
                false, // no tooltips
                false // URLs?
            );
            currentDatasetType = DatasetTypes.CategoryDataset;
        }
        catch(Exception ex)
        {
            System.out.println("Error Generating stacked bar chart :" +
                                ex.getMessage());
        }
    }
    /**
     * Function to set subcategory axis after calling to
     * generateGroupedStackedBarChart
     * @param AxisLabel: label for the whole axis
     * @param subCategoryLabels: sub category axis labels
     */
    @Override
    public void setSubCategoryAxis (String AxisLabel, String[] subCategoryLabels)
    {
        try
        {
            SubCategoryAxis domainAxis = new SubCategoryAxis(AxisLabel);
            domainAxis.setCategoryMargin(0.1);
            for(String s : subCategoryLabels)//set labels for each sub category
                domainAxis.addSubCategory(s);

            //assign to plot
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.setDomainAxis(domainAxis);
        }
        catch(Exception ex)
        {
            System.out.println("Error set domain axis :" +
                                ex.getMessage());
        }
        
    }
    @Override
    public void generateGroupBarChart(CategoryDataset dataset,
                                             KeyToGroupMap map)
    {
        this.ds = dataset;//store reference to dataset to do update
        try
        {
            generateStackedBarChart(dataset);//generate a stacked bar chart first
            GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
            //set group series
            renderer.setSeriesToGroupMap(map);
            
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.setRenderer(renderer);
            //plot.setFixedLegendItems(generateItemCollection());
        }
        catch(Exception ex)
        {
            System.out.println("Error Generating grouped stacked bar chart :" +
                                ex.getMessage());
        }
    }
//    private  LegendItemCollection generateItemCollection()
//    {
//        RandomColor colorGenerator = new RandomColor();
//        LegendItemCollection legends = new LegendItemCollection();
//        legends.add(new LegenItem("", colorGenerator.randomColor()));
//    }
    @Override
    public void generate3DStackedBarChart(CategoryDataset dataset)
    {
        this.ds = dataset;//store reference to dataset to do update
        try
        {
            if(dataset == null)
                throw new Exception("No dataset provided");
            chart = ChartFactory.createStackedBarChart3D(
                title, // chart title
                domainAxisLabel, // domain axis label
                rangeAxisLabel, // range axis label
                dataset, // data
                orientation, // orientation
                legend, // include legend
                false, // no tooltips
                false // URLs?
            );
            currentDatasetType = DatasetTypes.CategoryDataset;
        }
        catch(Exception ex)
        {
            //handle exception
        }
    }
    
    @Override
    public void generateBarChartWithXYDataset(IntervalXYDataset dataset)
    {
        
        try
        {
            if(dataset == null)
                throw new Exception("No dataset provided");
            chart = ChartFactory.createXYBarChart(
                title, // chart title
                domainAxisLabel, // domain axis label
                true, //showing date
                rangeAxisLabel, // range axis label
                dataset, // data
                orientation, // orientation
                legend, // include legend
                false, // no tooltips
                false // URLs?
            );
            currentDatasetType = DatasetTypes.XYDataset;
        }
        catch(Exception ex)
        {
            //handle exception
        }
    }
    
   /* Testing main function
*
* @param args ignored.
*/
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
        IBarChart c = new CustomBarChart("Test barchart", "Domain", "Range", 
                PlotOrientation.VERTICAL, true);
        c.generate3DStackedBarChart(dataset);
        c.drawChart("Chart Editor");
    }

//    public void generate3DStackedBarChart(DefaultCategoryDataset dataset) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
}
