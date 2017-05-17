/**
 *
 */
public class Item {
	public enum Status{UNFINISHED,FINISHED}
	private Status status;
	private String displayName;
	private String description;

	public Item(){
		this("","",Status.UNFINISHED);
	}

	public Item(String displayName, String description, Status status){
		this.displayName = displayName;
		this.description = description;
		this.status = status;
	}
}
