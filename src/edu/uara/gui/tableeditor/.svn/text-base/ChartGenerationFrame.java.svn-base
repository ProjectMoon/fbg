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

package edu.uara.gui.tableeditor;

import edu.uara.gui.main.MainFactbookFrame;
import edu.uara.tableeditor.DataCellListener;
import edu.uara.tableeditor.ITableObject;
import edu.uara.tableeditor.TableFigure;
import edu.uara.wrappers.CustomJTable;
import java.awt.event.HierarchyEvent;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


import edu.uara.wrappers.customcharts.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.jfree.util.TableOrder;

/**
 * GUI for Chart Generation
 * @author  Tu Hoang
 */
public class ChartGenerationFrame extends javax.swing.JFrame 
{
    
    ITableObject table = null;
    private CustomDatasetTable datasetTable = null;
    private TableFigure figure = null;
    private CategoryDataset baseDataset = null;
    /*private ChartPanel chart = null;*/
    private CustomJFreeChart.CustomChartTypes currentChartType = null;
    private CustomJFreeChart c = null;
    private ChartPanel panel = null;
    private MainFactbookFrame parentFrame;
    private CustomJTable dataviewTable;
    KeyToGroupMap groupMap = null;//group map for bar chart
    
    
 
    /** Creates new form ChartGenerationFrame */
    public ChartGenerationFrame(TableFigure figureObj, ITableObject tableSource,
                                MainFactbookFrame parent)
    {
        initComponents();
        this.combinedChartOption.setVisible(false);//disable this option
        this.addRadioButtonGroup();
        parentFrame = parent;   
        
        datasetTable = new CustomDatasetTable(figureObj.getTableContents(),
                                              figureObj.getRowLabels(),
                                              figureObj.getColumnLabels());
         tableSource.addDataCellListener(
                new DataCellListener(){
                @Override
                public void updateDataCellContents()
                {
                    refreshData();
                }
        });//cell listener to sync data
        dataviewTable = datasetTable.getDataView(tableSource,
                                                createDataviewPopup());
        JScrollPane tableScroll =  new JScrollPane(dataviewTable);
        splitPane.setBottomComponent(tableScroll);        
        
        //store table source
        table = tableSource;
        //store table figure
        figure = figureObj;
        c = figure.getChartObject();
        //initialize chart panel
        panel = figure.getChartPanel();
        this.splitPane.setDividerLocation(panel.getHeight());
        this.splitPane.setTopComponent(panel);
        //disable property tab 
        this.toggleTabPanel(false);
        
        //set default chart name
       try
        {
            String title =  tableSource.getTitle();
            if(title != null)
                txt_chartName.setText(title);
            else
                txt_chartName.setText("Chart_" + tableSource.getName());
        }
        catch(Exception ex)
        {
            //System.out.println("Error setting chart title: " + ex.getMessage());
            this.txt_chartName.setText("Chart_" + tableSource.getName());
        }
        this.datasetTable.setGroupKey(figureObj.getGroupKeys());
        this.txt_chartWidth.setText(String.valueOf(figure.getFigureSize().width));
        this.txt_chartHeight.setText(String.valueOf(figure.getFigureSize().height));
        
        this.cmd_saveChart.setEnabled(false);
        this.cmd_generateChart.setEnabled(false);    
    }
    public ChartGenerationFrame(CustomDatasetTable t,
                                ITableObject tableSource,
                                MainFactbookFrame parent)
    {
        //table = new TableObject((CustomJTable)t);
        initComponents();
        this.combinedChartOption.setVisible(false);//disable this option
        this.addRadioButtonGroup();
        parentFrame = parent;   
        
        datasetTable = t;
        tableSource.addDataCellListener(
                new DataCellListener(){
                @Override
                public void updateDataCellContents()
                {
                    refreshData();
                }
        });//cell listener to sync data
        dataviewTable = datasetTable.getDataView(tableSource,
                                                 createDataviewPopup());
        JScrollPane tableScroll =  new JScrollPane(dataviewTable);
        splitPane.setBottomComponent(tableScroll);
        
        //initialize chart panel
        panel = null;
        //store table source
        table = tableSource;
        //disable property tab 
        this.toggleTabPanel(false);
        //set default chart name
        try
        {
            String title =  tableSource.getTitle();
            if(title != null)
                txt_chartName.setText(title);
            else
                txt_chartName.setText("Chart_" + tableSource.getName());
            
        }
        catch(Exception ex)
        {
            //System.out.println("Error setting chart title: " + ex.getMessage());
            this.txt_chartName.setText("Chart_" + tableSource.getName());
        }
        //series color button
        this.cmd_changeSeriesColor.setEnabled(false);//enable this when chart is generated
        this.cmd_saveChart.setEnabled(false);
    }
    //this constructor is to be deleted
    public ChartGenerationFrame()//dummy constructor for testing
    {

        initComponents(); 
        this.addRadioButtonGroup();
        // create a baseDataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(20.3, "Product 1 (US)", "Jan 04");
        dataset.addValue(27.2, "Product 1 (US)", "Feb 04");
        dataset.addValue(19.7, "Product 1 (US)", "Mar 04");
        dataset.addValue(19.4, "Product 1 (Europe)", "Jan 04");
        dataset.addValue(10.9, "Product 1 (Europe)", "Feb 04");
        dataset.addValue(18.4, "Product 1 (Europe)", "Mar 04");
        dataset.addValue(16.5, "Product 1 (Asia)", "Jan 04");
        dataset.addValue(15.9, "Product 1 (Asia)", "Feb 04");
        dataset.addValue(16.1, "Product 1 (Asia)", "Mar 04");
        dataset.addValue(13.2, "Product 1 (Middle East)", "Jan 04");
        dataset.addValue(14.4, "Product 1 (Middle East)", "Feb 04");
        dataset.addValue(13.7, "Product 1 (Middle East)", "Mar 04");

        dataset.addValue(23.3, "Product 2 (US)", "Jan 04");
        dataset.addValue(16.2, "Product 2 (US)", "Feb 04");
        dataset.addValue(28.7, "Product 2 (US)", "Mar 04");
        dataset.addValue(12.7, "Product 2 (Europe)", "Jan 04");
        dataset.addValue(17.9, "Product 2 (Europe)", "Feb 04");
        dataset.addValue(12.6, "Product 2 (Europe)", "Mar 04");
        dataset.addValue(15.4, "Product 2 (Asia)", "Jan 04");
        dataset.addValue(21.0, "Product 2 (Asia)", "Feb 04");
        dataset.addValue(11.1, "Product 2 (Asia)", "Mar 04");
        dataset.addValue(23.8, "Product 2 (Middle East)", "Jan 04");
        dataset.addValue(23.4, "Product 2 (Middle East)", "Feb 04");
        dataset.addValue(19.3, "Product 2 (Middle East)", "Mar 04");

        dataset.addValue(11.9, "Product 3 (US)", "Jan 04");
        dataset.addValue(31.0, "Product 3 (US)", "Feb 04");
        dataset.addValue(22.7, "Product 3 (US)", "Mar 04");
        dataset.addValue(15.3, "Product 3 (Europe)", "Jan 04");
        dataset.addValue(14.4, "Product 3 (Europe)", "Feb 04");
        dataset.addValue(25.3, "Product 3 (Europe)", "Mar 04");
        dataset.addValue(23.9, "Product 3 (Asia)", "Jan 04");
        dataset.addValue(19.0, "Product 3 (Asia)", "Feb 04");
        dataset.addValue(10.1, "Product 3 (Asia)", "Mar 04");
        dataset.addValue(13.2, "Product 3 (Middle East)", "Jan 04");
        dataset.addValue(15.5, "Product 3 (Middle East)", "Feb 04");
        dataset.addValue(10.1, "Product 3 (Middle East)", "Mar 04");
        // create a chart...
        IBarChart c = new CustomBarChart("Test barchart", "Domain", "Range", 
                PlotOrientation.VERTICAL, true);
        c.generate3DStackedBarChart(dataset);
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        KeyToGroupMap map = new KeyToGroupMap("G1");
        map.mapKeyToGroup("Product 1 (US)", "G1");
        map.mapKeyToGroup("Product 1 (Europe)", "G1");
        map.mapKeyToGroup("Product 1 (Asia)", "G1");
        map.mapKeyToGroup("Product 1 (Middle East)", "G1");
        map.mapKeyToGroup("Product 2 (US)", "G2");
        map.mapKeyToGroup("Product 2 (Europe)", "G2");
        map.mapKeyToGroup("Product 2 (Asia)", "G2");
        map.mapKeyToGroup("Product 2 (Middle East)", "G2");
        map.mapKeyToGroup("Product 3 (US)", "G3");
        map.mapKeyToGroup("Product 3 (Europe)", "G3");
        map.mapKeyToGroup("Product 3 (Asia)", "G3");
        map.mapKeyToGroup("Product 3 (Middle East)", "G3");
        renderer.setSeriesToGroupMap(map); 
        
        renderer.setItemMargin(0.0);
        Paint p1 = new GradientPaint(
            0.0f, 0.0f, new Color(0x22, 0x22, 0xFF), 0.0f, 0.0f, new Color(0x88, 0x88, 0xFF)
        );
        renderer.setSeriesPaint(0, p1);
        renderer.setSeriesPaint(4, p1);
        renderer.setSeriesPaint(8, p1);
         
        Paint p2 = new GradientPaint(
            0.0f, 0.0f, new Color(0x22, 0xFF, 0x22), 0.0f, 0.0f, new Color(0x88, 0xFF, 0x88)
        );
        renderer.setSeriesPaint(1, p2); 
        renderer.setSeriesPaint(5, p2); 
        renderer.setSeriesPaint(9, p2); 
        
        Paint p3 = new GradientPaint(
            0.0f, 0.0f, new Color(0xFF, 0x22, 0x22), 0.0f, 0.0f, new Color(0xFF, 0x88, 0x88)
        );
        renderer.setSeriesPaint(2, p3);
        renderer.setSeriesPaint(6, p3);
        renderer.setSeriesPaint(10, p3);
            
        Paint p4 = new GradientPaint(
            0.0f, 0.0f, new Color(0xFF, 0xFF, 0x22), 0.0f, 0.0f, new Color(0xFF, 0xFF, 0x88)
        );
        renderer.setSeriesPaint(3, p4);
        renderer.setSeriesPaint(7, p4);
        renderer.setSeriesPaint(11, p4);
        renderer.setGradientPaintTransformer(
            new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL)
        );
        
