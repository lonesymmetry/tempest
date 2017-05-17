package Util;

import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;
import java.util.Vector;

/**
 * A series of useful methods I've defined to use in other projects
 */
public class Util{
	/**
	 * Takes an array and returns a string which represents it
	 * @param array the array to print
	 * @param <T> the type of the array elements
	 * @return the array in string form
	 */
	public static <T extends Object> String arrayToString(T[] array){
		String s = "[";
		for(int i = 0; i < array.length; i++){
			s += array[i].toString();
			if(i < array.length - 1) s += ", ";
		}
		s += "]";
		return s;
	}

	public static <T extends Object> T getRandomTreeSetElement(TreeSet<T> a){
		Iterator<T> aIterator = a.iterator();
		Random rand = new Random();
		int r = rand.nextInt(a.size()), i = 0;
		for(T obj: a){
			if(i == r) return obj;
			i++;
		}
		Util.nyi(Util.getFileName(), Util.getLineNumber());
		return aIterator.next();//should never reach this line
	}

	public static String stringToLowerCase(final String ORIGINAL){
		String lowerCase = "";
		for(char a: ORIGINAL.toCharArray()){
			lowerCase += Character.toLowerCase(a);
		}
		return lowerCase;
	}

	public static String removeSpaces(final String ORIGINAL){
		String s = "";
		for(char c: ORIGINAL.toCharArray()){
			if(c != ' ') s+=c;
		}
		return s;
	}

	public static String stringToUpperCase(final String ORIGINAL){
		String upperCase = "";
		for(char a: ORIGINAL.toCharArray()){
			upperCase += Character.toUpperCase(a);
		}
		return upperCase ;
	}

	public static boolean parseBoolean(String s){
		final String true1 = "true", true2 = "1", false1 = "false", false2 = "0";
		if(!(s.equals(true1) || s.equals(true2) || s.equals(false1) || s.equals(false2))){
			Util.error("Not a valid option: received \"" + s + "\"", Util.getFileName(), Util.getLineNumber());
		}
		return s.equals(true1) || s.equals(true2);
	}

	public static <T extends Object> boolean treeSetEquals(TreeSet<T> a, TreeSet<T> b){
		if(a.size() != b.size()) return false;
		Iterator<T> aIterator = a.iterator();
		Iterator<T> bIterator = b.iterator();
		while(aIterator.hasNext() && bIterator.hasNext()){
			if(!aIterator.next().equals(bIterator.next())) return false;
		}
		return true;
	}

	public static <T extends Object> boolean contains(TreeSet<T> all, T a){//TODO: remove?
		for(T b: all){
			if(a.equals(b)) return true;
		}
		return false;
	}

	public static <T extends Object> boolean contains(Vector<T> all, T a){//TODO: remove?
		for(T b: all){
			if(a.equals(b)) return true;
		}
		return false;
	}

	/**
	 * Get the line number of the line where this method is used
	 * @return the line number of where the method is called
	 */
	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

	/**
	 * Get the name of the file this method is used in
	 * @return the name of the file where this method is called from
	 */
	public static String getFileName(){
		return Thread.currentThread().getStackTrace()[2].getFileName();
	}

	/**
	 * Exit the process if the a argument evaluates to false
	 * @param a the argument to check
	 * @param fileName the name of the file calling the assert function
	 * @param lineNumber the line number this function is called on
	 */
	public static void myAssert(boolean a,String fileName,int lineNumber){
		if(a) return;
		System.err.println(fileName + ":" + lineNumber + ": Asserting via MyAssert");
		System.exit(1);
	}
	/**
	 * Exits the process and prints a given error message
	 * @param message the message to print to the screen
	 * @param fileName the name of the file calling the assert function
	 * @param lineNumber the line number this function is called on
	 */
	public static void error(String message, String fileName,int lineNumber){
		System.err.println(fileName + ":" + lineNumber + ": " + message);
		System.exit(1);
	}
	/**
	 * Prints a given string like System.out.println along with the file name and line number it is called from
	 * @param STRING the string to print
	 * @param fileName the file it's called from
	 * @param lineNumber the line number it's called from
	 */
	public static void println(final String STRING, String fileName,int lineNumber){
		String front = "";
		int startAt = 0;
		for(int i = 0; i < STRING.length(); i++){
			if(STRING.charAt(i) == '\t') front += '\t';
			else if(STRING.charAt(i) == '\n') front += '\n';
			else{
				startAt = i;
				break;
			}
		}
		String back = STRING.substring(startAt);
		System.out.println(front + fileName + ":" + lineNumber + ":" + back);
	}
	/**
	 * Prints a given string like System.out.print along with the file name and line number it is called from
	 * @param STRING the string to print
	 * @param fileName the file it's called from
	 * @param lineNumber the line number it's called from
	 */
	public static void print(String STRING, String fileName,int lineNumber){
		String front = "";
		int startAt = 0;
		for(int i = 0; i < STRING.length(); i++){
			if(STRING.charAt(i) == '\t') front += '\t';
			else if(STRING.charAt(i) == '\n') front += '\n';
			else{
				startAt = i;
				break;
			}
		}
		String back = STRING.substring(startAt);
		System.out.print(front + fileName + ":" + lineNumber + ":" + back);
	}
	/**
	 * Exits the process
	 * @param fileName the name of the file calling the assert function
	 * @param lineNumber the line number this function is called on
	 */
	public static void nyi(String fileName,int lineNumber){
		System.err.println("NYI " + fileName + ":" + lineNumber);
		System.exit(1);
	}
}
