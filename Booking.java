import java.io.*;
import java.util.*;

/* maybe make constants out of it */
class Global {
    public static int speed = 10;
    public static int numberOfCabs = 5;
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
    public String toString(){
        return this.coordinate[0] + " " + this.coordinate[1];
    }
}

class Landmark extends Location{
}

class City{
    Location[] objects;
    
    @Override
    public String toString(){
    	/* show the grid points, at each point show the object which are at that point 
    	   this function will be called multiple times, to show the map animation (driver moving from point A to point B with/without customer)
    	*/
    }
}

interface Drives {
    public float averageRating();
    public void rate(int rate);
}

class Driver{
    String id;
    String password;
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
    public bool login(String id, String passowrd){
        return id == this.id && password == this.password;
    }
}

class Cab extends Location {
    public int uniqueRegistration;
    public int ratingCount;
    public float ratingAverage;
    public boolean busy;

    Cab() {
        this.ratingAverage = 0;
        this.ratingCount = 0;
    }
    public void searchBookings(){
        
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
    String cabId;
    Customer customer;
    Location pickup;
    Location destination;
    int fare;
    
    Booking(String cabId, Customer customer, Location pickup, Location destination){
        this.cabId = cabId;
        this.customer = customer;
        this.pickup = pickup;
        this.destination = destination;
    }

    public void setDestination(Location destination){
        this.destination = destination;
    }
    public void setPickup(Location pickup){
        this.pickup = pickup;
    }
    void saveFare(int fare){
        this.fare = fare;
    }

    public int generateFare(){
        int fare;
        /* do calculations for fare */

        saveFare(fare);
    }
    public void saveBooking(){
        /* save it in a file named by the driver's name */
        File file = new File("bookings/" + cab.uniqueRegistration + ".txt");
        if (!file.exists()) { 
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write("Test data");
            writer.close();
            System.out.println("Booking successful");
        } else {
            System.out.println("Cab booked already :(");
        } 
    }
    public String toString(){
        return cabId + " " + customer.id + " " + pickup.toString() + " " + destination.toString() + " " + fare;
    }
    public static Booking fromString(String encoding){
        String[] properties =  encoding.split(" ");
        Customer customer = new Customer(properties[1]);
        Location destination = new Location();
        Location pickup = new 

    }

}

class Customer extends Location {
    String id;
    String password;
    int money; /* in paise , not rupees */
    
    Customer(String id){
        /* the string must purely be a number */
        this.id = Integer.parseInt(id);
    }
    public void book(){}
    public void rate(){}
    public bool login(String id, String passowrd){
        return id == this.id && password == this.password;
    }
}

class Solution{
	public static void join(Scanner sc, HashMap<String, String> details){
		System.out.print("Enter username: ");
		String username = sc.nextInt();
		
		System.out.print("Enter password: ");
		String password = sc.nextInt();
		
		/* check hashmap if that user exists. if it does then login, else signup */
	}
    public static void intro(){
        System.out.println("Join as? [Driver (D) / User(U)]");
        System.out.print(">");
    }
    public static void main(String[] args){
    	Scanner sc = new Scanner(System.in);
    	int option = 0;
    	
    	/* option as 2 means exit */
	    do{
            /* make the login/signup page 
               
               if login as driver:
               1. show bookings
               2. accept a booking
               3. show the driving animation of all the grids
               4. drive ended animation
               
               if login as user
               1. accept user destination
               2. show them the booking procedure (accept destination, pay fare)
               3. 
               
               show help also, if user is confused*/


        } while (option != 2);
    }
}