        SubCategoryAxis domainAxis = new SubCategoryAxis("Product / Month");
        domainAxis.setCategoryMargin(0.05);
        domainAxis.addSubCategory("Product 1");
        domainAxis.addSubCategory("Product 2");
        domainAxis.addSubCategory("Product 3");
        
        CategoryPlot plot = (CategoryPlot) ((CustomJFreeChart)c).getPlot();
        plot.setDomainAxis(domainAxis);
        //plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
        plot.setRenderer(renderer);
        //plot.setFixedLegendItems(createLegendItems());
        
        ChartPanel panel = c.drawChart("Chart Editor").getChartPanel();
        
        ChartPanel.add(panel);
        //ChartPanel.setSize(panel.getWidth(), panel.getHeight());
        splitPane.setDividerLocation(panel.getHeight());
        //test table
        //H4_2 t = new H4_2();
        //CustomJTable temp = (CustomJTable) t.generateTable();
        //temp.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //JScrollPane tableScrollPane = new JScrollPane(temp);
        //tableScrollPane.add(temp);
        //splitPane.setBottomComponent(tableScrollPane);
        //temp.setCellSelectionEnabled(true);
        //this.getContentPane().add(TablePanel)
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rgp_chartButtons = new javax.swing.ButtonGroup();
        rgp_layoutGroup = new javax.swing.ButtonGroup();
        jSplitPane1 = new javax.swing.JSplitPane();
        ChartOptionPanel = new javax.swing.JPanel();
        pieChartOption = new javax.swing.JRadioButton();
        BarChartOption = new javax.swing.JRadioButton();
        lineChartOption = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        combinedChartOption = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_chartTitle = new javax.swing.JLabel();
        txt_chartName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        opt_effect3D = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        opt_layoutV = new javax.swing.JRadioButton();
        opt_layoutH = new javax.swing.JRadioButton();
        opt_effectStacked = new javax.swing.JRadioButton();
        opt_legend = new javax.swing.JRadioButton();
        tab_chartProperties = new javax.swing.JTabbedPane();
        tabPanel_piechart = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cbo_explodedSections = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txt_explodedPercent = new javax.swing.JTextField();
        opt_exploded = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        cbo_pieDataSelect = new javax.swing.JComboBox();
        cbo_pieDataSeries = new javax.swing.JComboBox();
        lbl_datasetBy = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        cbo_pieSectionLabel = new javax.swing.JComboBox();
        lbl_pieLabelFormat = new javax.swing.JLabel();
        opt_multiPieCharts = new javax.swing.JRadioButton();
        tabPanel_barlineChart = new javax.swing.JPanel();
        lbl_domainLabel = new javax.swing.JLabel();
        txt_DomainLabel = new javax.swing.JTextField();
        lbl_domainLabel1 = new javax.swing.JLabel();
        txt_RangeLabel = new javax.swing.JTextField();
        opt_showGridline = new javax.swing.JRadioButton();
        opt_showItemLabel = new javax.swing.JRadioButton();
        txt_itemLabelFormat = new javax.swing.JTextField();
        lbl_itemLabelFormat = new javax.swing.JLabel();
        opt_subCategory = new javax.swing.JRadioButton();
        cbo_itemLabelSize = new javax.swing.JComboBox();
        lbl_itemLabelFormat1 = new javax.swing.JLabel();
        cbo_barChartTableOrder = new javax.swing.JComboBox();
        lbl_itemLabelFormat2 = new javax.swing.JLabel();
        tablPanel_combined = new javax.swing.JPanel();
        opt_barLineChart = new javax.swing.JRadioButton();
        lbl_domainLabel2 = new javax.swing.JLabel();
        lbl_status = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_chartWidth = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_chartHeight = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        ChartArea = new javax.swing.JPanel();
        splitPane = new javax.swing.JSplitPane();
        ChartPanel = new javax.swing.JPanel();
        TablePanel = new javax.swing.JPanel();
        jScrollBar1 = new javax.swing.JScrollBar();
        jScrollBar2 = new javax.swing.JScrollBar();
        jToolBar1 = new javax.swing.JToolBar();
        cmd_generateChart = new javax.swing.JButton();
        cmd_saveChart = new javax.swing.JButton();
        cmd_refresh = new javax.swing.JButton();
        cmd_changeSeriesColor = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Chart Generation");
        setLocationByPlatform(true);
        setName("Chart Generation"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jSplitPane1.setMinimumSize(new java.awt.Dimension(200, 200));

        ChartOptionPanel.setAutoscrolls(true);

        pieChartOption.setText("Pie Chart");
        pieChartOption.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                pieChartOptionItemStateChanged(evt);
            }
        });

        BarChartOption.setText("Bar Chart");
        BarChartOption.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                BarChartOptionItemStateChanged(evt);
            }
        });

        lineChartOption.setText("Line Chart");
        lineChartOption.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                lineChartOptionItemStateChanged(evt);
            }
        });

        jLabel1.setText("Chart Types");

        combinedChartOption.setText("Combined Chart");
        combinedChartOption.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combinedChartOptionItemStateChanged(evt);
            }
        });

        lbl_chartTitle.setText("Chart Title");

        txt_chartName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_chartNameKeyTyped(evt);
            }
        });

        jLabel2.setText("Effects");

        opt_effect3D.setText("3D");

        jLabel3.setText("Layout");

        opt_layoutV.setSelected(true);
        opt_layoutV.setText("Vertical");

        opt_layoutH.setText("Horizontal");

        opt_effectStacked.setText("Stacked");

        opt_legend.setSelected(true);
        opt_legend.setText("Legend");

        tab_chartProperties.setToolTipText("");

        tabPanel_piechart.setName(""); // NOI18N

        jLabel4.setText("Exploded Section");

        jLabel5.setText("Offset percentage");

        txt_explodedPercent.setText("0.5");

        opt_exploded.setText("Exploded");

        jLabel6.setText("(e.g 0.5)");

        cbo_pieDataSelect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Row", "Column" }));
        cbo_pieDataSelect.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_pieDataSelectItemStateChanged(evt);
            }
        });

        lbl_datasetBy.setText("Data by");

        jLabel9.setText("Series");

        cbo_pieSectionLabel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Section Label", "Section Value", "Section Percentage", "Label and Value", "Label and Percentage" }));

        lbl_pieLabelFormat.setText("Label Format");

        opt_multiPieCharts.setText("Multi Pie Charts");
        opt_multiPieCharts.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                opt_multiPieChartsItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout tabPanel_piechartLayout = new javax.swing.GroupLayout(tabPanel_piechart);
        tabPanel_piechart.setLayout(tabPanel_piechartLayout);
        tabPanel_piechartLayout.setHorizontalGroup(
            tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPanel_piechartLayout.createSequentialGroup()
                .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabPanel_piechartLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabPanel_piechartLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabPanel_piechartLayout.createSequentialGroup()
                                .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl_datasetBy)
                                    .addComponent(jLabel9))
                                .addGap(4, 4, 4)
                                .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbo_pieDataSeries, 0, 160, Short.MAX_VALUE)
                                    .addComponent(cbo_pieDataSelect, 0, 160, Short.MAX_VALUE)))
                            .addGroup(tabPanel_piechartLayout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addComponent(opt_multiPieCharts))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabPanel_piechartLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabPanel_piechartLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(opt_exploded)
                                    .addComponent(cbo_explodedSections, 0, 140, Short.MAX_VALUE)))
                            .addGroup(tabPanel_piechartLayout.createSequentialGroup()
                                .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(tabPanel_piechartLayout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addComponent(lbl_pieLabelFormat))
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbo_pieSectionLabel, 0, 139, Short.MAX_VALUE)
                                    .addGroup(tabPanel_piechartLayout.createSequentialGroup()
                                        .addComponent(txt_explodedPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)))))))
                .addContainerGap())
        );
        tabPanel_piechartLayout.setVerticalGroup(
            tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPanel_piechartLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(opt_multiPieCharts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_pieDataSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_datasetBy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cbo_pieDataSeries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(opt_exploded)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_explodedSections, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_explodedPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(8, 8, 8)
                .addGroup(tabPanel_piechartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_pieSectionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_pieLabelFormat))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        tab_chartProperties.addTab("Pie Chart", tabPanel_piechart);

        lbl_domainLabel.setText("Domain Label");

        lbl_domainLabel1.setText("Range Label");

        opt_showGridline.setText("Show grid lines");

        opt_showItemLabel.setText("Show Item label");

        txt_itemLabelFormat.setText("0.00");

        lbl_itemLabelFormat.setText("Item label format (e.g #.##)");

        opt_subCategory.setText("Subcategory");
        opt_subCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                categoryOptionChanged(evt);
            }
        });

        cbo_itemLabelSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "8", "9", "10", "11", "12", "14", "15" }));
        cbo_itemLabelSize.setSelectedIndex(3);

        lbl_itemLabelFormat1.setText("Label size");

        cbo_barChartTableOrder.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Row", "Column" }));
        cbo_barChartTableOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_barChartTableOrderActionPerformed(evt);
            }
        });

        lbl_itemLabelFormat2.setText("Table Orientation");

        javax.swing.GroupLayout tabPanel_barlineChartLayout = new javax.swing.GroupLayout(tabPanel_barlineChart);
        tabPanel_barlineChart.setLayout(tabPanel_barlineChartLayout);
        tabPanel_barlineChartLayout.setHorizontalGroup(
            tabPanel_barlineChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPanel_barlineChartLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabPanel_barlineChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabPanel_barlineChartLayout.createSequentialGroup()
                        .addComponent(lbl_domainLabel)
                        .addContainerGap(179, Short.MAX_VALUE))
                    .addGroup(tabPanel_barlineChartLayout.createSequentialGroup()
                        .addComponent(lbl_domainLabel1)
                        .addGap(183, 183, 183))
                    .addGroup(tabPanel_barlineChartLayout.createSequentialGroup()
                        .addComponent(lbl_itemLabelFormat2)
                        .addContainerGap(159, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabPanel_barlineChartLayout.createSequentialGroup()
                        .addGroup(tabPanel_barlineChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_DomainLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                            .addComponent(txt_RangeLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tabPanel_barlineChartLayout.createSequentialGroup()
                                .addComponent(cbo_barChartTableOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(opt_subCategory, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tabPanel_barlineChartLayout.createSequentialGroup()
                                .addComponent(opt_showItemLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                                .addComponent(opt_showGridline))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tabPanel_barlineChartLayout.createSequentialGroup()
                                .addComponent(lbl_itemLabelFormat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                                .addComponent(lbl_itemLabelFormat1))
                            .addGroup(tabPanel_barlineChartLayout.createSequentialGroup()
                                .addComponent(txt_itemLabelFormat, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                .addGap(88, 88, 88)
                                .addComponent(cbo_itemLabelSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20))))
        );
        tabPanel_barlineChartLayout.setVerticalGroup(
            tabPanel_barlineChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPanel_barlineChartLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lbl_domainLabel)
                .addGap(1, 1, 1)
                .addComponent(txt_DomainLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_domainLabel1)
                .addGap(1, 1, 1)
                .addComponent(txt_RangeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabPanel_barlineChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(opt_showItemLabel)
                    .addComponent(opt_showGridline))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPanel_barlineChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_itemLabelFormat)
                    .addComponent(lbl_itemLabelFormat1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPanel_barlineChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_itemLabelFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_itemLabelSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(lbl_itemLabelFormat2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPanel_barlineChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_barChartTableOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(opt_subCategory))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        tab_chartProperties.addTab("Bar/Line Chart", tabPanel_barlineChart);

        tablPanel_combined.setEnabled(false);

        opt_barLineChart.setText("Bar and Line chart");

        javax.swing.GroupLayout tablPanel_combinedLayout = new javax.swing.GroupLayout(tablPanel_combined);
        tablPanel_combined.setLayout(tablPanel_combinedLayout);
        tablPanel_combinedLayout.setHorizontalGroup(
            tablPanel_combinedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablPanel_combinedLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(opt_barLineChart)
                .addContainerGap(133, Short.MAX_VALUE))
        );
        tablPanel_combinedLayout.setVerticalGroup(
            tablPanel_combinedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablPanel_combinedLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(opt_barLineChart)
                .addContainerGap(190, Short.MAX_VALUE))
        );

        tab_chartProperties.addTab("Combined", tablPanel_combined);

        lbl_domainLabel2.setText("Chart Property");

        lbl_status.setText("Status:None");

        jLabel7.setText("Image Size (output image)");

        jLabel8.setText("Width");

        txt_chartWidth.setText("800");
        txt_chartWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_chartWidthActionPerformed(evt);
            }
        });
        txt_chartWidth.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_chartWidthKeyTyped(evt);
            }
        });

        jLabel10.setText("Height");

        txt_chartHeight.setText("600");
        txt_chartHeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_chartHeightActionPerformed(evt);
            }
        });
        txt_chartHeight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_chartHeightKeyTyped(evt);
            }
        });

        jLabel11.setText("px");

        jLabel12.setText("px");

        javax.swing.GroupLayout ChartOptionPanelLayout = new javax.swing.GroupLayout(ChartOptionPanel);
        ChartOptionPanel.setLayout(ChartOptionPanelLayout);
        ChartOptionPanelLayout.setHorizontalGroup(
            ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChartOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(pieChartOption)
                    .addComponent(BarChartOption)
                    .addComponent(lineChartOption)
                    .addComponent(combinedChartOption))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(opt_legend, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ChartOptionPanelLayout.createSequentialGroup()
                        .addGroup(ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(opt_effect3D)
                                .addComponent(jLabel2))
                            .addComponent(opt_effectStacked))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(opt_layoutH)
                            .addComponent(opt_layoutV)
                            .addComponent(jLabel3))))
                .addContainerGap(28, Short.MAX_VALUE))
            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
            .addGroup(ChartOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_chartName, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(ChartOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_chartTitle)
                .addContainerGap(217, Short.MAX_VALUE))
            .addGroup(ChartOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_domainLabel2)
                .addGap(177, 177, 177))
            .addGroup(ChartOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tab_chartProperties, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(ChartOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(141, Short.MAX_VALUE))
            .addGroup(ChartOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_chartWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_chartHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addContainerGap(78, Short.MAX_VALUE))
            .addGroup(ChartOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_status, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                .addContainerGap())
        );
        ChartOptionPanelLayout.setVerticalGroup(
            ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChartOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ChartOptionPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pieChartOption)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BarChartOption)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lineChartOption)
                            .addComponent(opt_legend))
                        .addGap(3, 3, 3)
                        .addComponent(combinedChartOption))
                    .addGroup(ChartOptionPanelLayout.createSequentialGroup()
                        .addGroup(ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(opt_effect3D)
                            .addComponent(opt_layoutV))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(opt_effectStacked)
                            .addComponent(opt_layoutH))))
                .addGap(15, 15, 15)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(lbl_chartTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_chartName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_domainLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tab_chartProperties, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ChartOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_chartWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(txt_chartHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_status)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(ChartOptionPanel);

        ChartArea.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));

        splitPane.setDividerLocation(200);
        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        ChartPanel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));

        javax.swing.GroupLayout ChartPanelLayout = new javax.swing.GroupLayout(ChartPanel);
        ChartPanel.setLayout(ChartPanelLayout);
        ChartPanelLayout.setHorizontalGroup(
            ChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 878, Short.MAX_VALUE)
        );
        ChartPanelLayout.setVerticalGroup(
            ChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 199, Short.MAX_VALUE)
        );

        splitPane.setTopComponent(ChartPanel);

        TablePanel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));

        jScrollBar1.setAutoscrolls(true);

        jScrollBar2.setOrientation(javax.swing.JScrollBar.HORIZONTAL);

        javax.swing.GroupLayout TablePanelLayout = new javax.swing.GroupLayout(TablePanel);
        TablePanel.setLayout(TablePanelLayout);
        TablePanelLayout.setHorizontalGroup(
            TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TablePanelLayout.createSequentialGroup()
                .addContainerGap(861, Short.MAX_VALUE)
                .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 878, Short.MAX_VALUE)
        );
        TablePanelLayout.setVerticalGroup(
            TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TablePanelLayout.createSequentialGroup()
                .addComponent(jScrollBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        splitPane.setRightComponent(TablePanel);

        javax.swing.GroupLayout ChartAreaLayout = new javax.swing.GroupLayout(ChartArea);
        ChartArea.setLayout(ChartAreaLayout);
        ChartAreaLayout.setHorizontalGroup(
            ChartAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
        );
        ChartAreaLayout.setVerticalGroup(
            ChartAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(ChartArea);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setToolTipText("Chart Tools");

        cmd_generateChart.setText("Generate Chart");
        cmd_generateChart.setToolTipText("Generate new chart. If disable save current chart or enter new chart name to reenable this tool");
        cmd_generateChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmd_generateChartActionPerformed(evt);
            }
        });
        jToolBar1.add(cmd_generateChart);

        cmd_saveChart.setText("Save");
        cmd_saveChart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmd_saveChart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmd_saveChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmd_saveChartActionPerformed(evt);
            }
        });
        jToolBar1.add(cmd_saveChart);

        cmd_refresh.setText("Refresh");
        cmd_refresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmd_refresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmd_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmd_refreshActionPerformed(evt);
            }
        });
        jToolBar1.add(cmd_refresh);

        cmd_changeSeriesColor.setText("Series Color");
        cmd_changeSeriesColor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmd_changeSeriesColor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmd_changeSeriesColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmd_changeSeriesColorActionPerformed(evt);
            }
        });
        jToolBar1.add(cmd_changeSeriesColor);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 947, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
