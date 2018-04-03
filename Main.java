//package DeltaNetwork;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Main {

    private String path;

    private ArrayList<Float> param = new ArrayList<>();
    private ArrayList<String> input = new ArrayList<>();
    private ArrayList<String> teaching = new ArrayList<>();

    public static void main(String[] args) {
        // Gets local directory of .jar file.
        try {
            String pre_path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String path = pre_path.substring(0, pre_path.lastIndexOf("/") + 1);
            Main instance = new Main(path);
            Network network = new Network(path, instance.param, instance.input, instance.teaching);
        }catch(URISyntaxException e) {
            e.printStackTrace();
        }
    }

    Main(String path) {
        this.path = path;
        String line;

        // reading in input files.
        try {
            FileReader reading = new FileReader(path + "param.txt");
            BufferedReader reader = new BufferedReader(reading);
            while ((line = reader.readLine()) != null) {
                param.add(Float.parseFloat(line));
            }

            reading = new FileReader(path + "in.txt");
            reader = new BufferedReader(reading);
            while ((line = reader.readLine()) != null) {
                input.add(line);
            }

            reading = new FileReader(path + "teach.txt");
            reader = new BufferedReader(reading);
            while ((line = reader.readLine()) != null) {
                teaching.add(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}