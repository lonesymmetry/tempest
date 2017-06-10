package main.java.control;

import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class to read and write the database.
 *
 * @author Adrian Hardt
 */

public class Database {
    private static final String FILE_NAME = "./src/main/data/database.txt";//path from root (.)
    private ArrayList<Item> items;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("E MM dd y hh:mm:ss a");

    public static class PositionedItem{
		private Item item;
    	private int index;

        /**
         * Gets the item from the positioneditem
         * @return item
         */
    	public Item getItem(){
    		return this.item;
		}

        /**
         * Gets index of the positioneditem
         * @return index
         */
		public int getIndex(){
    		return this.index;
		}

        /**
         * Constructs Positioned item based on a database and an index
         * @param database Database where the item is
         * @param index index of the item in the database
         */
    	public PositionedItem(Database database,int index){
    		this.item = database.getItem(index);
    		this.index = index;
		}

        /**
         * Constructs a positioned item based on an item and an index
         * @param item item to add index to
         * @param index index of the item
         */
        public PositionedItem(Item item,int index){
            this.item = item;
            this.index = index;
        }
	}

    /**
     * Converts arraylist of items to an arraylist of PositionedItems
     * @param in input ArrayList<Item>
     * @return ArrayList<PositionedItem>
     */
	public static ArrayList<PositionedItem> toPositionedArray(ArrayList<Item> in){
        ArrayList<PositionedItem> out = new ArrayList<>();
        for(int i = 0; i < in.size(); i++){
            out.add(i,new PositionedItem(in.get(i),i));
        }
        return out;
    }

    /**
     * Converts an arraylist of positioneditems to an arraylist of items
     * @param in input ArrayList<PositionedItem>
     * @return output ArrayList<Item>
     */
    public static ArrayList<Item> fromPositionedArray(ArrayList<PositionedItem> in){
        ArrayList<Item> out = new ArrayList<>();
        for(PositionedItem a: in){
            out.add(a.getIndex(),a.getItem());
        }
        return out;
    }

	/**
	 * Getter for Items list
	 * @return ArrayList<Item> List of items
	 */
	public ArrayList<Item> getItems(){
        return items;
    }

    /**
     * Getter for ArrayList<PositionedItem>
     * @return output ArrayList
     */
    public ArrayList<PositionedItem> getPositionedItems(){
        return toPositionedArray(items);
    }

    /**
     * Getter of an item based on its index
     * @param i Index of Item
     * @return Item at index
     */
	public Item getItem(int i){
    	return this.items.get(i);
    }

    /**
     * Gets positioned item based on an index
     * @param i index of the positioneditem in the arraylist
     * @return the positioneditem at the index
     */
	public PositionedItem getPositionedItem(int i){
		return new PositionedItem(this,i);
	}

    /**
     * Sets items as an input arraylist
     * @param items arraylist to set
     */
    public void setItems(ArrayList<Item> items){
        this.items=items;
    }

    /**
     * Writes the local ArrayList<Item> to the database
     */
    public void writeList(){
        for(Item a: items){
            writeItem(a);
        }
    }

    /**
     * Converts local Items into a string
     * @return String of Items
     */
    @Override
	public String toString(){
    	return "Database(" + this.items.toString() + ")";
	}

    /**
     * Clears all data from the database
     */
    public static void clearDoc(){
        try{
            PrintWriter writer = new PrintWriter(FILE_NAME);
            writer.print("");
            writer.close();
        }
        catch (IOException x) {
            x.printStackTrace(new PrintStream(System.out));
        }
    }

