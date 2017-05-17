import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;

/**
 *
 */
public class Database {
    public void writeItem(Item toWrite){
        String name = toWrite.getDisplayName();
        String desc = toWrite.getDescription();

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
        Database a= new Database();
        a.test();
    }
}