/**
 * Handle generation event
 * @param evt
 */
private void cmd_generateChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmd_generateChartActionPerformed
    //generate chart
    this.generateChart();
    
    
}//GEN-LAST:event_cmd_generateChartActionPerformed

private void lineChartOptionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_lineChartOptionItemStateChanged
// TODO add your handling code here:
    if(this.lineChartOption.isSelected())
    {
        currentChartType = CustomJFreeChart.CustomChartTypes.JLineChart;
        this.switchTabPanel(tabPanel_barlineChart);
        this.toggleAxisTextInput(true);
        this.toggleStackedEffect(false);
        this.toggleLayoutOption(true);
        this.toggleExplodedEffect(false);
        this.toggleBarChartOptions(false);
    }
    
}//GEN-LAST:event_lineChartOptionItemStateChanged

private void BarChartOptionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_BarChartOptionItemStateChanged
// TODO add your handling code here:
    if(this.BarChartOption.isSelected())
    {
        currentChartType = CustomJFreeChart.CustomChartTypes.JBarChart;
        this.switchTabPanel(tabPanel_barlineChart);
        this.toggleAxisTextInput(true);
        this.toggleStackedEffect(true);
        this.toggleLayoutOption(true);
        this.toggleExplodedEffect(false);
        this.toggleBarChartOptions(true);
    }
}//GEN-LAST:event_BarChartOptionItemStateChanged

