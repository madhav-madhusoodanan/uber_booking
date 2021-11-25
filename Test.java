import java.util.*;
class Y{}

class Test {
	public static void main(String[] args){
		X x = new X();
		Class class = x.getClass();
		class y = new class();
	}
}
