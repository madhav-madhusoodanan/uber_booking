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
    public static int mod(int number){
        return number > 0 ? number : -1 * number;
    }
}

class Location {
    int coordinate[];
    public String id;

    Location(){
        coordinate[0] = 0;
        coordinate[1] = 0;
    }
    Location(int x, int y){
        coordinate[0] = x;
        coordinate[1] = y;
    }

    public int distance(int x2, int y2) {
        return Global.mod(x2 - this.coordinate[0]) + Global.mod(y2 - this.coordinate[1]);
    }
    public int distance(Location location) {
        return Global.mod(location.coordinate[0] - this.coordinate[0]) + Global.mod(location.coordinate[1] - this.coordinate[1]);
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
        String[] outputs = encoding.split(" ");
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

    public String toString(){
    	/* show the grid points, at each point show the object which are at that point 
    	   this function will be called multiple times, to show the map animation (driver moving from point A to point B with/without customer)
    	*/

        String str = new String();

        for(int i = 0; i < Global.gridSize; i++){
            for(int j = 0; j < Global.gridSize; j++){
                str += "*";
                for(Location obj:this.objects){
                    if(obj.coordinate[0] == i && obj.coordinate[1] == j){
                        str += "," + obj.id;
                    }
                }
                str += "\t\t";
            }
            str += "\n";
        }
        return str;
    }

    public void addObject(Location obj){
        objects.add(obj);
    }
    public static void update(){
        /* this line clears the terminal */
        System.out.print("\033[H\033[2J");
		System.out.flush();

        this.toString();
        try
        {
            /* wait for 1 second */
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
    public void drive(Location location);
}

class Driver extends Location implements Drives {
    String id;
    String password;
    ArrayList<Integer> ratings;
    String cabid;

    @Override
    public float averageRating(){
        float rating = 0;
        for(int i:ratings){
            rating += i/(ratings.size());
        }
        return rating;
    }

    @Override
    public void rate(int rate){
        ratings.add(rate);
    }
    public boolean login(String id, String passowrd){
        return id == this.id && password == this.password;
    }

    @Override
    public void drive(Location destination){
        while(this.distance(this.coordinate[0], destination.coordinate[1]) != this.distance(bk.destination)){
            City.update();
            int update = (destination.coordinate[0] > this.coordinate[0]) ? 1 : -1;
            this.setNewLocation(this.coordinate[0] + update, this.coordinate[1]);
        }

        while(this.distance(bk.destination) != 0){
            City.update();
            int update = (destination.coordinate[1] > this.coordinate[1]) ? 1 : -1;
            this.setNewLocation(this.coordinate[0], this.coordinate[1] + update);
        }
    }
}

class Cab extends Location {
    public Customer cust;
    public int ratingCount;
    public float ratingAverage;
    public boolean busy;
    String driverId;

    Cab() {
        this.ratingAverage = 0;
        this.ratingCount = 0;
    }
    public void occupy(Customer cust){
        this.cust = cust;
        busy = true;
    }
    public void free(){
        this.cust = null;
        busy = false;
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
    public static ArrayList<Booking> bookings;
    
    Booking(String cabId, Customer customer, Location pickup, Location destination){
        this.cabId = cabId;
        this.customer = customer;
        this.pickup = pickup;
        this.destination = destination;
        this.generateFare();
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
        saveFare(CabManager(this.pickup, this.destination));
    }
    public void saveBooking(){
        try {
            Booking.bookings.add(this);
            System.out.println("Booking successful");
        } catch(Exception e) {
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
    public boolean login(String id, String passowrd){
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
    static void menu(){
        
    }
    static boolean intro(Scanner sc){
        System.out.println("Join as? [Driver (D) / User(U)]");
        System.out.print(">");

        char reply = sc.next();
        return (reply == 'D' || reply == 'd');
    }
    static int handleCustomer(Scanner sc, ArrayList<Customer> customers, ArrayList<Booking> bookings){
        /* handle the customer */
        Customer cust;
        boolean tryAgain = false;

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
            Cab cab = CabManager.getCab();
            Booking booking = new Booking(cab.id, cust, cust, dest);
            System.out.println("\nBooking successful");
            System.out.println("Wait for cab");
            System.out.println("Press 2 to logout, or q to quit the program");

            if(sc.next().trim().equals("q")) System.exit(0);

        } catch(NoCabFoundException e){
            System.out.println("No cab available now :(");
        }
        return 0;
        /* making a booking object */
    }
    static void handleDriver(Scanner sc, ArrayList<Driver> drivers, ArrayList<Booking> bookings){
        /* handle the driver and the cab */
        Driver driv;
        Booking bk;
        boolean tryAgain = false;

        do {
            try {
                driv = join(sc, drivers);
                tryAgain = false;
            } catch (NoUserFoundException e){
                tryAgain = true;
            }
        } while (tryAgain && cust != null);

        System.out.println("Your booking:");
        for(Booking bk:Booking.bookings){
            if(bk.cabId == driv.id) System.out.println(bk);
        }
        System.out.println("");
        System.out.print("Confirm mission? [y/n]");
        if(sc.next().trim().equals("n")) return;

        driv.drive(bk.pickup);
        driv.drive(bk.destination);

        System.out.println("Destination reached");
        System.out.println("Press 2 to logout, or q to quit the program");

        if(sc.next().trim().equals("q")) System.exit(0);
        

        /* try{
            Cab cab = CabManager.getCab();
            Booking booking = new Booking(cab.id, cust, cust, dest);
            System.out.println("\nBooking successful");
            System.out.println("Wait for cab");
            sc.next();
        } catch(NoCabFoundException e){
            System.out.println("No cab available now :(");
        }
        return 0; */
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

        } while (true);
    }
}

