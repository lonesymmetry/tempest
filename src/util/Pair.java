package util;

/**
 * Contains a pair of a type
 */
public class Pair<T>{
	private T first, second;

	public T getFirst(){
		return first;
	}

	public T getSecond(){
		return second;
	}

	public void setFirst(T first){
		this.first = first;
	}

	public void setSecond(T second){
		this.second = second;
	}

	public Pair(T first, T second){
		this.first = first;
		this.second = second;
	}
}
