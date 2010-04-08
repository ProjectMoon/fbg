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
package edu.uara.gui;

import edu.uara.FBG;
import edu.uara.FBGFlow;
import edu.uara.gui.main.MainFactbookFrame;

/**
 * The thread that creates the main graphical frame. This puts the GUI on its own thread
 * so it doesn't lock up with anything else.
 * @author jeff
 */
public class GUIThread implements Runnable {
    public void run() {
        FBG.mainFrame = new MainFactbookFrame();
        
        //set the program state to running.
        FBGFlow.setState("running");
    }
}
