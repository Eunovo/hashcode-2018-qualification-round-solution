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
public class Location {
    public final int x;
    public final int y;
    
    public Location(int x, int y) {
        this.x = x; this.y = y;
    }
    
    public int getDistance(Location loc) {
        return Math.abs(this.x - loc.x) + Math.abs(this.y - loc.y);
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.x;
        result = 31 * result + this.y;
        return result;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Location) {
            Location loc = (Location)o;
            return (this.x == loc.x) && (this.y == loc.y);
        }
        return false;
    }
    
}
