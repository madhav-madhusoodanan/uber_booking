import java.util.*;

class Test {
	static <T> T identity(T obj)  {
		return obj;
		/* check hashmap if that user exists. if it does then login, else signup */
	}

	public static void main(String[] args){
		int integer = identity(300);
		double floating = identity(30.0);

		/* printing */
		System.out.println(integer);
		System.out.println(floating);
	}
}
