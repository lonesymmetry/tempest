package control;

import util.Util;

import java.util.Date;

/**
 * Stores Item class
 *
 * @author Logan Traffas, Adrian Hardt
 */
public class Item {
	public enum Status{
		UNFINISHED,FINISHED;

		public static boolean parseable(String s){
			Status a = Status.parse(s);
			return a != null;
		}

		public static Status parse(String s){
			if(s.equals("UNFINISHED")){
				return UNFINISHED;
			}
			if(s.equals("FINISHED")){
				return FINISHED;
			}
			return null;
		}
	}

	public enum Priority{
		HIGH,MEDIUM,LOW;

		public static boolean parseable(String s){
			Priority a = Priority.parse(s);
			return a != null;
		}

		public static Priority parse(String s){
			if(s.equals("HIGH")){
				return HIGH;
			}
			if(s.equals("MEDIUM")){
				return MEDIUM;
			}
			if(s.equals("LOW")){
				return LOW;
			}
			return null;
		}
	}

	private static final char[] DISALLOWED_CHARACTERS = {'|','\n'};
	private static final int maxLength = 16;
	private Status status;
	private String displayName;
	private String description;
	private Priority priority;
	private Date date;

	/**
	 * Checks a string for disallowed characters
	 * @param TEST the string to test
	 * @return false if the string contains a disallowed character, true otherwise
	 */
	private boolean checkIfAllowed(String TEST){
		for(char disallowed: DISALLOWED_CHARACTERS){
			for(char c: TEST.toCharArray()){
				if(c == disallowed){
					return false;
				}
			}
		}
		return true;
	}

	public String shortenName(){
		if(displayName.length()>maxLength)return displayName.substring(0,maxLength+1);
		return displayName;
	}

	public void setDisplayName(String displayName){
		boolean safe = checkIfAllowed(displayName);
		if(!safe){
			Util.error("Attempting to set description with a disallowed character",Util.getFileName(),Util.getLineNumber());
		}
		this.displayName = displayName;
	}

	public void setDescription(String description){
		boolean safe = checkIfAllowed(displayName);
		if(!safe){
			Util.error("Attempting to set description with a disallowed character",Util.getFileName(),Util.getLineNumber());
		}
		this.description = description;
	}

	public void setStatus(Status status){
		this.status = status;
	}

	public void setPriority(Priority priority){
		this.priority=priority;
	}

	public void setDate(Date date){
		this.date = date;
	}

	public String getDisplayName(){
		return this.displayName;
	}

	public String getDescription(){
		return this.description;
	}

	public Status getStatus(){
		return this.status;
	}

	public Priority getPriority(){
		return this.priority;
	}

	public Date getDate(){
		return date;
	}

	@Override
	public String toString(){
		return "control.Item(displayName:" + this.displayName + " status:" + this.status + " priority:"+this.priority+" date:"+ this.date+ " description:" + this.description + ")";
	}

	public Item(){
		this("","",Status.UNFINISHED,Priority.LOW,new Date());
	}

	public Item(String displayName,String description){
		this(displayName,description,Status.UNFINISHED,Priority.LOW,new Date());
	}

	public Item(String displayName,String description,Priority priority){
		this(displayName,description,Status.UNFINISHED,priority,new Date());
	}

	public Item(String displayName, String description, Status status, Priority priority, Date date){
		this.displayName = displayName;
		this.description = description;
		this.status = status;
		this.priority = priority;
		this.date = date;
	}
}
