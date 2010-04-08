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
package edu.uara.tableeditor;

import edu.uara.FBGFlow;
import edu.uara.wrappers.customcharts.CustomDatasetTable;
import java.io.IOException;
import java.io.Serializable;
import org.jfree.chart.ChartPanel;

import edu.uara.wrappers.customcharts.CustomJFreeChart;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * Temporary class that represents a table figure. Used in conjunction with TableObject.
 * @author jeff
 */
public class TableFigure implements Serializable {
    public static final int serialVersionUID = 1;
    
    private CustomJFreeChart chart = null;
    private String title = null;
    private CustomJFreeChart.CustomChartTypes chartType;
    private CellReferenceObject[][] tableContents;
    private String[] rowLabels;
    private CellReferenceObject[] columnLabels;
    private ArrayList<String> groupKeys;
    private Dimension figureSize = null;
    
    public TableFigure(String figureName,
                        CustomJFreeChart.CustomChartTypes type,
                        CustomJFreeChart c)
                        
    { 
        this.title = figureName;
        this.chartType = type;
        chart = c;
        
    }
    public TableFigure(String figureName,
                        CustomJFreeChart.CustomChartTypes type,
                        CustomJFreeChart c, 
                        CellReferenceObject[][] tableContents,
                        String[] rowLabels,
                        CellReferenceObject[] columnLabels,
                        String[] gk,
                        Dimension chartSize
                        )
                        
    { 
        
        
        this.title = figureName;
        this.chartType = type;
        chart = c;
        this.tableContents = tableContents;
        this.rowLabels = rowLabels;
        this.columnLabels = columnLabels;
        this.groupKeys = new ArrayList<String>();
        this.figureSize = chartSize;
        for(String s : gk)
            groupKeys.add(s);
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getGroupKeys() {
        return groupKeys;
    }
    
    public CellReferenceObject[] getColumnLabels() {
        return columnLabels;
    }

    public String[] getRowLabels() {
        return rowLabels;
    }

    public CellReferenceObject[][] getTableContents() {
        return tableContents;
    }
    
    /**
     * view chart in a new frame
     */
    public void chartPreview()
    {
                     
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame chartFrame = new JFrame(title);
                JFreeChart ch = chart.getChart();
                ChartPanel chartPanel = new ChartPanel(ch);
                chartPanel.setPreferredSize(new Dimension(500, 300));
                chartFrame.setContentPane(chartPanel);
                chartFrame.pack();
                RefineryUtilities.centerFrameOnScreen(chartFrame);
                chartFrame.setDefaultCloseOperation(
                                   javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                chartFrame.setVisible(true);
                chartFrame.setAlwaysOnTop(true);
            }
        });
        
    }

    public void setFigureSize(Dimension figureSize) {
        this.figureSize = figureSize;
    }

    public Dimension getFigureSize() {
        return figureSize;
    }
    /**
     * do dataset update and regenerate chart
     * @param source: table source that gives data feed
     */
    public void updateChart(ITableObject source)
    {
        try
        {
            chart.updateChart(new CustomDatasetTable(this.tableContents,
                                                     this.rowLabels,
                                                     this.columnLabels)
                              , source);
        }
        catch(Exception ex)
        {
            System.out.println("Error updating chart! " + ex.getMessage());
        }
    }
    public void compileFigure(String path) 
    {
        try
        {
           
            File chartFile = new File(path + "Chart_" + this.title + ".png");
            ChartUtilities.saveChartAsPNG(chartFile,
                                          chart.getChart(),
                                          this.figureSize.width,
                                          this.figureSize.height);
        }
        catch(Exception ex)
        {
            System.out.println("Error compiling figure: " + this.title 
                    + ". " + ex.getMessage());
            
        }
    }
    public String getName() 
    {
        return title;
    }
    /**
     * set new chart object
     * @param c: CustomJFreeChart object
     */
    public void setChart(CustomJFreeChart c)
    {
        chart = c;
    }
    /**
     * get the underlined CustomJFreeChart object
     * @return: CustomJFreeChart Object
     */
    public CustomJFreeChart getChartObject()
    {
        return chart;
    }
    /**
     * get chart panel to show in GUI
     * @return: ChartPanel (JPanel)
     */
    public ChartPanel getChartPanel()
    {
        try
        {
            return chart.drawChart(title).getChartPanel();
        }
        catch(Exception ex)
        {
            System.err.print("Chart object was not created");
            return null;
        }
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
