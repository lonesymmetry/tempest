package util;

/**
 * Represents a single point
 *
 * @author Logan Traffas
 */
public class Point<T>{
	private T x, y;

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

	public Point(T x, T y){
		this.x = x;
		this.y = y;
	}
}
