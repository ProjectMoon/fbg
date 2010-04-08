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
package edu.uara.tables;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/**
 * Used to generate images of JTables
 * @author Paul Halvorsen
 */
public class TableImageGenerator {
    static private TableImageGenerator tigInstance = null;
    private JFrame frame;
    private JPanel panel;
    private JTable prevTable;
    
    /**
     * Constructor to set up a new frame and panel which are needed to generate the image
     */
    private TableImageGenerator() {
        frame = new JFrame();
        panel = new JPanel();
        prevTable  = new JTable();
        panel.add(prevTable);
        frame.add(panel);
        frame.setVisible(true);
    }
    
    /**
     * Deconstructor, only call after all images have been generated, otherwise a new frame and panel will need to be created.
     */
    public void destroyTableImageGenerator() {
        frame.dispose();
        prevTable = null;
        tigInstance = null;
    }
    
    /**
     * Get the instance of the table image generator.
     * There is only one for now, there will soon be new implimentation that should allow several images to be gnerated at one time.
     * @return the instance of the TableImageGenerator
     */
    static public TableImageGenerator getInstance() {
        if(tigInstance == null) {
            tigInstance = new TableImageGenerator();
            return tigInstance;
        }
        else { return tigInstance; }
    }
    
    /**
     * Creates a png image given a table and file name
     * @param table: The JTable you want exported as a png
     * @param filePath: The path and name of the image (soon to only be image name)
     */
    public void generateImage(JTable table, String filePath) {
        frame.remove(panel);
        panel.remove(prevTable);
        panel.add(table);
        frame.add(panel);
        frame.repaint();
        prevTable = table;
        frame.setVisible(true);
        
        //frame.setSize(1000, 1000);

        // Get the table header component
        JTableHeader tableHeaderComp = table.getTableHeader();

        // Calculate the total width and height of thr table iwth the header
        int totalWidth = tableHeaderComp.getWidth() + table.getWidth();
        int totalHeight = tableHeaderComp.getHeight() + table.getHeight();
        
        // Create a BufferedImage object of total width and height
        BufferedImage tableImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);

        // Get the graphics object from the image
        Graphics2D g2D = (Graphics2D)tableImage.getGraphics();

        // Paint the table header on image graphics
        tableHeaderComp.paint(g2D);

        // Now translate the origin to (0, height of table header)
        g2D.translate(0, tableHeaderComp.getWidth());

        // Now paint the table on the image graphics
        table.paint(g2D);

        // Output image to file
        File outFile = new File(filePath);
        try {
            ImageIO.write(tableImage, "png", outFile);
        }
        catch(java.io.IOException e) {
            System.err.print("Error wrinting file " + filePath + "\n");
        }
    }
    
    /**
     * Writes the specified string into a png file.
     * @param footnote
     * @param filepath
     */
    public void generateFootnote(String footnote, String filepath) {
        
        //First we figure out height based on number of lines
        int height = 20;
        for (int c = 0; c < footnote.length(); c++) {
            if (footnote.charAt(c) == '\n')
                height += 30;
        }
        
        String[] lines = footnote.split("\n");
        int width = 0;
        //Now we figure out width based on longest lines
        for (int c = 0; c < lines.length; c++) {
            if (lines[c].length() > width) width = lines[c].length();
        }
        
        width = (int)(width * 5.8);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.setBackground(Color.BLACK);
       
        Font f = new Font("Verdana", Font.PLAIN, 10);
        g.setFont(f);

        //Draw each line
        int currpos = 10;
        for (int c = 0; c < lines.length; c++) {
            g.drawString(lines[c], 0, currpos);
            currpos += 11;
        }
        g.dispose();
        
        File outFile = new File(filepath);
        try {
            ImageIO.write(img, "png", outFile);
        }
        catch (java.io.IOException e) {
            System.err.println("Error writing file " + filepath);
        }
    }
}