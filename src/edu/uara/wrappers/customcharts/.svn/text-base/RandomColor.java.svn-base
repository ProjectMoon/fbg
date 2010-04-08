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

/**
 *
 * @author Darren
 */
import java.awt.Color;
import java.util.Random;
/**
 * RandomColor is a coass that allows the user to create random
 * colors and random grays.
 * 
 * @author William Austad
 * @version 5/12/03
 */
public class RandomColor
{
    
    private Random rand;

    /**
     * Constructor for objects of class RandomColor initializes the
     * random number generator
     */
    public RandomColor()
    {
        rand = new Random();
    }

    /**
     * randomColor returns a pseudorandom Color
     * 
     * @return a pseudorandom Color
     */
    public Color randomColor()
    {
        return(new Color(rand.nextInt(256), 
                         rand.nextInt(256),
                         rand.nextInt(256)));
    }
    
    /**
     * randomGray returns a pseudorandom gray Color
     * 
     * @return a pseudorandom Color
     */
    public Color randomGray()
    {
        int intensity = rand.nextInt(256);
        return(new Color(intensity,intensity,intensity));
    }
}
