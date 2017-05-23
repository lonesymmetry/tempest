package control;

import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;

/**
 *Class which deals with writing and reading from the database.
 *
 * @author Adrian Hardt
 */
//TODO create a date aspect of the database as well, look into using Java 8 Date()
//TODO add a way to wipe the database file
public class Database {
    private static final String FILE_NAME = "database.txt";
    private ArrayList<Item> items;

    public ArrayList<Item> getItems(){
        return items;
    }

    public void setItems(ArrayList<Item> items){
        this.items=items;
    }

    public void writeItem(Item toWrite){
        String name = toWrite.getDisplayName();
        String desc = toWrite.getDescription();
        Item.Status status = toWrite.getStatus();
        Item.Priority priority = toWrite.getPriority();

        String write = name+"|"+status+"|"+priority+"|"+desc+"\n";

        byte data[] = write.getBytes(); //String -> Bytes
        Path p = Paths.get("./"+FILE_NAME);

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        }
        catch (IOException x) {
            System.err.println(x);
        }
    }

    public Item parseLine(String input){
        int length=input.length();

        String name="INIT";
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
        String desc = input.substring(bar3+1,length);
        return new Item(name,desc,status,priority);
    }

    public ArrayList<Item> fillList(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                items.add(parseLine(line));
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException x){
            System.err.println(x);
        }
        return items;
    }

    public void test(){
        String s = "Hello World! ";
        byte data[] = s.getBytes(); //String -> Bytes
        Path p = Paths.get("./"+FILE_NAME);

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        }
        catch (IOException x) {
            System.err.println(x);
        }
    }

    public static void testWrite(){
    	final int LENGTH = 15;
		Database a = new Database();
    	for(int i = 0; i < LENGTH; i++){
			Item z = new Item("TestItem", "This is a test");
			a.writeItem(z);
		}
		a.fillList();
		System.out.println(a.getItems());
	}

    public Database(){
        this.items = new ArrayList<>();
    }

    public static void main(String[] args) {
        Item z= new Item("TestItem","This is a test");
        Database a= new Database();
        a.writeItem(z);
        a.fillList();
        System.out.println(a.getItems());
        Analytics test=new Analytics();
        test.numFinished(a);
        test.numUnfinished(a);
    }
}
