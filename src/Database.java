import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 */
public class Database {
    ArrayList<Item> items= new ArrayList<Item>(); //TODO create method to fill the arraylist

    public void writeItem(Item toWrite){
        String name = toWrite.getDisplayName();
        String desc = toWrite.getDescription();
        Item.Status status = toWrite.getStatus();

        String write = name+"|"+status+"|"+desc+"\n";

        byte data[] = write.getBytes(); //String -> Bytes
        Path p = Paths.get("./database.txt");

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        }
        catch (IOException x) {
            System.err.println(x);
        }
    }

    public int itemCount(){
        int itemCount=0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("database.txt"));
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

    public void test(){
        String s = "Hello World! ";
        byte data[] = s.getBytes(); //String -> Bytes
        Path p = Paths.get("./database.txt");

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
    }
}