private void pieChartOptionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_pieChartOptionItemStateChanged
// TODO add your handling code here:
    if(this.pieChartOption.isSelected())
    {
        currentChartType = CustomJFreeChart.CustomChartTypes.JPieChart; 
        this.switchTabPanel(tabPanel_piechart);
        this.toggleAxisTextInput(false);
        this.toggleStackedEffect(false);
        this.toggleLayoutOption(false);
        this.toggleExplodedEffect(true);
        this.toggleBarChartOptions(false);
        
        //populate data series for piechart
        this.populatePieDataSeries();
        if(this.opt_multiPieCharts.isSelected())
            this.toggleMultiplePieChartOptions(false);
        else
            this.toggleMultiplePieChartOptions(true);
    }
}//GEN-LAST:event_pieChartOptionItemStateChanged

private void cmd_saveChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmd_saveChartActionPerformed
// TODO add your handling code here:
    saveFigure();//add figure to table//GEN-LAST:event_cmd_saveChartActionPerformed
}
private void cbo_pieDataSelectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_pieDataSelectItemStateChanged
// TODO add your handling code here:
    this.populatePieDataSeries();
}//GEN-LAST:event_cbo_pieDataSelectItemStateChanged

private void opt_multiPieChartsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_opt_multiPieChartsItemStateChanged
// TODO add your handling code here:

}//GEN-LAST:event_opt_multiPieChartsItemStateChanged

