package control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @Author Logan Traffas
 * @Date 5/24/2017.
 * @Version 1.0.0
 * @Assignment
 */
public class ItemDescription {
	private StringProperty description;

	public ItemDescription(){
		this(new SimpleStringProperty());
	}

	public String getDescription(){
		return this.description.get();
	}

	public void setDescription(String description){
		this.description.setValue(description);
	}

	public StringProperty descriptionProperty(){
		return description;
	}

	public ItemDescription(String description){
		this.description = new SimpleStringProperty();
		this.description.setValue(description);
	}

	public ItemDescription(StringProperty description){
		this.description = description;
	}
}
