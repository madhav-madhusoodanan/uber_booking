//henlo


import java.util.*;
/* maybe make constants out of it */
class Global {
    public static int speed = 10;
    public static int number_of_cabs = 5;
}

class Location {
    int coordinate[];

    int mod(int number){
        return number > 0 ? number : -1 * number;
    }

    public int distance(int x2, int y2) {
        return mod(x2 - this.coordinate[0]) + mod(y2 - this.coordinate[1]);
    }
    public int distance(Location location) {
        return mod(location.coordinate[0] - this.coordinate[0]) + mod(location.coordinate[1] - this.coordinate[1]);
    }
    public void setNewLocation(int x2, int y2){
    coordinate[0] = x2;
    coordinate[1] = y2;
    }
    public void setNewLocation(Location location){
    coordinate[0] = location.coordinate[0];
    coordinate[1] = location.coordinate[1];
    }
}

interface Drives {
    public float averageRating();
    public void rate(int rate);
}

class Driver{
    int id;
    ArrayList<Integer> ratings;


    public void retrieveBooking(){
        /* make a file for a driver
           and store his bookings in that */
    }
    public float averageRating(){
        float rating = 0;
        for(int i:ratings){
            rating += i/(ratings.size());
        }
        return rating;
    }
    public void rate(int rate){
        ratings.add(rate);
    }
}




class Cab extends Location {
    public int uniqueRegistration;
    public int rating_count;
    public float rating_average;
    public boolean busy;

    Cab() {
        this.rating_average = 0;
        this.rating_count = 0;
    }
}

class Cab_manager {
    public static Cab[] cabs;
    public static float fare() {
        // fare = distance * (total - free)cabs * a time based variable
        return 0;
    }
}

class Booking{
    Cab[] cabs;
    
}

class Solution{
    public static void main(String[] args){

    }
}

/* Logic

1.  Location is an abstract class that will be inherited by all cabs, landmarks and the user */
