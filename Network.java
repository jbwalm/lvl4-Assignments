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

    private int finished = 0;

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
    public void run_network(String type){
        int epoch_count = 0;
        float error = 100.0f;
        Node[][] layers = {layer_one, layer_two, layer_three};

        // prints out all weights in structure
        for (int i = 0; i < layers.length-1; i++){
            for (int j = 0; j < layers[i].length; j++){
                for (int k = 0; k < layers[i][j].weights.length; k++){
                    System.out.print(layers[i][j].weights[k] + ", ");
                }
                System.out.print("bias: " + layers[i][j].bias_weight);
                System.out.println("");
            }
            System.out.println("-----");
        }

        while (/**error > this.param.get(5)*/epoch_count<50001){
            error = run_epoch(layers) / (layers[2].length * in.size());
            this.data.shuffle();
            /**put back prop here to batch learn.
            if (type.equals("Batch")){
                back_prop(???)
            }*/

            epoch_count++;
            this.finished = epoch_count;
            //System.out.println("--------------------------");
            if (epoch_count % 10 == 0){
                System.out.println("Epochs completed: " + epoch_count + ", error: " + error);
            }
        }

        // prints out all weights, post learning.
        for (int i = 0; i < layers.length-1; i++){
            for (int j = 0; j < layers[i].length; j++){
                for (int k = 0; k < layers[i][j].weights.length; k++){
                    System.out.print(layers[i][j].weights[k] + ", ");
                }
                System.out.print("bias: " + layers[i][j].bias_weight);
                System.out.println("");
            }
            System.out.println("-----");
        }

    }
    // runs an epoch.
    private float run_epoch(Node[][] layers_in) {
        String[] split;
        int i;
        int j;
        float pattern_errors = 0.0f;

        // assigns each value in the current pattern to an input node.
        this.in = this.data.get_in();
        this.teach = this.data.get_teach();
        for (i = 0; i < in.size(); i++) {
            split = in.get(i).split("\\s+");
            for (j = 0; j < split.length; j++) {
                layer_one[j].sum = Float.parseFloat(split[j]);
            }
            // runs a feed forward for this pattern to calculate output.
            //System.out.println("---------pattern forward: " + i);
            pattern_errors += feed_forward(layers_in, teach.get(i));
            //System.out.println("---------feedforward done");
            // online learning, not batch.
            //System.out.println("---------pattern backprop: " + i);
            back_prop(layers_in, teach.get(i));
            //System.out.println("---------backprop done");
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
                //System.out.println("active: " + layers[i][j].get_active() + "  sum: " + layers[i][j].sum);
                layers[i][j].sum = 0; // set sum to 0 since down with this nodes sum.
                //each forward-weight in node.
                //System.out.print("Changes: ");
                for (k = 0; k < layers[i][j].weights.length; k++){
                    change = (layers[i][j].get_active() * layers[i][j].weights[k]);
                    //System.out.print(layers[i][j].weights[k] + "--> " + change + ", ");
                    layers[i+1][k].sum += change;
                }
                //System.out.println("");
            }
            // bias node for loop
            //System.out.print("Bias weights: ");
            for (j = 0; j < layers[i+1].length; j++){
                change = (1 * layers[i+1][j].bias_weight);
                //System.out.print(layers[i+1][j].bias_weight + "--> " + change + ", ");
                layers[i+1][j].sum += change;
            }
            //System.out.println("");
            //System.out.println("_-_");
        }

        //System.out.println("");
        String[] desired = desired_output.split("\\s+");
        float output = 0.0f;
        for (i = 0; i < desired.length; i++){
            layers[2][i].function();
            //System.out.println("output node: ");
            //System.out.println("sum: " + layers[2][i].sum + ", activation:" + layers[2][i].get_active() + ", bias: " + layers[2][i].bias_weight);
            layers[2][i].sum = 0.0f;
            output += (float) Math.pow(Double.parseDouble(desired[i]) - (double) layers[2][i].get_active(), 2);

            if (this.finished == 50000) {
                System.out.print("(" + layers[2][i].get_active() + ", " + desired[i] + "), ");
            }
        }
        if (this.finished == 50000) {
            for (i = 0; i < this.layer_one.length; i ++){
                System.out.print(this.layer_one[i].get_active() + ", ");
            }
            System.out.println("");
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
                    //each weight for current node from previous layer
                    /**
                    for (l = 0; l < layers[i-1][k].weights.length; l++){
                        weight_change = (learning * error * layers[i-1][k].get_active()) +
                                (this.param.get(4) * layers[i-1][k].previous_changes[l]);

                        layers[i-1][k].weights[l] += weight_change;
                        layers[i-1][k].previous_changes[l] = weight_change;
                    }
                     */
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

}