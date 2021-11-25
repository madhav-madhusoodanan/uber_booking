import java.io.*;
import java.util.*;
import java.time.LocalTime;
/* maybe make constants out of it */

class NoUserFoundException extends Exception{}  
class NoCabFoundException extends Exception{}
class Global {
    public static final int speed = 10;
    public static final int numberOfCabs = 5;
    public static final int gridSize = 8;
}

class Location {
    int coordinate[2];

    int mod(int number){
        return number > 0 ? number : -1 * number;
    }
    Location(){
        coordinate[0] = 0;
        coordinate[1] = 0;
    }
    Location(int x, int y){
        coordinate[0] = x;
        coordinate[1] = y;
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
    public static Location fromString(String encoding){
        String[] outputs = encoding.split(' ');
        int x = Integer.parseInt(outputs[0]);
        int y = Integer.parseInt(outputs[1]);
        return new Location(x, y);
    }
}

class Landmark extends Location{
}

class City{
    public ArrayList<Location> objects;
    
    City(){
        this.objects = new ArrayList<Location>();
    }

    @Override
    public String toString(){
    	/* show the grid points, at each point show the object which are at that point 
    	   this function will be called multiple times, to show the map animation (driver moving from point A to point B with/without customer)
    	*/
        for(int i = 0; i < Global.gridSize; i++){
            for(int j = 0; j < Global.gridSize; j++){
                StringBuffer str = new StringBuffer();
                str += "*";
                for(Location obj:this.objects){
                    if(obj.coordinate[0] == i && obj.coordinate[1] == j){
                        str += "," + obj.id;
                    }
                }
                System.out.print(str + "\t\t");
            }
            System.out.println("");
        }
    }

    public void addObject(Location obj){
        objects.add(obj);
    }
    public void update(){
        /* wait for 1 second */
        toString();
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
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
    String cabUniqueRegistration;

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
    String driverId;

    Cab() {
        this.ratingAverage = 0;
        this.ratingCount = 0;
    }
    public void searchBookings(){
        
    }
    public void toggleOccupied(){
        busy = !busy;
    }
    public boolean isOccupied(){
        return busy;
    }
}

class CabManager {
    public static ArrayList<Cab> cabs;
    CabManager(){
        this.cabs = new ArrayList<Cab>();
    }
    public static double fare(Location pickup, Location destination) {
        int busy = 0;
        double rate = LocalTime.now().isAfter(LocalTime.of(16, 0, 0)) ? 0.2 : 0.1;
        for(Cab cab:cabs){
            if(cab.isOccupied()) busy += 1;
        }
        fare = (pickup.distance(destination)) * busy * rate;
        return fare;
    }
    public static Cab getCab() throws NoCabFoundException{
        for( Cab cab:cabs ){
            if(!cab.isOccupied()) return cab;
        }
        throw new NoCabFoundException();
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
    static <T> T join(Scanner sc, ArrayList<T> details) throws NoUserFoundException {
		System.out.print("Enter username: ");
		String username = sc.next().trim();
		
		System.out.print("Enter password: ");
		String password = sc.next().trim();

        for(int i = 0; i < details.size(); i++){
            T detail = details.get(i);
            if(detail.username.equals(username) && detail.password.equals(password)) return detail;

        }
		throw new NoUserFoundException();
		/* check hashmap if that user exists. if it does then login, else signup */
	}
    static void help(){
        /* print halp message */
    }
    static void menu(){
        
    }
    static boolean intro(Scanner sc){
        System.out.println("Join as? [Driver (D) / User(U)]");
        System.out.print(">");

        char reply = sc.next();
        return (reply == 'D' || reply == 'd');
    }
    static void handleCustomer(Scanner sc, ArrayList<Customer> customers, ArrayList<Booking> bookings){
        /* handle the customer */
        Customer cust;
        bool tryAgain = false;

        do {
            try {
                cust = join(sc, customers);
                tryAgain = false;
            } catch (NoUserFoundException e){
                tryAgain = true;
            }
        } while (tryAgain && cust != null);

        System.out.println("Type the destination coordinates:");
        String destination = sc.nextLine().trim();
        Location dest = Location.fromString(destination);
        try{
            
            Booking booking = new Booking();
        } catch(NoCabFoundException e){
            System.out.println("No cab available now :(");
        }
        /* making a booking object */
    }
    static void handleDriver(ArrayList<Driver> drivers, ArrayList<Booking> bookings){
        /* handle the driver and the cab */
    }
    public static void main(String[] args){
    	Scanner sc = new Scanner(System.in);
    	int option = 0;
    	ArrayList<Customer> customers = new ArrayList<Customer>();
        ArrayList<Driver> drivers = new ArrayList<Customer>();
        ArrayList<Booking> bookings = new ArrayList<Booking>();
    
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
               
               show help also, if user is confused
            */

            boolean isCustomer = intro();
            if(isCustomer) handleCustomer(sc, customers, bookings);
            else handleDriver(sc, drivers, bookings);

        } while (option != 2);
    }
}

