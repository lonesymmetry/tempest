import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 */
public class Database {
    final String fileName = "database.txt";
    private ArrayList<Item> items= new ArrayList<Item>(); //TODO create method to fill the arraylist

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

        String write = name+"|"+status+"|"+desc+"\n";

        byte data[] = write.getBytes(); //String -> Bytes
        Path p = Paths.get("./"+fileName);

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

        String desc = input.substring(bar2+1,length);
        return new Item(name,desc,status);
    }

    public int itemCount(){
        int itemCount=0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = reader.readLine();
                itemCount++;
            }
            String everything = sb.toString();
            reader.close();
        }
        catch (IOException x){
            System.err.println(x);
        }
        return itemCount;
    }

    public ArrayList<Item> fillList(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
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
        Path p = Paths.get("./"+fileName);

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        }
        catch (IOException x) {
            System.err.println(x);
        }
    }

    public static void main(String[] args) {
        Item z= new Item("TestItem","This is a test");
        Database a= new Database();
        a.writeItem(z);
        System.out.println(a.itemCount());
        a.fillList();
        System.out.println(a.getItems());
    }
}
