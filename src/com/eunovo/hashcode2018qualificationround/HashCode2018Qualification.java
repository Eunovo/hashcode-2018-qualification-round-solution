/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eunovo.hashcode2018qualificationround;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eunovo
 */
public class HashCode2018Qualification {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        if(args.length != 2) {
            System.out.println(
                "Usage: java -jar HashCode2018Qualification.jar problem-path submission-path"
            );
            System.exit(0);
        }
        
        String inputFilePath = args[0];
        String outputFilePath = args[1];
        
        try {
            readFile(inputFilePath);
            List<Schedule> solution = new RideScheduler().startSimulation();
            writeToFile(solution, outputFilePath);
        } catch (IOException ex) {
            Logger.getLogger(HashCode2018Qualification.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void readFile(String filePath) throws IOException {
        File inputFile = new File(filePath);
        String name = inputFile.getName();
        String ext = name.substring(name.lastIndexOf(".")+1);
        if(!ext.equals("in")) {
            throw new IllegalArgumentException("input file must be an input file");
        }
        Object[] lines = Files.lines(inputFile.toPath()).sequential().toArray();
        String paramString = (String)lines[0];
        String[] paramArray = paramString.split(" ");
        if(paramArray.length != 6) {
            throw new IllegalArgumentException("Invalid input file");
        }
        // 3 rows, 4 columns, 2 vehicles, 3 rides, 2 bonus and 10 steps
        int nRows = Integer.parseInt(paramArray[0]);
        int nCols = Integer.parseInt(paramArray[1]);
        int fleetSize = Integer.parseInt(paramArray[2]);
        int nRides = Integer.parseInt(paramArray[3]);
        int bonus = Integer.parseInt(paramArray[4]);
        int nSteps = Integer.parseInt(paramArray[5]);
        Problem.createInstance(nRows, nCols, fleetSize, nRides, bonus, nSteps);
        
        for(int r = 1; r <= nRides; r++) {
            String line = (String)lines[r];
            String[] tokens = line.split(" ");
            int startX = Integer.parseInt(tokens[0]);
            int startY = Integer.parseInt(tokens[1]);
            int endX = Integer.parseInt(tokens[2]);
            int endY = Integer.parseInt(tokens[3]);
            int earliestStart = Integer.parseInt(tokens[4]);
            int latestFinish = Integer.parseInt(tokens[5]);
            Problem.addRide(new Ride(r-1, startX, startY, endX, endY,
                earliestStart, latestFinish));
        }
        
    }
    
    public static void writeToFile(List<Schedule> soln, String targetPath) throws FileNotFoundException{
        try (FileOutputStream output = new FileOutputStream(targetPath)) {
            soln.stream().map((schedule) -> {
                String line = ""+schedule.nRides+" ";
                for(Ride ride: schedule.rides) {
                    line += ride.getRideNumber()+" ";
                }
                line += "\r\n";
                System.out.print(line);
                return line;
            }).map((line) -> {
                return line.getBytes();
            }).forEach((bytes) -> {
                try {
                    output.write(bytes);
                } catch (IOException ex) {
                    Logger.getLogger(HashCode2018Qualification.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            });
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(HashCode2018Qualification.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public static class Problem {
        private static Problem instance = null;
        public final int nCols;
        public final int nRows;
        public final int fleetSize;
        public final int nRides;
        public final int bonusPerRide;
        public final int nSteps;
        private final ArrayList<Ride> rides;
        
        private Problem(int nRows, int nCols, int fleetSize, int nRides, 
            int bonusPerRide, int nSteps) {
            this.nCols = nCols;
            this.nRows = nRows;
            this.fleetSize = fleetSize;
            this.nRides = nRides;
            this.nSteps = nSteps;
            this.bonusPerRide = bonusPerRide;
            this.rides = new ArrayList();
        }
        
        
        public Ride[] getRides() {
            return this.rides.toArray(new Ride[this.rides.size()]);
        }
        
        public static void createInstance(int nRows, int nCols, int fleetSize, int nRides, 
            int bonusPerRide, int nSteps) {
            instance = new Problem(nRows, nCols, fleetSize, nRides, bonusPerRide, nSteps);
        }
        
        public static Problem getInstance() {
            return instance;
        }
     
        public static void addRide(Ride ride) {
            instance.rides.add(ride);
        }
        
        public static void removeRide(Ride ride) {
            instance.rides.remove(ride);
        }
        
    }
    
}
