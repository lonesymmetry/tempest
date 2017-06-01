package main.java.util;

/**
 * Stores a value and keeps track of whether it has been set or not
 *
 * @author Logan Traffas
 */
public class Maybe<T>{
	private T a;
	private boolean valid;

	public T get(){
		if(!this.valid){
			Util.error("Value not set", Util.getFileName(), Util.getLineNumber());
		}
		return this.a;
	}

	@Override
	public String toString(){
		String s = "Maybe(";
		if(this.valid) s+= this.get();
		else s+= "NULL";
		s += ")";
		return s;
	}

	@Override
	public boolean equals(Object o){
		if(o == null || o.getClass() != this.getClass()) return false;
		Maybe<T> b = (Maybe<T>)o;
		if(b.isValid() != this.valid) return false;
		if(this.valid){
			return this.get().equals(b.get());
		}
		return true;
	}

	public boolean isValid(){
		return this.valid;
	}

	public Maybe(){
		valid = false;
	}

	public void set(T a){
		this.a = a;
		this.valid = true;
	}

	public Maybe(T a){
		this.a = a;
		valid = true;
	}
}
