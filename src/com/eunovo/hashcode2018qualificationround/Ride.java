/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eunovo.hashcode2018qualificationround;

/**
 *
 * @author Eunovo
 */
public class Ride {
    private final int rideNumber;
    private final Location startIntersection;
    private final Location endIntersection;
    private final int earliestStart;
    private final int latestFinish;
    
    public Ride(int rideNumber, int startX, int startY, int endX, int endY, 
        int earliestStart, int latestFinish) {
        this.rideNumber = rideNumber;
        this.startIntersection = new Location(startX, startY);
        this.endIntersection = new Location(endX, endY);
        if(this.startIntersection.equals(this.endIntersection)) {
            throw new IllegalArgumentException("Start intersection must"
                + " not be equalt to end intersection");
        }
        this.earliestStart = earliestStart;
        this.latestFinish = latestFinish;
    }

    public int getRideNumber() {
        return this.rideNumber;
    }
    
    public Location getStartIntersection() {
        return startIntersection;
    }

    public Location getEndIntersection() {
        return endIntersection;
    }

    public int getEarliestStart() {
        return earliestStart;
    }

    public int getLatestFinish() {
        return latestFinish;
    }
    
    public int getDistance() {
        return startIntersection.getDistance(endIntersection);
    }
    
}
