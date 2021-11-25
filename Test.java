import java.util.*;

class Test {
	public static int calc(int number){
		if(number == 1) return 3;
		else{
			int var = ((number/3) * calc(number - 3));
			return var;
		}
	}
	public static void main(String[] args){
		System.out.println(calc(300));
	}
}
