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
package edu.uara.wrappers;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 *
 * @author paul
 */
public class CustomBorderGenerator implements Serializable {

    public static final long serialVersionUID = 1;

    /**
     * Class used to store the border information for each side
     */
    private class BorderInfo implements Serializable {

        public static final long serialVersionUID = 1;
        public Color borderColor;
        public int borderWidth;

        /**
         * Constructor, sets the boder to invisible
         */
        public BorderInfo() {
            borderColor = Color.black;
            borderWidth = 0;
        }

        public BorderInfo(int width, Color clr) {
            borderColor = clr;
            borderWidth = width;
        }

        public BorderInfo(BorderInfo other) {
            borderColor = other.borderColor;
            borderWidth = other.borderWidth;
        }
    }

    /**
     * Enumeration of the index of the BorderInfo
     */
    public enum BorderLocation {

        Top(0), Bottom(1), Left(2), Right(3);
        int loc;

        /**
         * Constructor to set the values for each enumerator
         * @param val: integer value for the BorderLocation
         */
        private BorderLocation(int val) {
            loc = val;
        }

        /**
         * To retrieve the value for the BoderLocation
         * @return The BoderLocation value
         */
        public int getValue() {
            return loc;
        }
    }
    private BorderInfo bInfo[];

    /**
     * Constructor, creates a new border
     */
    public CustomBorderGenerator() {
        bInfo = new BorderInfo[4];
        for (int i = 0; i < 4; i++) {
            bInfo[i] = new BorderInfo();
        }
    }

    public CustomBorderGenerator(int topWidth, int bottomWidth, int leftWidth, int rightWidth, Color clr) {
        bInfo = new BorderInfo[4];
        for (int i = 0; i < 4; i++) {
            bInfo[i] = new BorderInfo();
        }
        bInfo[BorderLocation.Top.getValue()].borderColor = clr;
        bInfo[BorderLocation.Top.getValue()].borderWidth = topWidth;
        bInfo[BorderLocation.Bottom.getValue()].borderColor = clr;
        bInfo[BorderLocation.Bottom.getValue()].borderWidth = bottomWidth;
        bInfo[BorderLocation.Left.getValue()].borderColor = clr;
        bInfo[BorderLocation.Left.getValue()].borderWidth = leftWidth;
        bInfo[BorderLocation.Right.getValue()].borderColor = clr;
        bInfo[BorderLocation.Right.getValue()].borderWidth = rightWidth;

    }

    public CustomBorderGenerator(CustomBorderGenerator gen) {
        this.bInfo = new BorderInfo[4];
        for (int i = 0; i < 4; i++) {
            this.bInfo[i] = new BorderInfo(gen.bInfo[i]);
        }
        /*bInfo[BorderLocation.Top.getValue()].borderColor = gen.getEdgeColor(BorderLocation.Top);
        bInfo[BorderLocation.Top.getValue()].borderWidth = gen.getEdgeWidth(BorderLocation.Top);
        bInfo[BorderLocation.Bottom.getValue()].borderColor = gen.getEdgeColor(BorderLocation.Bottom);
        bInfo[BorderLocation.Bottom.getValue()].borderWidth = gen.getEdgeWidth(BorderLocation.Bottom);
        bInfo[BorderLocation.Left.getValue()].borderColor = gen.getEdgeColor(BorderLocation.Left);
        bInfo[BorderLocation.Left.getValue()].borderWidth = gen.getEdgeWidth(BorderLocation.Left);
        bInfo[BorderLocation.Right.getValue()].borderColor = gen.getEdgeColor(BorderLocation.Right);
        bInfo[BorderLocation.Right.getValue()].borderWidth = gen.getEdgeWidth(BorderLocation.Right);*/

    }

    /**
     * Sets the properties for one edge of the border
     * @param loc: The location of the edge
     * @param width: The thickness of the edge
     * @param clr: the color of the edge
     * @return the full border
     */
    public Border setEdge(BorderLocation loc, int width, Color clr) {
        bInfo[loc.getValue()].borderWidth = width;
        // Temporary until multi color border problem is solved
        // bInfo[loc.getValue()].borderColor = clr;

        // Temporary until multi color border problem is solved
        for (int i = 0; i < 4; i++) {
            bInfo[i].borderColor = clr;
        }

        return generateBorder();
    }

    /**
     * Sets the color for the edge
     * @param loc: The location of the edge
     * @param clr: The color of the edge
     * @return the full border
     */
    public Border setEdgeColor(BorderLocation loc, Color clr) {
        // Temporary until multi color border problem is solved
        // bInfo[loc.getValue()].borderColor = clr;

        // Temporary until multi color border problem is solved
        for (int i = 0; i < 4; i++) {
            bInfo[i].borderColor = clr;
        }
        return generateBorder();
    }

    /**
     * Sets the width for the edge
     * @param loc: The loccation of the edge
     * @param width: The weight of the edge
     * @return the full border
     */
    public Border setEdgeWidth(BorderLocation loc, int width) {
        bInfo[loc.getValue()].borderWidth = width;
        return generateBorder();
    }

    public Color getEdgeColor(BorderLocation loc) {
        return bInfo[loc.getValue()].borderColor;
    }

    public int getEdgeWidth(BorderLocation loc) {
        return bInfo[loc.getValue()].borderWidth;
    }

    /**
     * Generates the border given the current BorderInfo
     * @return the generated border
     */
    public Border generateBorder() {
        Border finalBorder;
        finalBorder = BorderFactory.createMatteBorder(
                bInfo[BorderLocation.Top.getValue()].borderWidth,
                bInfo[BorderLocation.Left.getValue()].borderWidth,
                bInfo[BorderLocation.Bottom.getValue()].borderWidth,
                bInfo[BorderLocation.Right.getValue()].borderWidth,
                bInfo[BorderLocation.Top.getValue()].borderColor);

        return finalBorder;
    }
}
