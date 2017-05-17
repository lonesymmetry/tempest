/**
 *
 */
public class Item {
	public enum Status{UNFINISHED,FINISHED}
	private Status status;
	private String displayName;
	private String description;

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public void setDescription(String description){
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
