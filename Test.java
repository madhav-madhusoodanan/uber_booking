import java.util.*;
class Y{}
interface Z{}

class X extends Y implements Z{}

class Test {
	public static void main(String[] args){
		X x = new X();
	}
}