private void combinedChartOptionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combinedChartOptionItemStateChanged
// TODO add your handling code here:
    if(this.combinedChartOption.isSelected())
    {
        currentChartType = CustomJFreeChart.CustomChartTypes.JCombinedChart;
        this.switchTabPanel(this.tablPanel_combined);
        this.toggleStackedEffect(false);
    }
}//GEN-LAST:event_combinedChartOptionItemStateChanged

private void cmd_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmd_refreshActionPerformed
// TODO add your handling code here:
    this.refreshData();
}//GEN-LAST:event_cmd_refreshActionPerformed

private void categoryOptionChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_categoryOptionChanged
// TODO add your handling code here:
    if(this.opt_subCategory.isSelected())                                                
    {
        this.opt_effect3D.setEnabled(false);
        this.toggleStackedEffect(false);
    }
    else
    {
        this.opt_effect3D.setEnabled(true);
        this.toggleStackedEffect(true);
    }
}//GEN-LAST:event_categoryOptionChanged

private void cmd_changeSeriesColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmd_changeSeriesColorActionPerformed
// TODO add your handling code here:
    java.awt.EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                seriesColorEdit();
            }
        
    });
    
}//GEN-LAST:event_cmd_changeSeriesColorActionPerformed

private void txt_chartWidthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_chartWidthActionPerformed
// TODO add your handling code here:
    chartSizeEdited();
}//GEN-LAST:event_txt_chartWidthActionPerformed

private void txt_chartHeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_chartHeightActionPerformed
// TODO add your handling code here:
    chartSizeEdited();
}//GEN-LAST:event_txt_chartHeightActionPerformed

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
// TODO add your handling code here:
    if(this.cmd_saveChart.isEnabled())
    {
        int option = 
                JOptionPane.showConfirmDialog(this,
                "Chart has been modified! Do you want to save?",
                "Warning",
                JOptionPane.YES_NO_OPTION); 
                
        switch(option)
        {
            case JOptionPane.OK_OPTION:
                this.saveFigure();
                break;
            case JOptionPane.NO_OPTION:
                break;
        }
    }
}//GEN-LAST:event_formWindowClosing

private void txt_chartWidthKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_chartWidthKeyTyped
// TODO add your handling code here:
    chartSizeEdited();
}//GEN-LAST:event_txt_chartWidthKeyTyped

private void txt_chartHeightKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_chartHeightKeyTyped
// TODO add your handling code here:
    chartSizeEdited();
}//GEN-LAST:event_txt_chartHeightKeyTyped

private void txt_chartNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_chartNameKeyTyped
// TODO add your handling code here:
    if(!this.cmd_generateChart.isEnabled())
        this.cmd_generateChart.setEnabled(true);
}//GEN-LAST:event_txt_chartNameKeyTyped

