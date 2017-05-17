import Util.Util;

/**
 *
 */
public class Item {
	public enum Status{UNFINISHED,FINISHED}
	private static final char[] DISALLOWED_CHARACTERS = {'|','\n'};
	private Status status;
	private String displayName;
	private String description;

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

	public String getDisplayName(){
		return this.displayName;
	}

	public String getDescription(){
		return this.description;
	}

	public Status getStatus(){
		return this.status;
	}

	@Override
	public String toString(){
		return "Item(displayName:" + this.displayName + " status:" + this.status + " description:" + this.description + ")";
	}

	public Item(){
		this("","",Status.UNFINISHED);
	}

	public Item(String displayName,String description){
		this(displayName,description,Status.UNFINISHED);
	}

	public Item(String displayName, String description, Status status){
		this.displayName = displayName;
		this.description = description;
		this.status = status;
	}
}
