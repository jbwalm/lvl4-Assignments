//package DeltaNetwork;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private String path;

    private ArrayList<Float> param = new ArrayList<>();
    private ArrayList<String> input = new ArrayList<>();
    private ArrayList<String> teaching = new ArrayList<>();

    public static void main(String[] args) {
        // Gets local directory of .jar file.
        String bad_init = "Cannot run, you have not initialised the network.\n Please initialise the network";
        String menu = "Main menu: \n  1 : Initialise\n  2 : teach(100 epochs)\n  3 : Teach(to criteria)" +
                "\n  4 : Test\n  5 : Show weights\n  6 : Estimate Criterion\n  7 : Show Menu\n  0 : Quit\n";
        try {
            String pre_path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String path = pre_path.substring(0, pre_path.lastIndexOf("/") + 1);
            Main instance;// = new Main(path);
            Data data;// = new Data(instance.input, instance.teaching);
            Network network = new Network();// = new Network(path, instance.param, data);
            Scanner scan = new Scanner(System.in);

            boolean run = true;
            int option;
            System.out.println(menu);
            /**
             * text menu here? can call network methods.
             * eg: build network --> reinitialize
             * --> Network network = new Network(...); --> build --> run
             */
            while (run){
                System.out.print("Option: ");
                option = scan.nextInt();
                switch (option){
                    case 0:
                        return;
                    case 1:
                        instance = new Main(path);
                        data = new Data(instance.input, instance.teaching);
                        network = new Network(path, instance.param, data);
                        network.build_network();
                        break;

                    case 2:
                        // teach for 100 epochs
                        if (network.blank){
                            System.out.println(bad_init);
                        }else{
                            network.run_network("online", false, false);
                            System.out.println("");
                        }
                        break;

                    case 3:
                        // teach to criterion
                        if (network.blank){
                            System.out.println(bad_init);
                        }else{
                            network.run_network("online", true, false);
                            System.out.println("");
                        }
                        break;

                    case 4:
                        // Test network
                        if (network.blank){
                            System.out.println(bad_init);
                        }else{
                            Node[][] layers = network.get_layers();
                            network.run_epoch(layers, true);
                        }
                        System.out.println("");
                        break;

                    case 5:
                        // ouput weights
                        if (network.blank){
                            System.out.println(bad_init);
                        }else{
                            network.weights();
                        }
                        System.out.println("");
                        break;

                    case 6:
                        // Estimate criterion
                        System.out.print("Choosing this option will re-initialise your network, 1 = yes, 0 = no\nContinue? ");
                        int input = scan.nextInt();
                        if(input == 1) {
                            System.out.print("\nHow many error criterions would you like to calculate for an estimate?\n" +
                                    "Lowest error criterion found will be the estimate given.\nHow many? ");
                            int repeats = scan.nextInt();
                            float crit = 10.0f;
                            float crit_temp;
                            for (int i = 0; i < repeats; i++) {
                                instance = new Main(path);
                                data = new Data(instance.input, instance.teaching);
                                network = new Network(path, instance.param, data);
                                network.build_network();
                                crit_temp = network.run_network("online", true, true);
                                if (crit_temp < crit) {
                                    crit = crit_temp;
                                }else if (crit_temp == 10.0f){
                                    i -= 1;
                                }
                            }
                            if (crit != 10.0f) {
                                System.out.println(crit + "\n");
                            }else{
                                System.out.println("");
                            }
                        }
                        break;

                    case 7:
                        // show menu
                        System.out.println(menu);
                        break;
                }
            }


            // builds network using given parameters.
            //network.build_network();
            // runs the network.
            //network.run_network("online");
            //System.out.println("epochs completed: " + epoch_count + ", Error rate: " + error_rate*100 + "%");

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