    /**
     * Adds an item to the database
     * @param toWrite Item to write
     */
    public void writeItem(Item toWrite){
        String name = toWrite.getDisplayName();
        String desc = toWrite.getDescription();
        Item.Status status = toWrite.getStatus();
        Item.Priority priority = toWrite.getPriority();
        Date date = toWrite.getDate();


        String dateString = formatter.format(date);

        String write = name+"|"+status+"|"+priority+"|"+dateString+"|"+desc+"\n";

        byte data[] = write.getBytes(); //String -> Bytes
        Path p = Paths.get(FILE_NAME);

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        }
        catch (IOException x) {
            x.printStackTrace(new PrintStream(System.out));
        }
    }

    /**
     * Parses a date from a string
     * @param in String to parse
     * @return Date parsed
     * @throws ParseException Failure
     */
    public Date parseDate(String in) throws ParseException{
        return formatter.parse(in);
    }

    /**
     * Parses a whole line from the database
     * @param input String of the whole line
     * @return An item parsed from the line
     * @throws ParseException Failure
     */
    public Item parseLine(String input) throws ParseException{
        int length=input.length();

        String name="";
        int bar1=0;
        for(int i=0;i<length;i++){
            if(input.substring(i,i+1).equals("|")){
                bar1=i;
                break;
            }
        }
        name=input.substring(0,bar1);

        int bar2=0;
        for(int i=bar1+1;i<length;i++) {
            if (input.substring(i, i + 1).equals("|")) {//A
                bar2 = i;
                break;
            }
        }
        Item.Status status= Item.Status.UNFINISHED;
        {
            String temp = input.substring(bar1+1, bar2);
            if (Item.Status.parseable(temp)) {//B
                status = Item.Status.parse(temp);
            }
        }

        int bar3=0;
        for(int i=bar2+1;i<length;i++) {
            if (input.substring(i, i + 1).equals("|")) {//A
                bar3 = i;
                break;
            }
        }
        Item.Priority priority= Item.Priority.LOW;
        {
            String temp = input.substring(bar2+1, bar3);
            if (Item.Priority.parseable(temp)) {//B
                priority = Item.Priority.parse(temp);
            }
        }

        int bar4=0;
        for(int i=bar3+1;i<length;i++) {
            if (input.substring(i, i + 1).equals("|")) {//A
                bar4 = i;
                break;
            }
        }
        String temp = input.substring(bar3+1, bar4);
        Date date = parseDate(temp);

        String desc = input.substring(bar4+1,length);
        return new Item(name,desc,status,priority,date);
    }

    /**
     * Fills Items with data from the database
     */
    public void fillList(){
        items.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line = reader.readLine();

            while (line != null){
                try {
                    items.add(parseLine(line));
                }
                catch (ParseException a){
                    a.printStackTrace(new PrintStream(System.out));
                }
                finally {
                    line = reader.readLine();
                }
            }
            reader.close();
        }
        catch (IOException x){
            x.printStackTrace(new PrintStream(System.out));
        }
    }

    /**
     * Replaces the database with test items
     */
    public static void convertDataToTest(){
    	clearDoc();
    	final int LENGTH = 30;
		Database a = new Database();
    	for(int i = 0; i < LENGTH; i++){
			Item z = new Item("TestItem#" + (i + 1), "This is a test #" + (i + 1));
			a.writeItem(z);
		}
	}

    /**
     * Edits items based on their index and the item to replace it with
     * @param index index of the item
     * @param in edited item
     */
    public void editItem(int index, Item in){
        clearDoc();
        items.remove(index);
        items.add(index,in);
        writeList();
    }

    /**
     * Edits an item in the database
     * @param item Replacement PositionedItem
     */
    public void editItem(PositionedItem item){
		editItem(item.getIndex(),item.getItem());
    }

    /**
     * Deletes the item specified at the index
     * @param index index of the item to delete
     */
    public void deleteItem(int index){
        clearDoc();
        items.remove(index);
        writeList();
    }

    /**
     * Constructs new Database
     */
    public Database(){
        this(new ArrayList<>());
    }

    /**
     * Constructs new database based on an ArrayList<Item>
     * @param items ArrayList of items for the new Database
     */
    public Database(ArrayList<Item> items){
        this.items = items;
    }

    public static void main(String[] args) {
        Item z= new Item("TestItem","This is a test");
        Database a= new Database();
        a.writeItem(z);
        a.fillList();
        Analytics test=new Analytics();
        test.numFinished(a);
        test.numUnfinished(a);

        a.editItem(3,new Item("Replaced","An edited item"));

        /*
        for(int i=0;i<test.filterHIGH(a).size();i++){
            System.out.println(test.filterHIGH(a).get(i));
            System.out.println(i);
        }
        */

    }
}
