package main.java.control;

import main.java.util.Util;

import java.util.Date;

/**
 * Stores Item class
 *
 * @author Logan Traffas, Adrian Hardt
 */
public class Item {
	/**
	 * Represents the status of an Item (finished or unfinished)
	 */
	public enum Status{//TODO: add toString() ?
		UNFINISHED,FINISHED;

		/**
		 * Returns the opposite status of the one given--used in toggling
		 * @param a the status to return the opposite of
		 * @return the opposite status
		 */
		public static Status not(Status a){
			switch(a){
				case FINISHED:
					return UNFINISHED;
				case UNFINISHED:
					return FINISHED;
				default:
					Util.nyi(Util.getFileName(),Util.getLineNumber());
			}
			return null;//will never reach this line
		}

		/**
		 * Determines if a String can be parsed into a Status
		 * @param s the String to check
		 * @return true if the String can be parsed
		 */
		public static boolean parseable(String s){
			Status a = Status.parse(s);
			return a != null;
		}

		/**
		 * Parses a String and returns a Status
		 * @param s the String to parse
		 * @return the Status if it can be parsed, null otherwise
		 */
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

	/**
	 * Represents the priority of the Item (high, medium, or low priority)
	 */
	public enum Priority{//TODO: add toString() ?
		HIGH,MEDIUM,LOW;

		/**
		 * Determines if a String can be parsed into a Priority
		 * @param s the String to check
		 * @return true if the String can be parsed
		 */
		public static boolean parseable(String s){
			Priority a = Priority.parse(s);
			return a != null;
		}

		/**
		 * Parses a String and returns a Priority
		 * @param s the String to parse
		 * @return the Priority if it can be parsed, null otherwise
		 */
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
	private static final int MAX_DISPLAY_NAME_LENGTH = 45;
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

	/**
	 * Returns a shorter version of the Item name that will fit in the display list of the Desktop Application
	 * @return
	 */
	public String shortenName(){
		if(displayName.length() > MAX_DISPLAY_NAME_LENGTH){
			final String ELIPSIS = "...";
			return displayName.substring(0,Math.max(0,(MAX_DISPLAY_NAME_LENGTH+1) - ELIPSIS.length())) + ELIPSIS;
		}
		return displayName;
	}

	/**
	 * Sets the displayName to a given String
	 * @param displayName the given String
	 */
	public void setDisplayName(String displayName){
		boolean safe = checkIfAllowed(displayName);
		if(!safe){
			Util.error("Attempting to set description with a disallowed character",Util.getFileName(),Util.getLineNumber());
		}
		this.displayName = displayName;
	}

	/**
	 * Sets the description to a given String
	 * @param description the given String
	 */
	public void setDescription(String description){
		boolean safe = checkIfAllowed(displayName);
		if(!safe){
			Util.error("Attempting to set description with a disallowed character",Util.getFileName(),Util.getLineNumber());
		}
		this.description = description;
	}

	/**
	 * Sets the status to a given Status
	 * @param status the given Status
	 */
	public void setStatus(Status status){
		this.status = status;
	}

	/**
	 * Toggles the status of the Item (so unfinished becomes finished, and finished becomes finished)
	 */
	public void toggleStatus(){
		this.status = Status.not(this.status);
	}

	/**
	 * Sets the priority to a given Priority
	 * @param priority the given Priority
	 */
	public void setPriority(Priority priority){
		this.priority=priority;
	}

	/**
	 * Sets the date to a given Date
	 * @param date the given Date
	 */
	public void setDate(Date date){
		this.date = date;
	}

	/**
	 * Returns the displayName
	 * @return the displayName
	 */
	public String getDisplayName(){
		return this.displayName;
	}

	/**
	 * Returns the description
	 * @return the description
	 */
	public String getDescription(){
		return this.description;
	}

	/**
	 * Returns the status
	 * @return the status
	 */
	public Status getStatus(){
		return this.status;
	}

	/**
	 * Returns the priority
	 * @return the priority
	 */
	public Priority getPriority(){
		return this.priority;
	}

	/**
	 * Returns the date
	 * @return the date
	 */
	public Date getDate(){
		return date;
	}

	/**
	 * Compares an Object to this and returns true if they are equal by value
	 * @param o the Object to compare with
	 * @return true if they are equal by value
	 */
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Item)){
			return false;
		}
		Item b = (Item)o;
		if(this.status != b.status){
			return false;
		}
		if(!this.displayName.equals(b.displayName)){
			return false;
		}
		if(!this.description.equals(b.description)){
			return false;
		}
		if(this.priority != b.priority){
			return false;
		}
		if(!this.date.equals(b.date)){
			return false;
		}
		return true;
	}

	/**
	 * Creates a String to display the Item's values
	 * @return a formatted String with this Item's contents
	 */
	@Override
	public String toString(){
		return "main.java.control.Item(displayName:" + this.displayName + " status:" + this.status + " priority:"+this.priority+" date:"+ this.date+ " description:" + this.description + ")";
	}

	/**
	 * Constructs a new Item with the default values
	 */
	public Item(){
		this("","",Status.UNFINISHED,Priority.LOW,new Date());
	}

	/**
	 * Constructs an Item from a name and description
	 * @param displayName the initial name
	 * @param description the initial description
	 */
	public Item(String displayName,String description){
		this(displayName,description,Status.UNFINISHED,Priority.LOW,new Date());
	}

	/**
	 * Constructs an Item from a name and description
	 * @param displayName the initial name
	 * @param description the initial description
	 * @param priority the initial priority
	 */
	public Item(String displayName,String description,Priority priority){
		this(displayName,description,Status.UNFINISHED,priority,new Date());
	}

	/**
	 * Constructs an Item from a name and description
	 * @param displayName the initial name
	 * @param description the initial description
	 * @param status the initial  status
	 * @param priority the initial priority
	 * @param date the initial date
	 */
	public Item(String displayName, String description, Status status, Priority priority, Date date){
		this.displayName = displayName;
		this.description = description;
		this.status = status;
		this.priority = priority;
		this.date = date;
	}
}
