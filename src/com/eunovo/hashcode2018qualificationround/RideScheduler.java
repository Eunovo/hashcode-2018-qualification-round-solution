/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eunovo.hashcode2018qualificationround;

import com.eunovo.hashcode2018qualificationround.HashCode2018Qualification.Problem;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eunovo
 */
public class RideScheduler {
    
    
    public List<Schedule> startSimulation() {
        Problem problem = Problem.getInstance();
        // create fleet
        Car[] fleet = new Car[problem.fleetSize];
        for(int i = 0; i < problem.fleetSize; i++) {
            fleet[i] = new Car(0, 0); // cars start at [0,0]
        }
        
        for(int t = 0; t < problem.nSteps; t++) {
            for(Car car: fleet) {
                if(car.state == State.IDLE) {
                    if(!car.assignRide(t, problem.getRides())) {
                        // no rides to assign to car
                        continue; // move to next car
                    }
                }
                car.move(t);
            }
        }
        
        List<Schedule> schedules = new ArrayList();
        long score = 0;
        for(Car car: fleet) {
            score += car.score;
            score += (car.onTime * problem.bonusPerRide);
            schedules.add(new Schedule(car.assignedRides, 
                car.completedRides.toArray(new Ride[car.assignedRides])));
        }
        System.out.println("Score: "+score);
        return schedules;
    }
    
    
    private class Car {
        Location loc;
        Ride target;
        State state;
        int assignedRides;
        int onTime;
        List<Ride> completedRides;
        int score;
        
        Car(int x, int y) {
            this.loc = new Location(x, y);
            this.state = State.IDLE;
            this.target = null;
            this.assignedRides = 0;
            completedRides = new ArrayList();
            score = 0;
            onTime = 0;
        }
        
        
        /**
         * 
         * @param currentTimeStep the current timestep of the simulation
         * @param rides the list of existing rides
         * @throws IllegalStateException when the car is not free
         * @return true is a ride was  assigned else false
         */
        public boolean assignRide(int currentTimeStep, Ride[] rides) {
            if(state != State.IDLE) {
                throw new IllegalStateException("This car is busy");
            }
            Ride selected = null;
            int mostUrgent = Integer.MAX_VALUE;
            for(Ride ride: rides) {
                int urgency = calculateUrgency(currentTimeStep, ride);
                if(urgency < 0) continue; // too late
                if(mostUrgent > urgency) {
                    selected = ride;
                    mostUrgent = urgency;
                }
            }
            if(selected == null) return false;
            this.target = selected;
            Problem.removeRide(selected);
            state = State.BUSY;
            return true;
        }

        private int calculateUrgency(int currentTimeStep, Ride ride) {
            int distanceEI = this.loc.getDistance(ride.getEndIntersection());
            int latestFinish = ride.getLatestFinish();
            return latestFinish - (distanceEI + currentTimeStep);
        }
        
        public void move(int currentTimeStep) {
            if(this.state == State.IDLE) {
                throw new IllegalStateException("This car is idle");
            }
            
            Location destination = null;
            if(this.state == State.RIDEONGOING) {
                destination = this.target.getEndIntersection();
            }else if(!this.loc.equals(this.target.getStartIntersection())) {
                destination = this.target.getStartIntersection();
            }else if(currentTimeStep < this.target.getEarliestStart()) {
                // too early, must wait
                return;
            }
            else {
                destination = this.target.getEndIntersection();
                this.state = State.RIDEONGOING;
            }
            
            if(this.loc.equals(this.target.getStartIntersection()) &&
              currentTimeStep == this.target.getEarliestStart()) {
                this.onTime++;
            }
            
            if(destination.x != this.loc.x) {
                int newX = (destination.x > this.loc.x) ? this.loc.x + 1: this.loc.x - 1;
                this.loc = new Location(newX, this.loc.y);
            }else if(destination.y != this.loc.y) {
                int newY = (destination.y > this.loc.y) ? this.loc.y + 1: this.loc.y - 1;
                this.loc = new Location(this.loc.x, newY);
            }
            
            if(this.loc.equals(this.target.getEndIntersection())) {
                // ride has ended
                this.state = State.IDLE;
                this.completedRides.add(target);
                this.assignedRides++;
                this.score += this.target.getDistance();
                this.target = null;
            }
            
        }
        
        
        
    }
    
    private enum State {
        IDLE, BUSY, RIDEONGOING;
    }
    
}
