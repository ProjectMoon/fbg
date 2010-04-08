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
import java.text.DecimalFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/*
 * CustomPieChart
 * generates charts for factbook generator
 * Represent Pie chart class
 * @author Tu Hoang
 */
import org.jfree.util.TableOrder;
public class CustomPieChart extends CustomJFreeChart implements IPieChart
{
    private String title;
    private boolean legend;
    private CategoryDataset ds = null;
    private PieDataset pieDs = null;
    private String singlePieDatasetSeries = null;
    private TableOrder tableOrder = null;
    /**
     * Constructor 
     * ChartTilte: title of the chart
     * chartLegend: true -- display legend, false -- hide legend
     */ 
    public CustomPieChart(String chartTitle, boolean hasLegend)
    {
        super();      
        title = chartTitle;
        legend = hasLegend;
        this.currentChartType = CustomChartTypes.JPieChart;
    }
    
    @Override
    public void updateChart(CustomDatasetTable dsTable, ITableObject source)
    {
        if(ds != null)//update multipiedataset
        {
            DefaultCategoryDataset dataset = (DefaultCategoryDataset)ds;
            String[] rowLabels = dsTable.getRowLabels();
            String[] columnLabels = dsTable.getColumnLabels(source);
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
        else if(pieDs != null)//single pie dataset
        {
            DefaultPieDataset pieDataset = (DefaultPieDataset)pieDs;
            String[] rowLabels = dsTable.getRowLabels();
            String[] columnLabels = dsTable.getColumnLabels(source);
            double[][] values = dsTable.getTableContentAsValue(source);
            if(tableOrder == TableOrder.BY_ROW)
            {
                int r;//index of the series in the main dataset
                for(r = 0; r < rowLabels.length; r++)
                {
                    if(rowLabels[r].equals(this.singlePieDatasetSeries));
                        break;
                }
                for(int c = 0; c < columnLabels.length; c++)
                {
                    pieDataset.setValue(columnLabels[c], values[r][c]);
                }
            }
            else if(tableOrder == TableOrder.BY_COLUMN)
            {
                int c;//index of the series in the main dataset
                for(c = 0; c < columnLabels.length; c++)
                {
                    if(columnLabels[c].equals(this.singlePieDatasetSeries));
                        break;
                }
                for(int r = 0; r < rowLabels.length; r++)
                {
                    pieDataset.setValue(rowLabels[r], values[r][c]);
                }
                
            }
                
        }
            
    }
    /**
     * get series color (section paint)
     * @param series
     * @return
     */
    @Override
    public Color getSeriesColor(int series)
    {
        Color color = null;
        Plot plot = chart.getPlot();
        if(plot instanceof PiePlot)
        {
            PiePlot p = (PiePlot)chart.getPlot();
            PieDataset pds = p.getDataset();
            String section = pds.getKey(series).toString();
            color = (Color)p.getSectionPaint(section);
        }
        else if(plot instanceof MultiplePiePlot)
        {
            MultiplePiePlot p = (MultiplePiePlot)plot;
            JFreeChart pieCh = p.getPieChart();
            PiePlot piePlot = (PiePlot)pieCh.getPlot();
            PieDataset pds = piePlot.getDataset();
            String section = pds.getKey(series).toString();
            color = (Color)piePlot.getSectionPaint(section);
        }
        return color;
    }
    /**
     * set series color (Section for piechart)
     * @param series
     * @param color
     */
    @Override
    public void setSeriesColor(int series, Color color)
    {
        //call setSectionPaint
        Plot plot = chart.getPlot();
        if(plot instanceof PiePlot)
        {
            PiePlot p = (PiePlot)chart.getPlot();
            PieDataset pds = p.getDataset();
            String section = pds.getKey(series).toString();
            this.setSectionPaint(section, color);
        }
        else if(plot instanceof MultiplePiePlot)
        {
            MultiplePiePlot p = (MultiplePiePlot)plot;
            JFreeChart pieCh = p.getPieChart();
            PiePlot piePlot = (PiePlot)pieCh.getPlot();
            PieDataset pds = piePlot.getDataset();
            String section = pds.getKey(series).toString();
            setSectionPaint(section, color);
            chart.fireChartChanged();
        }
    }
    /**
     * Function to set change color of pie section
     * sectionName-- section to to change color
     * color-- color used to change section color
     */
    @Override
    public void setSectionPaint(String sectionName, Color color)
    {
        Plot plot = chart.getPlot();
        if(plot instanceof PiePlot)
        {
            PiePlot p = (PiePlot)chart.getPlot();
            p.setSectionPaint(sectionName, color);
        }
        else if(plot instanceof MultiplePiePlot)
        {
            MultiplePiePlot p = (MultiplePiePlot)plot;
            JFreeChart pieCh = p.getPieChart();
            PiePlot piePlot = (PiePlot)pieCh.getPlot();
            piePlot.setSectionPaint(sectionName, color);
        }
    }
    /**
     * set label format
     * @param index
     */
    @Override
    public void setLabelFormat(int index)
    {
        StandardPieSectionLabelGenerator labelGenerator = null;
        switch(index)
        {
            case 0:
                labelGenerator = new StandardPieSectionLabelGenerator("{0}");
                break;
            case 1:
                labelGenerator = new StandardPieSectionLabelGenerator("{1}");
                break;
            case 2: 
                labelGenerator = new StandardPieSectionLabelGenerator("{2}");
                break;
            case 3:
                labelGenerator = new StandardPieSectionLabelGenerator("{0}\n\r{1}");
                break;
            case 4:
                labelGenerator = new StandardPieSectionLabelGenerator("{0}\n\r{2}");
                break;
        }
        Plot plot = chart.getPlot();
        if(plot instanceof MultiplePiePlot)
        {
            
           MultiplePiePlot p = (MultiplePiePlot)plot;
           JFreeChart pieCh = p.getPieChart();
           PiePlot piePlot = (PiePlot)pieCh.getPlot();
           piePlot.setLabelGenerator(labelGenerator);
        }
        else
        {
            PiePlot p = (PiePlot)plot;
            p.setLabelGenerator(labelGenerator);
        }
    }
    /**
     * Function to set label and label format
     * labelFormat: is to specify how to display label on the chart
     *              the format is using MessageFormat Subtistution
     *              Code: Description (as in PieDataset)
     *               {0} The item key.
     *               {1} The item value.
     *               {2} The item value as a percentage of the total.
     * numberFormat is used to format number display in the chart label
     * percentageFormat is used to format the percentage in the label
     */
    @Override
    public void setCustomLabelFormat(String labelFormat, String numberFormat, String percentageFormat)
    {
        PiePlot plot = (PiePlot) chart.getPlot();
        PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator(
        labelFormat, new DecimalFormat(numberFormat), new DecimalFormat(percentageFormat));
        plot.setLabelGenerator(generator);
    }
    /**
     * Function to set exploded parts of the chart
     * key--category to be exploded from the chart
     * percent-- percentage of the radius of the pie chart
     */
    @Override
    public void setExplodePercent(String key, double percent)
    {
        Plot plot = chart.getPlot();
        if(plot instanceof MultiplePiePlot)
        {
            
           MultiplePiePlot p = (MultiplePiePlot)plot;
           JFreeChart pieCh = p.getPieChart();
           PiePlot piePlot = (PiePlot)pieCh.getPlot();
           piePlot.setExplodePercent(key, percent);
        }
        else
        {
            PiePlot p = (PiePlot)plot;
            p.setExplodePercent(key, percent);
        }
       
    }
    /**
     * Function to set out line visibility
     */
    @Override
    public void setOutlineVisibility(boolean outLine)
    {
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionOutlinesVisible(outLine);
    }
    /**
     * Function to set ability to ignore 0 or null values in dataset
     */ 
    @Override
    public void setIgnoreNullOrZeroValues(boolean ignore)
    {
        Plot plot =  chart.getPlot();
        if(plot instanceof PiePlot)
        {
            ((PiePlot)plot).setIgnoreNullValues(ignore);
            ((PiePlot)plot).setIgnoreZeroValues(ignore);
        }
        else if(plot instanceof MultiplePiePlot)
        {
            JFreeChart pieCh = ((MultiplePiePlot)plot).getPieChart();
            Plot piePlot = pieCh.getPlot();
            if(piePlot instanceof PiePlot)
            {
                ((PiePlot)piePlot).setIgnoreNullValues(ignore);
                ((PiePlot)piePlot).setIgnoreZeroValues(ignore);
            }
            else
            {
                ((PiePlot3D)piePlot).setIgnoreNullValues(ignore);
                ((PiePlot3D)piePlot).setIgnoreZeroValues(ignore);
            }
        }
        else if(plot instanceof PiePlot3D)
        {
            ((PiePlot3D)plot).setIgnoreNullValues(ignore);
            ((PiePlot3D)plot).setIgnoreZeroValues(ignore);
        }
    }
    /*
     * Function to set background color for the chart
     */
    @Override
    public void setBackGroundColor(Color color)
    {
        chart.setBackgroundPaint(color);
    }
     @Override
    public void setPlotBackGroundColor(Color color)
    {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(color);
    }
    /**
     * Override functions 
     * generate pieChart
     */ 
       
    @Override
    public void generatePieChart(PieDataset dataset,
                                   String seriesName,
                                   TableOrder order)
    {
        pieDs = dataset;//store dataset for update
        this.singlePieDatasetSeries = seriesName;
        tableOrder = order;
        try
        {
            if(dataset == null)
                throw new Exception("No dataset provided");
            chart = ChartFactory.createPieChart(
            title,
            dataset,
            legend, // legend?
            false, // no tooltip needed
            false // mo URL needed
            );
            currentDatasetType = DatasetTypes.PieDataset;
        }
        catch(Exception ex)
        {
            //handle exception
            System.out.print("Error Generating pie chart. "
                    +ex.getMessage());
        }
    }
    @Override
    public void generate3DPieChart(PieDataset dataset,
                                   String seriesName,
                                   TableOrder order)
    {
        pieDs = dataset;//store dataset for update
        this.singlePieDatasetSeries = seriesName;
        tableOrder = order;
        try
        {
            if(dataset == null)
                throw new Exception("No dataset provided");
            chart = ChartFactory.createPieChart3D(
            title,
            dataset,
            legend, // legend?
            false, // no tooltip needed
            false // mo URL needed
            );
            currentDatasetType = DatasetTypes.PieDataset;
        }
        catch(Exception ex)
        {
            //handle exception
            System.out.print("Error Generating 3D pie chart. "
                    +ex.getMessage());
        }
    }
    @Override
    public void generateMultiplePieChart(CategoryDataset dataset, TableOrder order)
    { 
        ds = dataset;//store dataset for update
        try
        {
            currentDatasetType = DatasetTypes.CategoryDataset;
            if(dataset == null)
                    throw new Exception("No dataset provided");
            chart = ChartFactory.createMultiplePieChart(
                    title,
                    dataset,
                    order,
                    legend, // legend?
                    false, // no tooltip needed
                    false // mo URL needed
            );
            currentDatasetType = DatasetTypes.PieDataset;
        }
        catch(Exception ex)
        {
            //handle exception
            System.out.print("Error Generating muliple pie chart. "
                    +ex.getMessage());
        }
    }
    @Override
    public void generateMultiple3DPieChart(CategoryDataset dataset, TableOrder order)
    {
        ds = dataset;//store dataset for update
        try
        {
            currentDatasetType = DatasetTypes.CategoryDataset;
            if(dataset == null)
                    throw new Exception("No dataset provided");
            chart = ChartFactory.createMultiplePieChart3D(
                    title,
                    dataset,
                    order,
                    legend, // legend?
                    false, // no tooltip needed
                    false // mo URL needed
            );
            currentDatasetType = DatasetTypes.PieDataset;
        }
        catch(Exception ex)
        {
            //handle exception
            System.out.print("Error Generating 3D muliple pie chart. "
                    +ex.getMessage());
        }
    }
    
    /**
* Testing main function
*
* @param args ignored.
*/
    public static void main(String[] args) 
    {
    // create a dataset...
    DefaultPieDataset dataset = new DefaultPieDataset();
    dataset.setValue("Category 1", 43.2);
    dataset.setValue("Category 2", 27.9);
    dataset.setValue("Category 3", 69.5);
    dataset.setValue("Category 4", 10);
    // create a chart...
    IPieChart c = new CustomPieChart("test Pie Chart", true);
    //c.generatePieChart(dataset);
    c.setExplodePercent("Category 2", 0.2);
   
    c.drawChart("Figure #");
    }
}
