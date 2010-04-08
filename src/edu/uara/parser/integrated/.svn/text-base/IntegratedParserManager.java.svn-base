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
package edu.uara.parser.integrated;

import edu.uara.gui.Utilities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.SwingWorker;

/**
 *
 * @author jh44695
 */
public class IntegratedParserManager {
    class IntegratedParserThread extends SwingWorker {
        private ParseableFile f;
        private Parser p;

        public IntegratedParserThread(ParseableFile f, Parser p) {
            this.f = f;
            this.p = p;
        }

        @Override
        protected Object doInBackground() throws Exception {
            try {
                //create a batch updater for this file
                BatchUploader uploader = new BatchUploader(f.getType(), f.getYear());

                //create buffered reader for input
                BufferedReader br = new BufferedReader(new FileReader(f.getFile()));
                String line = "";

                //get total amount of lines in file
                int lines = 0;
                while ((line = br.readLine()) != null)
                    lines++;

                br.close();

                //get "chunks" amount for progress bar.
                int chunkNum = lines / 100; //this task takes 100% of the swing worker
                int chunkCount = 0;
                
                //this resets the stream to the beginning
                br = new BufferedReader(new FileReader(f.getFile()));

                //read each line and transform it
                while ((line = br.readLine()) != null) {
                    //transform to raw data
                    List<RawData> rawData = p.parse(line);

                    //send to batch updater
                    uploader.put(rawData);

                    //update progress
                    chunkCount++;
                    if (chunkCount >= chunkNum) {
                        int prog = this.getProgress();
                        if (prog + 1 > 100)
                            this.setProgress(100);
                        else
                            this.setProgress(this.getProgress() + 1);
                        
                        chunkCount = 0;
                    }
                }

                //finish the batch
                uploader.finishBatch();
            }
            catch (Exception e) {
                Utilities.displayError(null, "Upload Error", "There was an error uploading. Possible reasons:\n1. Wrong file format chosen.\n2. Not connected to a server.");
                return null;
            }
            return null;
        }

        @Override
        protected void done() {
            IntegratedParserManager.this.signalDone(this);
        }
    }

    private AISParser aisParser = new AISParser();
    private DISParser disParser = new DISParser();
    private EDSParser edsParser = new EDSParser();
    private EISParser eisParser = new EISParser();
    private POPParser popParser = new POPParser();
        
    private Stack<ParseableFile> filesToParse;
    private ArrayList<IntegratedParserThread> threads = new ArrayList<IntegratedParserThread>();
    private int filesParsing = 0;
    private int filesDone = 0;
    private int totalFiles;

    public static final int FILE_LIMIT = 1;

    public IntegratedParserManager(ParseableFile ... files) {
        filesToParse = new Stack<ParseableFile>();
        totalFiles = files.length;
        
        for (ParseableFile f : files)
            filesToParse.push(f);
    }

    public IntegratedParserManager(Stack<ParseableFile> files) {
        filesToParse = files;
        totalFiles = files.size();
    }

    public void parse() {
        while (!filesToParse.isEmpty() && filesParsing < FILE_LIMIT) {
            parse(filesToParse.pop());
            //FBG.mainFrame.signalServerViewUpdate();
            filesParsing++;

            //This ensures that other parsers will have a unique random
            //seed
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException e) {}
        }
    }

    private void signalDone(IntegratedParserThread t) {
        threads.remove(t);
        filesParsing--;
        filesDone++;
        if (!filesToParse.isEmpty()) {
            System.out.println("Parsing another file!");
            parse(filesToParse.pop());
            //FBG.mainFrame.signalServerViewUpdate();
            filesParsing++;
            
            //This ensures that other parsers will have a unique random
            //seed
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException e) {}
        }
    }

    private boolean parse(ParseableFile f) {
        System.out.println("Starting parser for " + f);
        if (f.getType().equals("AIS"))
            return doParse(f, f.getYear(), aisParser);
        else if (f.getType().equals("DIS")) {
            disParser.setYear(f.getYear());
            return doParse(f, f.getYear(), disParser);
        }
        else if (f.getType().equals("EDS"))
            return doParse(f, f.getYear(), edsParser);
        else if (f.getType().equals("EIS"))
            return doParse(f, f.getYear(), eisParser);
        else if (f.getType().equals("POP"))
            return doParse(f, f.getYear(), popParser);

        return false;
    }

    private boolean doParse(ParseableFile f, int year, Parser p) {
        IntegratedParserThread worker = new IntegratedParserThread(f, p);
        worker.execute();
        threads.add(worker);
        return true;
    }

    public int getTotalProgress() {
        int progress = (filesDone * 100) / totalFiles;
        int threadAmt = threads.size();

        for (int c = 0; c < threadAmt; c++) {
            IntegratedParserThread t = threads.get(c);
            progress += (t.getProgress() / totalFiles);
        }

        if (progress > 100) progress = 100;
        return progress;
    }
    
    public boolean isParsing() {
        return (threads.size() > 0);
    }
}
