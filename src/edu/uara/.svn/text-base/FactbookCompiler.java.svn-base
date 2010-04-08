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
package edu.uara;

import java.util.ArrayList;

import edu.uara.project.FactbookProject;
import edu.uara.tableeditor.TableFigure;
import edu.uara.tableeditor.TableObject;
import edu.uara.tables.TableImageGenerator;
import java.io.File;

public class FactbookCompiler {
	public static void compileFactbook(FactbookProject project, String path) {
        System.out.println("Compiling Factbook to: " + path);
		ArrayList<TableObject> objs = project.getTableObjects();
		TableImageGenerator generator = TableImageGenerator.getInstance();
		for(TableObject to : objs) {
			System.out.println("** Compiling: " + to.getName());
            String tablePath = path + to.getName() + FBGConstants.SEP;

            //Generate table
            new File(tablePath).mkdir();
			generator.generateImage(to.getTable(), tablePath + to.getName() + ".png");

            //Generate figures
            for (TableFigure figure : to.getFigures()) {
                //compile a figure.
                figure.updateChart(to);//update charts with current data
                figure.compileFigure(tablePath);
            }
		}
		generator.destroyTableImageGenerator();
        System.out.println("---FACTBOOK COMPILED---");
	}

	public static void compileTable(TableObject obj, String path) {
		TableImageGenerator generator = TableImageGenerator.getInstance();
		generator.generateImage(obj.getTable(), path + obj.getName() + ".png");
                generator.generateFootnote(obj.getFootnote(), path + "Footnote_" + obj.getName() + ".png");
		generator.destroyTableImageGenerator();
	}
}