private void cbo_barChartTableOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_barChartTableOrderActionPerformed
// TODO add your handling code here:
    if(this.cbo_barChartTableOrder.getSelectedItem().toString().equals("Column"))
    {
        this.datasetTable.setTableOrder(TableOrder.BY_COLUMN);
        this.refreshData();
    }
    else
    {
        this.datasetTable.setTableOrder(TableOrder.BY_ROW);
        this.refreshData();
    }
}//GEN-LAST:event_cbo_barChartTableOrderActionPerformed
   
    private void chartSizeEdited()
    {
        if(c != null)
        {
            this.cmd_saveChart.setEnabled(true);
            updateStatus("Chart size is edited!");
        }
    }
    /**
     * activate series editor frame (used in change series
     * tool bar button event
     */
    private void seriesColorEdit()
    {      
        if(this.pieChartOption.isEnabled())
        {
            String tableOrder = cbo_pieDataSelect.getSelectedItem().toString();
            TableOrder tbOrder = null;
            if(tableOrder.equals("Row"))
            {
                tbOrder = TableOrder.BY_ROW;
            }
            else
            {
                 tbOrder = TableOrder.BY_COLUMN;
            }
            new SeriesColorEditor(datasetTable, c, table, tbOrder).setVisible(true);       
        }
        else
            new SeriesColorEditor(datasetTable, c, table, TableOrder.BY_ROW).setVisible(true);       
        
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChartGenerationFrame().setVisible(true);
            }
        });
    }
       /**
     * update dataset contents in the event of source data changed
     * from source table (tableObject)
     */
    
    public void updateDataCellContents()
    {
        refreshData();
    }
    /**
     * function to load data from source table to dataview table
     */
    public void refreshData()
    {
        dataviewTable = datasetTable.getDataView(table, createDataviewPopup());
        JScrollPane tableScroll =  new JScrollPane(dataviewTable);
        //add new dataview table
        splitPane.setBottomComponent(tableScroll);    
        splitPane.setDividerLocation(0.7);
        
        if(this.pieChartOption.isSelected())
            this.populatePieDataSeries();//repopulate data labels for piechart
    }
    
    private JPopupMenu createDataviewPopup()
    {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem addColGroup = new JMenuItem("Group Column");
        JMenuItem addGroup = new JMenuItem("Group series");
        JMenuItem addData = new JMenuItem("Edit dataset..");
        menu.add(addColGroup);
        menu.add(addGroup);
        menu.add(addData);
        addColGroup.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                groupCategoryByCol();
                refreshData();
            }
        });
        addGroup.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                groupCategory();
                refreshData();
            }
        });
        addData.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                EventQueue.invokeLater(new Runnable(){
                
                    @Override
                    public void run()
                    {
                        
                        new DatasetEditor(table, datasetTable,
                                          table.getName(),
                                          new IDataRefreshListener()
                        {
                            @Override
                            public void refreshDataset()
                            {
                                refreshData();
                            }
                        }).setVisible(true);
                        
                    }
                });
            }
        });
        return menu;
    }
    /**
     * group category by adding group key to existing row series labels
     */
    private void groupCategory()
    {
        if(datasetTable.getTableOrder() == TableOrder.BY_COLUMN)
        {
            JOptionPane.showMessageDialog(this, 
                    "Can not group series when colmn orientation is selected!");
                return;
        }
        int[] rows = this.dataviewTable.getSelectedRows();
        int[] cols = this.dataviewTable.getSelectedColumns();
        String name = null;
        for(int i = 0; i < rows.length; i++)
        {
            if(rows[i] == 0)
            {
                JOptionPane.showMessageDialog(this, 
                    "Row 1 is not used! please select from row 2 and on");
                return;
            }
        }
        if(cols.length > 1 && cols[0] != 0)
        {
            JOptionPane.showMessageDialog(this, 
                    "Please select only the first column!");
            return;
        }
        else if(rows.length < 2)
        {
            JOptionPane.showMessageDialog(this, 
                    "Please select at least 2 series to group");
            return;
        }
        else
        {
            name = JOptionPane.showInputDialog("Please enter group name!");
            if(name == null)
                return;
            datasetTable.addGroupKey(name);
        }
        for(int row : rows)
        {
            
            String currentLabel = datasetTable.getValueAt(row, 0).toString();
            String newLabel = name + " " + currentLabel;
            datasetTable.setValueAt(newLabel, row, 0);
            
        }
    }
    private void groupCategoryByCol()
    {
        if(datasetTable.getTableOrder() == TableOrder.BY_ROW)
        {
            JOptionPane.showMessageDialog(this, 
                    "Can not group column when row orientation is selected!");
                return;
        }
        int[] rows = this.dataviewTable.getSelectedRows();
        int[] cols = this.dataviewTable.getSelectedColumns();
        String name = null;
        for(int i = 0; i < cols.length; i++)
        {
            if(cols[i] == 0)
            {
                JOptionPane.showMessageDialog(this, 
                    "Column 1 is not used! please select from column 2 and on");
                return;
            }
        }
        if(rows.length > 1 && rows[0] != 0)
        {
            JOptionPane.showMessageDialog(this, 
                    "Please select only the first row!");
            return;
        }
        else if(cols.length < 2)
        {
            JOptionPane.showMessageDialog(this, 
                    "Please select at least 2 categories to group");
            return;
        }
        else
        {
            name = JOptionPane.showInputDialog("Please enter group name!");
            if(name == null)
                return;
            String colNames[] = new String[cols.length];
            for(int i = 0; i < cols.length; i++)
            {
                try
                {
                    colNames[i] = dataviewTable.getValueAt(0, cols[i]).toString();
                }
                catch(Exception ex)
                {
                    System.out.println(ex.toString());
                }
            }
            datasetTable.addColGroupKey(name, colNames);
            //datasetTable.addGroupKey(name);
        }
       
    }
    private void saveFigure()
    {
        try
        {
            if(figure != null)
            {
                figure.setChart(this.c);
                figure.setFigureSize(new Dimension(
                                    Integer.parseInt(this.txt_chartWidth.getText()),
                                    Integer.parseInt(this.txt_chartHeight.getText())
                        ));
                figure.setTitle(this.txt_chartName.getText());
                updateStatus("Chart has been edited and saved!");
            }
            else
            {
                
                figure = this.getFigure();
                figure.setTitle(this.txt_chartName.getText());
                table.addFigure(figure);    
                parentFrame.updateProjectTree();
                updateStatus("Chart has been saved!");
            }
            this.cmd_saveChart.setEnabled(false);
            this.cmd_generateChart.setEnabled(true);
        }
        catch(Exception ex)
        {
            System.err.println("Error saving chart! " + ex.getMessage());
        }
    }
    
    private void updateStatus(String s)
    {
        this.lbl_status.setText("Status: "+s);
    }
    
    private void toggleBarChartOptions(boolean enabled)
    {
        this.txt_itemLabelFormat.setVisible(enabled);
        this.opt_showItemLabel.setEnabled(enabled);
        this.lbl_itemLabelFormat.setVisible(enabled);
        this.opt_subCategory.setEnabled(enabled);
        this.cbo_itemLabelSize.setEnabled(enabled);
    }
    private void toggleLayoutOption(boolean enabled)
    {
        this.opt_layoutH.setEnabled(enabled);
        this.opt_layoutV.setEnabled(enabled);
    }
    private void toggleStackedEffect(boolean enabled)
    {
        this.opt_effectStacked.setEnabled(enabled);
    }
    private void toggleExplodedEffect(boolean enabled)
    {
        this.opt_exploded.setEnabled(enabled);
    }
    private void toggleAxisTextInput(boolean enabled)
    {
        this.txt_DomainLabel.setEnabled(enabled);
        this.txt_RangeLabel.setEnabled(enabled);
    }
    /**
     * turn tab panel on or off
     * @param enabled
     */
    private void toggleTabPanel(boolean enabled)
    {
        for(Component comp : tab_chartProperties.getComponents())
        {
            if(comp instanceof JPanel)
            {
                JPanel p = (JPanel)comp;
                for(Component childComp :p.getComponents())
                    childComp.setEnabled(enabled);
            }
        }
    }
    /**
     * turn on and off other options when multi-piechart is selected
     * @param enabled
     */
    private void toggleMultiplePieChartOptions(boolean enabled)
    {
        for(Component comp : this.tabPanel_piechart.getComponents())
        {
            if(!comp.equals(this.cbo_pieDataSelect) && 
                    !comp.equals(this.opt_multiPieCharts) && 
                    !comp.equals(this.lbl_datasetBy) &&
                    !comp.equals(this.lbl_pieLabelFormat) &&
                    !comp.equals(this.cbo_pieSectionLabel))
            {
                comp.setEnabled(enabled);
            }
        }
    }
    private void switchTabPanel(Component c)
    {
        this.tab_chartProperties.setSelectedComponent(c);
        Component[] compCollection = tab_chartProperties.getComponents();
        for(Component comp : compCollection)
        {
            if(comp.equals(c))
            {
                if(comp instanceof JPanel)
                {
                    JPanel p = (JPanel)comp;
                    for(Component childComp :p.getComponents())
                        childComp.setEnabled(true);
                }
            }
            else
            {
                if(comp instanceof JPanel)
                {
                    JPanel p = (JPanel)comp;
                    for(Component childComp :p.getComponents())
                        childComp.setEnabled(false);
                }
            }
        }
    }
    private void generateBaseDataset()
    {
        try
        {
            if(this.cbo_barChartTableOrder.isEnabled())
            {
                String tableOrder = 
                        cbo_barChartTableOrder.getSelectedItem().toString();
                TableOrder tbOrder = null;
                if(tableOrder.equals("Row"))
                {
                    tbOrder = TableOrder.BY_ROW;
                    this.baseDataset = DatasetUtilities.createCategoryDataset(
                                           datasetTable.getRowLabels(),
                                           datasetTable.getColumnLabels(table),
                                           datasetTable.getTableContentAsValue(table));
                    
                }
                else
                {
                     tbOrder = TableOrder.BY_COLUMN;                  
                     this.baseDataset = DatasetUtilities.createCategoryDataset(
                                           datasetTable.getColumnLabels(table),
                                           datasetTable.getRowLabels(),
                                           datasetTable.getTableContentAsValueTranspose(table));
                }
                if(this.opt_subCategory.isSelected() && 
                        datasetTable.getGroupKey().length > 0)
                {
                    groupMap =
                            CustomJFreeChartData.generateGroupedMap(baseDataset, 
                                                    datasetTable.getGroupKey());
                }
            }
            else
            {
                this.baseDataset = DatasetUtilities.createCategoryDataset(
                                           datasetTable.getRowLabels(),
                                           datasetTable.getColumnLabels(table),
                                           datasetTable.getTableContentAsValue(table));
                
            }
        }
        catch(Exception ex)
        {
            System.out.print("Dataset generation error: "+ex.toString());
            JOptionPane.showMessageDialog(this, ex.getMessage());
            this.baseDataset = DatasetUtilities.createCategoryDataset(
                "series", "", 
                datasetTable.getTableContentAsValue(table));
        }
            
    }
    
    private void generateChart()
    {
        //generate Base Dataset (Category Dataset)
        if(datasetTable != null)
            this.generateBaseDataset();

        if(c != null)
        {
            if(this.splitPane.getTopComponent() == panel)
            {
                int option = JOptionPane.showConfirmDialog(this,
                        "Create new chart?\r\n",
                        "Warning", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION)
                {
                    if(this.cmd_saveChart.isEnabled())
                    {
                        int innerOption = JOptionPane.showConfirmDialog(this,
                        "Current chart is modified. Do you want to save?\r\n",
                        "Warning", JOptionPane.YES_NO_OPTION);
                        if(innerOption == JOptionPane.YES_OPTION)
                            this.saveFigure();
                    }
                    this.figure = null;
                    this.splitPane.remove(panel);
                }
                else
                    return;
        
            }
        }
        
        if(this.pieChartOption.isSelected())
        {
            //get series
            PieDataset dataset = null;
            String series = this.cbo_pieDataSeries.getSelectedItem().toString();
            CustomPieChart ch = new CustomPieChart(txt_chartName.getText(),
                                          this.opt_legend.isSelected());//legend
            String tableOrder = cbo_pieDataSelect.getSelectedItem().toString();
            TableOrder tbOrder = null;
            if(tableOrder.equals("Row"))
            {
                tbOrder = TableOrder.BY_ROW;
            }
            else
            {
                 tbOrder = TableOrder.BY_COLUMN;
            }
            
                    
            if(this.opt_multiPieCharts.isSelected())//multiple piechart 
            {         
                if(this.opt_effect3D.isSelected())
                    ch.generateMultiple3DPieChart(baseDataset,
                                                  tbOrder);
                else
                    ch.generateMultiplePieChart(baseDataset, 
                                                  tbOrder);
                
                if(this.opt_exploded.isSelected())
                {                    
                    try
                    {
                        ch.setExplodePercent(
                            this.cbo_explodedSections.getSelectedItem().toString(),
                            Double.parseDouble(this.txt_explodedPercent.getText()));
                    }
                    catch(Exception ex)
                    {
                        System.out.println("Error setting exploded section! "
                                + ex.getMessage());
                    }           
                }
            }
            else
            {
                try
                {
                    if(tbOrder == TableOrder.BY_ROW)
                        dataset = DatasetUtilities.createPieDatasetForRow(
                                                            baseDataset, series);
                    else
                        dataset = DatasetUtilities.createPieDatasetForColumn(
                                                            baseDataset, series);
                }
                catch(Exception ex)
                {
                    System.out.println(
                            "Error with pie dataset, default series is used");
                    dataset = 
                            DatasetUtilities.createPieDatasetForRow(baseDataset, 0);
                }
                
            
                //ch.setIgnoreNullOrZeroValues(true);//ignore null values

                if(opt_effect3D.isSelected())
                {
                    ch.generate3DPieChart(dataset, series, tbOrder);
                }
                else
                {                
                    ch.generatePieChart(dataset, series, tbOrder);
                    //edit exploded parts
                    if(this.opt_exploded.isSelected())
                    {
                        try
                        {
                            ch.setExplodePercent(
                                this.cbo_explodedSections.getSelectedItem().toString(),
                                Double.parseDouble(this.txt_explodedPercent.getText()));
                        }
                        catch(Exception ex)
                        {
                            System.out.println("Error setting exploded section! "
                                    + ex.getMessage());
                        }
                    }
                }
            }
            ch.setIgnoreNullOrZeroValues(true);//ignore null values
            int labelFormat = cbo_pieSectionLabel.getSelectedIndex();
            ch.setLabelFormat(labelFormat);
            c = ch;
            this.updateStatus("Pie chart has been generated.");
        }
        else if(this.lineChartOption.isSelected())
        {
            // create a chart...
            PlotOrientation orientation;//options for orientation
            
            if(this.opt_layoutV.isSelected())
                 orientation = PlotOrientation.VERTICAL;
            else if(this.opt_layoutH.isSelected())
                orientation = PlotOrientation.HORIZONTAL;
            else
                orientation = PlotOrientation.VERTICAL;
            
            CustomLineChart ch = new CustomLineChart(this.txt_chartName.getText(),
                                          this.txt_DomainLabel.getText(),
                                          this.txt_RangeLabel.getText(),
                                          orientation, this.opt_legend.isSelected());
            if(opt_effect3D.isSelected())
            {
                ch.generate3DLineChart(baseDataset);
            }
            else
            {
                ch.generateLineChart(baseDataset);
            }
            
            //apply options
            if(this.opt_showGridline.isSelected())
            {
                ch.setGridLineVisible(true);
            }
            else
            {
                ch.setGridLineVisible(false);
            }
           
            c = ch;
            this.updateStatus("Line chart has been generated.");
        }
        else if(this.BarChartOption.isSelected())
        {
            // create a chart...
            PlotOrientation orientation;//options for orientation
            
            if(this.opt_layoutV.isSelected())
                 orientation = PlotOrientation.VERTICAL;
            else if(this.opt_layoutH.isSelected())
                orientation = PlotOrientation.HORIZONTAL;
            else
                orientation = PlotOrientation.VERTICAL;
            
            CustomBarChart ch = new CustomBarChart(this.txt_chartName.getText(),
                                          this.txt_DomainLabel.getText(),
                                          this.txt_RangeLabel.getText(),
                                          orientation, opt_legend.isSelected());
            if(this.opt_subCategory.isSelected())
            {
                ch.generateGroupBarChart(baseDataset, groupMap);
                ch.setSubCategoryAxis(txt_DomainLabel.getText(),
                                      datasetTable.getGroupKey());
            }
            else
            {
                if(opt_effect3D.isSelected())
                {
                    if(this.opt_effectStacked.isSelected())
                        ch.generate3DStackedBarChart(baseDataset);
                    else
                        ch.generate3DBarChart(baseDataset);
                }
                else
                {
                    if(this.opt_effectStacked.isSelected())
                        ch.generateStackedBarChart(baseDataset);
                    else
                        ch.generateBarChart(baseDataset);
                }
            }
            //apply options
            if(this.opt_showGridline.isSelected())
            {
                ch.setGridLineVisible(true);
            }
            else
            {
                ch.setGridLineVisible(false);
            }
            if(this.opt_showItemLabel.isSelected())
            {
                ch.setItemLabelGenerator(
                        txt_itemLabelFormat.getText(),
                        Integer.parseInt(
                            cbo_itemLabelSize.getSelectedItem().toString()));
                ch.setItemLabelVisible(true);
            }
            c = ch;
            this.updateStatus("Bar chart has been generated.");
        }
        else if(this.combinedChartOption.isSelected())
        {
            //not available yet
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Please select chart type first!");
            return;
        }
        
        //c.generate3DStackedBarChart(baseDataset);
        ChartFrame f = c.drawChart("");
        panel = f.getChartPanel();
        splitPane.setDividerLocation(panel.getHeight());
        this.splitPane.setTopComponent(panel);
        //ChartPanel.add(panel);
        //panel.setSize(500, splitPane.getWidth());
        
        if(panel != null)
            panel.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener()
            {
               
                @Override
                public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                    panelAncestorResized(evt);
                };

                @Override
                public void ancestorMoved(HierarchyEvent e) {
                   
                }
            }); 
         
         this.cmd_saveChart.setEnabled(true);
         this.cmd_changeSeriesColor.setEnabled(true);
         this.cmd_generateChart.setEnabled(false);
        
         
    }
    private void panelAncestorResized(java.awt.event.HierarchyEvent evt) 
    {
        //handle resize
//        Component parent = evt.getChangedParent();
        Component parent = this.splitPane;
        Component comp = evt.getComponent();
        
        if(comp != null && parent != null)
        {
            comp.setSize(parent.getWidth(), this.splitPane.getDividerLocation());
            String s = String.format("Current Width = %1$s"+
                                      " Height = %2$s",
                                      comp.getWidth(), comp.getHeight());
                    
            this.updateStatus(s);
            
        }
    }
    /**
     * return tableFigure object
     * @return
     */
    public TableFigure getFigure()
    {
        if(figure == null)
        {
            String chartName = this.txt_chartName.getText();
            if(chartName.isEmpty())
                chartName = table.getTitle();
            return new TableFigure(chartName,
                                   this.currentChartType,
                                   this.c, 
                                   this.datasetTable.getCellReferences(),
                                   this.datasetTable.getRowLabels(),
                                   this.datasetTable.getColumnLabels(),
                                   this.datasetTable.getGroupKey(),
                                   new Dimension(
                                    Integer.parseInt(txt_chartWidth.getText()),
                                    Integer.parseInt(this.txt_chartHeight.getText()))
                                   );
        }
        else
            return figure;
    }
    
    private void addRadioButtonGroup()
    {
        //charttype button group
        this.rgp_chartButtons.add(BarChartOption);
        this.rgp_chartButtons.add(this.pieChartOption);
        this.rgp_chartButtons.add(this.lineChartOption);
        this.rgp_chartButtons.add(this.combinedChartOption);
        
        //layout type button group
        this.rgp_layoutGroup.add(this.opt_layoutH);
        this.rgp_layoutGroup.add(this.opt_layoutV);
    }
    
    private void populatePieDataSeries()
    {
        String selection = this.cbo_pieDataSelect.getSelectedItem().toString();
        if(this.cbo_pieDataSeries.getItemCount() > 0)
            this.cbo_pieDataSeries.removeAllItems();//remove all current series
        
        if(this.cbo_explodedSections.getItemCount() > 0)
            //remove all current exploded sections
            this.cbo_explodedSections.removeAllItems();
        if(selection.equals("Row"))
        {
            for(String s : this.datasetTable.getRowLabels())
                this.cbo_pieDataSeries.addItem(s);
            
            //populate exploded combo box for piechart
            for(String s : datasetTable.getColumnLabels(this.table))
                this.cbo_explodedSections.addItem(s);
            this.cbo_explodedSections.setSelectedIndex(0);
            
        }
        else if(selection.equals("Column"))
        {
            for(String s: this.datasetTable.getColumnLabels(this.table))
                this.cbo_pieDataSeries.addItem(s);
            
            //populate exploded combo box for piechart
            for(String s : datasetTable.getRowLabels())
                this.cbo_explodedSections.addItem(s);
            this.cbo_explodedSections.setSelectedIndex(0);
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton BarChartOption;
    private javax.swing.JPanel ChartArea;
    private javax.swing.JPanel ChartOptionPanel;
    private javax.swing.JPanel ChartPanel;
    private javax.swing.JPanel TablePanel;
    private javax.swing.JComboBox cbo_barChartTableOrder;
    private javax.swing.JComboBox cbo_explodedSections;
    private javax.swing.JComboBox cbo_itemLabelSize;
    private javax.swing.JComboBox cbo_pieDataSelect;
    private javax.swing.JComboBox cbo_pieDataSeries;
    private javax.swing.JComboBox cbo_pieSectionLabel;
    private javax.swing.JButton cmd_changeSeriesColor;
    private javax.swing.JButton cmd_generateChart;
    private javax.swing.JButton cmd_refresh;
    private javax.swing.JButton cmd_saveChart;
    private javax.swing.JRadioButton combinedChartOption;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollBar jScrollBar2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lbl_chartTitle;
    private javax.swing.JLabel lbl_datasetBy;
    private javax.swing.JLabel lbl_domainLabel;
    private javax.swing.JLabel lbl_domainLabel1;
    private javax.swing.JLabel lbl_domainLabel2;
    private javax.swing.JLabel lbl_itemLabelFormat;
    private javax.swing.JLabel lbl_itemLabelFormat1;
    private javax.swing.JLabel lbl_itemLabelFormat2;
    private javax.swing.JLabel lbl_pieLabelFormat;
    private javax.swing.JLabel lbl_status;
    private javax.swing.JRadioButton lineChartOption;
    private javax.swing.JRadioButton opt_barLineChart;
    private javax.swing.JRadioButton opt_effect3D;
    private javax.swing.JRadioButton opt_effectStacked;
    private javax.swing.JRadioButton opt_exploded;
    private javax.swing.JRadioButton opt_layoutH;
    private javax.swing.JRadioButton opt_layoutV;
    private javax.swing.JRadioButton opt_legend;
    private javax.swing.JRadioButton opt_multiPieCharts;
    private javax.swing.JRadioButton opt_showGridline;
    private javax.swing.JRadioButton opt_showItemLabel;
    private javax.swing.JRadioButton opt_subCategory;
    private javax.swing.JRadioButton pieChartOption;
    private javax.swing.ButtonGroup rgp_chartButtons;
    private javax.swing.ButtonGroup rgp_layoutGroup;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JPanel tabPanel_barlineChart;
    private javax.swing.JPanel tabPanel_piechart;
    private javax.swing.JTabbedPane tab_chartProperties;
    private javax.swing.JPanel tablPanel_combined;
    private javax.swing.JTextField txt_DomainLabel;
    private javax.swing.JTextField txt_RangeLabel;
    private javax.swing.JTextField txt_chartHeight;
    private javax.swing.JTextField txt_chartName;
    private javax.swing.JTextField txt_chartWidth;
    private javax.swing.JTextField txt_explodedPercent;
    private javax.swing.JTextField txt_itemLabelFormat;
    // End of variables declaration//GEN-END:variables

}
