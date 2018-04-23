//package DeltaNetwork;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.Math;

public class Network {

    private Data data;

    private List<Float> param;
    private List<String> in;
    private List<String> teach;

    private Node[] layer_one;
    private Node[] layer_two;
    private Node[] layer_three;

    private ArrayList<String> correct_check = new ArrayList<>();

    private float required_error = 0.0f;
    private float curr_error = 0.0f;

    private int finished = 0;

    public boolean blank = false;

    // Initializes a network instance, creating node layers with n nodes.
    Network(){
        this.blank = true;
    }

    Network(String path, ArrayList<Float> param, Data data){
        this.data = data;
        this.param = param;

        layer_one = new Node[(int)(float) param.get(0)];
        layer_two = new Node[(int)(float) param.get(1)];
        layer_three = new Node[(int)(float) param.get(2)];

    }

    // Builds Each layer with requested number of nodes.
    public void build_network(){
        int i;
        for (i = 0; i < layer_one.length; i++){
            layer_one[i] = new Node(layer_two.length, "input");
        }
        for (i = 0; i < layer_two.length; i++){
            layer_two[i] = new Node(layer_three.length, "hidden");
        }
        for (i = 0; i < layer_three.length; i++){
            layer_three[i] = new Node(0, "output");
        }
    }

    // Begins of the process of a feed forward network.
    public float run_network(String type, boolean full, boolean criterion){
        boolean found = false;
        int epoch_count = this.finished;
        int epoch_wanted = epoch_count + 1;
        if (!full){
            epoch_wanted = epoch_count + 100;
        }
        float error;
        if (this.required_error == 0){
            error = 100.0f;
        }else{
            error = this.curr_error;
            found = true;
        }
        if (criterion){
            this.param.set(5, 0.0f);
        }

        Node[][] layers = {layer_one, layer_two, layer_three};

        int check_count = 0;
        while (error > this.param.get(5) & epoch_wanted > epoch_count){
            this.correct_check.clear();
            error = run_epoch(layers, false) / (layers[2].length * in.size());

            // gets the required error criterion when all is correct
            if (!found){
                check_count = 0;
                for (String item : this.correct_check){
                    if (item.equals("correct")){
                        check_count += 1;
                    }
                }
                if (check_count == this.correct_check.size()){
                    this.required_error = error;
                    found = true;
                }
            }
            if (criterion && this.required_error > 0){
                break;
            }
            /**
             * put back prop here to batch learn.
             * if (type.equals("Batch")){
             * back_prop(???)
             * }
             */
            if (full){
                epoch_wanted += 1;
            }
            if (epoch_count == 50000 && criterion){
                return 10.0f;
            }
            epoch_count++;
            this.finished = epoch_count;
            if ((full && epoch_count % 100 == 0 || (!full && (epoch_count % 10 == 0))) && !criterion){
                System.out.println("Epochs completed: " + epoch_count + ", error: " + error);
            }
        }
        if (error < this.param.get(5)) {
            int correct = 0;
            for (String item : this.correct_check) {
                if (item.equals("correct")){
                    correct += 1;
                }
            }
            if (!criterion) {
                System.out.println("\n" + correct + " correct out of " + this.correct_check.size() + " patterns.");
                System.out.println("Required error criterion: " + this.required_error);
            }
        }
        this.curr_error = error;
        return this.required_error;
    }
    // runs an epoch.
    public float run_epoch(Node[][] layers_in, boolean test) {
        String[] split;
        int i;
        int j;
        float pattern_errors = 0.0f;

        if(test){
            this.correct_check.clear();
        }

        // assigns each value in the current pattern to an input node.
        this.in = this.data.get_in();
        this.teach = this.data.get_teach();
        for (i = 0; i < in.size(); i++) {
            split = in.get(i).split("\\s+");
            for (j = 0; j < split.length; j++) {
                layer_one[j].sum = Float.parseFloat(split[j]);
            }
            // runs a feed forward for this pattern to calculate output.

            pattern_errors += feed_forward(layers_in, teach.get(i));

            // online learning, not batch.
            if (!test) {
                back_prop(layers_in, teach.get(i));
            }
        }

        if(test){
            int correct = 0;
            for (String item : this.correct_check) {
                if (item.equals("correct")){
                    correct += 1;
                }
            }
            System.out.println("\n" + correct + " correct out of " + this.correct_check.size() + " patterns.");
            System.out.println("Test error: " + (pattern_errors / (layers_in[2].length * in.size())));
        }

        return pattern_errors;
    }

