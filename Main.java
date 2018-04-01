import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Main {

    private ArrayList<Integer> param = new ArrayList<Integer>();
    private ArrayList<String> input = new ArrayList<String>();
    private ArrayList<String> teaching = new ArrayList<String>();

    public static void main(String[] args) throws IOException, URISyntaxException {
        // Gets local directory of .jar file.
        String pre_path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String path = pre_path.substring(0, pre_path.lastIndexOf("/") + 1);

        // reading in input files.
        FileReader reading = new FileReader(path + "param.txt");
        BufferedReader reader = new BufferedReader(reading);
        String line;
        while ((line = reader.readLine()) != null){
            param.add(Integer.parseInt(line));
        }

        reading = new FileReader(path + "in.txt");
        reading = new FileReader(path + "teach.txt");
    }

    public main(){

    }
}
