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



class City{
    public static ArrayList<Location> objects;

    public static String stringify(){
    	/* show the grid points, at each point show the object which are at that point 
    	   this function will be called multiple times, to show the map animation (driver moving from point A to point B with/without customer)
    	*/

        String str = new String();

        for(int i = 0; i < Global.gridSize; i++){
            for(int j = 0; j < Global.gridSize; j++){
                str += "*";
                for(Location obj:objects){
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

        stringify();
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

interface Entity {
    public boolean verify(String username, String password);
}

interface Drives {
    public float averageRating();
    public void rate(int rate);
    public void drive(Location location);
}

class Driver extends Location implements Drives, Entity {
    String password;
    ArrayList<Integer> ratings;
    String cabid;

    Driver(String id, String password){
        super();
        super.id = id;
        this.password = password;
        coordinate = new int [2];
    }

    public static Driver join(Scanner sc, ArrayList<Driver> details) throws NoUserFoundException {
		System.out.print("Enter username: ");
		String username = sc.next().trim();

		Driver detail;
        
		System.out.print("Enter password: ");
		String password = sc.next().trim();

        for(int i = 0; i < details.size(); i++){
            detail = details.get(i);
            if(detail.verify(username, password)) {
		        System.out.print("successfully logged in");
                return detail;
            }
        }
        detail = new Driver(username, password);
		details.add(detail);
 
		System.out.print("successfully signed up");
        return detail;

		/* check hashmap if that user exists. if it does then login, else signup */
	}

    @Override
    public float averageRating(){
        float rating = 0;
        for(int i:ratings){
            rating += i/(ratings.size());
        }
        return rating;
    }

    @Override
    public boolean verify(String username, String password){
        return username.equals(this.id) && this.password.equals(password);
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
        while(this.distance(this.coordinate[0], destination.coordinate[1]) != this.distance(destination)){
            City.update();
            int update = (destination.coordinate[0] > this.coordinate[0]) ? 1 : -1;
            this.setNewLocation(this.coordinate[0] + update, this.coordinate[1]);
        }

        while(this.distance(destination) != 0){
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
    public static double generateFare(Location pickup, Location destination) {
        int busy = 0;
        double rate = LocalTime.now().isAfter(LocalTime.of(16, 0, 0)) ? 0.2 : 0.1;
        for(Cab cab:cabs){
            if(cab.isOccupied()) busy += 1;
        }
        double fare = (pickup.distance(destination)) * busy * rate;
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
    double fare;
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
    void saveFare(double fare){
        this.fare = fare;
    }

    public void generateFare(){
        saveFare(CabManager.generateFare(this.pickup, this.destination));
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

}

class Customer extends Location implements Entity {
    String password;
    double money; 
    
    Customer(String id, String password){
        super();
        super.id = id;
        this.password = password;
        coordinate = new int [2];
    }
    public void book(){}
    public void rate(){}
    public boolean login(String id, String passowrd){
        return id == this.id && password == this.password;
    }
    public void pay(double fare){
        this.money -= fare;
    }

    @Override
    public boolean verify(String username, String password){
        return username.equals(id) && this.password.equals(password);
    }

    static Customer join(Scanner sc, ArrayList<Customer> details) throws NoUserFoundException {
		System.out.print("Enter username: ");
		String username = sc.next().trim();

		Customer detail;
        
		System.out.print("Enter password: ");
		String password = sc.next().trim();
        for(int i = 0; i < details.size(); i++){
            detail = details.get(i);
            if(detail.verify(username, password)) {
		        System.out.print("successfully logged in");
                return detail;
            }
        }

        detail = new Customer(username, password);
		details.add(detail);
 
		System.out.print("successfully signed up");
        return detail;

		/* check hashmap if that user exists. if it does then login, else signup */
	}
}

class Solution{
    
    static void menu(){
        
    }
    static boolean intro(Scanner sc){
        System.out.println("Join as? [Driver (D) / User(U)]");
        System.out.print(">");
        String reply = sc.next().trim();
        return (reply.equals("D") || reply.equals("d"));
    }
    static int handleCustomer(Scanner sc, ArrayList<Customer> customers, ArrayList<Booking> bookings){
        /* handle the customer */
        Customer cust;
        boolean tryAgain = false;

        do {
            try {
                cust = Customer.join(sc, customers);
                tryAgain = false;
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
            } catch (NoUserFoundException e){
                tryAgain = true;
            }
        } while (tryAgain);

        return 0;
        /* making a booking object */
    }
    static void handleDriver(Scanner sc, ArrayList<Driver> drivers, ArrayList<Booking> bookings){
        /* handle the driver and the cab */
        Driver driv;
        Booking bk = null;
        boolean tryAgain = false;

        do {
            try {
                driv = Driver.join(sc, drivers);
                tryAgain = false;
                System.out.println("Your booking:");
                for(Booking book:Booking.bookings){
                    if(book.cabId == driv.id) {
                        System.out.println(book);
                        bk = book;
                        break;
                    }
                }
                if (bk == null) throw new NoUserFoundException();

                System.out.println("");
                System.out.print("Confirm mission? [y/n]");
                if(sc.next().trim().equals("n")) return;

                driv.drive(bk.pickup);
                driv.drive(bk.destination);

                System.out.println("Destination reached");
                System.out.println("Press 2 to logout, or q to quit the program");

                if(sc.next().trim().equals("q")) System.exit(0);
            } catch (NoUserFoundException e){
                tryAgain = true;
            }
        } while (tryAgain);

    }
    public static void main(String[] args){
    	Scanner sc = new Scanner(System.in);
    	int option = 0;
    	ArrayList<Customer> customers = new ArrayList<Customer>();
        ArrayList<Driver> drivers = new ArrayList<Driver>();
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

            boolean isCustomer = intro(sc);
            if(isCustomer) handleCustomer(sc, customers, bookings);
            else handleDriver(sc, drivers, bookings);

        } while (true);
    }
}