    private float feed_forward(Node[][] layers_in, String desired_output){
        Node[][] layers = layers_in;
        float change;
        int i;
        int j;
        int k;

        //each layer.
        for (i = 0; i < layers.length - 1; i++){
            //each node in layer.
            for (j = 0; j < layers[i].length; j++){
                layers[i][j].function();
                layers[i][j].sum = 0;

                //each forward-weight in node.
                for (k = 0; k < layers[i][j].weights.length; k++){
                    change = (layers[i][j].get_active() * layers[i][j].weights[k]);
                    layers[i+1][k].sum += change;
                }
            }

            // bias node for loop
            for (j = 0; j < layers[i+1].length; j++){
                change = (1 * layers[i+1][j].bias_weight);
                layers[i+1][j].sum += change;
            }

        }

        String oput = "";
        String[] desired = desired_output.split("\\s+");
        float output = 0.0f;
        for (i = 0; i < desired.length; i++){
            layers[2][i].function();
            layers[2][i].sum = 0.0f;
            output += (float) Math.pow(Double.parseDouble(desired[i]) - (double) layers[2][i].get_active(), 2);

            // checking output against what should of got:
            float o = 0.0f;
            if (layers[2][i].get_active() >= 0.5){
                o = 1.0f;
            }else{
                o = 0.0f;
            }
            oput += o + " ";
        }

        // correct_check preparing
        oput = oput.substring(0, oput.length()-1);
        String[] oput2;
        if (desired_output.length() != oput.length()){
            oput2 = oput.split("\\s+");
            oput = "";
            for (String item : oput2){
                oput += ((int) Float.parseFloat(item)) + " ";
            }
            oput = oput.substring(0, oput.length()-1);
        }
        if (oput.equals(desired_output)){
            this.correct_check.add("correct");
        }else{
            this.correct_check.add(oput +" , " + desired_output);
            //this.correct_check.add("wrong");
        }

        return output;
    }

    private void back_prop(Node[][] layers_in, String desired_output){
        float learning = param.get(3);
        Node[][] layers = layers_in;
        String[] desired = desired_output.split("\\s+");
        int i;
        int j;
        int k;
        int l;
        float error = 0.0f;
        float weight_change;

        //each layer.
        for (i = layers.length - 1; i > 0; i--){
            //each node in layer.
            for (j = 0; j < layers[i].length; j++){
                error = layers[i][j].error_term(desired, j, layers);
                //each node in previous layer.
                for (k = 0; k < layers[i-1].length; k++){

                    weight_change = (learning * error * layers[i-1][k].get_active()) +
                            (this.param.get(4) * layers[i-1][k].previous_changes[j]);

                    layers[i-1][k].weights[j] += weight_change;
                    layers[i-1][k].previous_changes[j] = weight_change;
                }
                // change bias node --> node weight:
                weight_change = (learning * error * 1) +
                        (layers[i][j].bias_weight_change * this.param.get(4));
                layers[i][j].bias_weight += weight_change;
                layers[i][j].bias_weight_change = weight_change;
            }
        }
        return;
    }

    public void weights(){
        Node[][] layers = {layer_one, layer_two, layer_three};
        // layers
        for (int i = 0; i < layers.length-1; i++){
            System.out.println("Weight layer: " + (i+1));
            // nodes
            for (int j = 0; j < layers[i].length; j++){
                System.out.println("Weights from node: " + (j+1));
                // all weights for node
                System.out.println(Arrays.toString(layers[i][j].weights) + "\n");
            }
            System.out.println("-----------------");
        }
    }

    public Node[][] get_layers(){
        Node[][] layers ={layer_one, layer_two, layer_three};
        return layers;
    }

}