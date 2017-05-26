package control;

import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *Class which deals with writing and reading from the database.
 *
 * @author Adrian Hardt
 */

//TODO create a date aspect of the database as well, look into using Java 8 Date()
//TODO add a way to wipe the database file and delete specific Items
//TODO add a way to edit Item's and re-write them to the file
public class Database {
    private static final String FILE_NAME = "database.txt";
    private ArrayList<Item> items;
    private SimpleDateFormat formatter = new SimpleDateFormat("E MM dd y hh:mm:ss a");

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
        Date date = toWrite.getDate();


        String dateString = formatter.format(date);

        String write = name+"|"+status+"|"+priority+"|"+dateString+"|"+desc+"\n";

        byte data[] = write.getBytes(); //String -> Bytes
        Path p = Paths.get("./"+FILE_NAME);

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        }
        catch (IOException x) {
            System.err.println(x);
        }
    }

    public Date parseDate(String in) throws ParseException{
        //System.out.println("A");
        return formatter.parse(in);
    }

    public Item parseLine(String input) throws ParseException{
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

    public ArrayList<Item> fillList(){
    	items.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line = reader.readLine();

            while (line != null){
                try {
                    items.add(parseLine(line));
                }
                catch (ParseException a){
                    System.err.println(a);
                }
                finally {
                    line = reader.readLine();
                }
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
			Item z = new Item("TestItem#" + i, "This is a test #" + i);
			a.writeItem(z);
		}
	}

    public Database(){
        this.items = new ArrayList<>();
    }

    public static void main(String[] args) {
        Item z= new Item("TestItem","This is a test");
        Database a= new Database();
        a.writeItem(z);
        a.fillList();
        Analytics test=new Analytics();
        test.numFinished(a);
        test.numUnfinished(a);

        System.out.println(a);
    }
}
