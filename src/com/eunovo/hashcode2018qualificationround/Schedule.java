/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eunovo.hashcode2018qualificationround;

import java.util.List;

/**
 *
 * @author Eunovo
 */
public class Schedule {
    public final int nRides;
    public final Ride[] rides;
    
    public Schedule(int nRides, Ride[] rides) {
        this.nRides = nRides;
        this.rides = rides;
    }
}