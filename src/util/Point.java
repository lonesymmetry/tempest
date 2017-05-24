package util;

/**
 * Represents a single point
 */
public class Point<T>{
	T x, y;

	public T getX(){
		return x;
	}

	public T getY(){
		return y;
	}

	public void setX(T x){
		this.x = x;
	}

	public void setY(T y){
		this.y = y;
	}

	public Point(){
	}

	public Point(T x, T y){
		this.x = x;
		this.y = y;
	}
}
