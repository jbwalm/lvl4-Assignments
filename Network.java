//package DeltaNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Network {

    private List<Float> param;
    private List<String> in;
    private List<String> teach;

    private Node[] layer_one;
    private Node[] layer_two;
    private Node[] layer_three;

    private int epoch_count = 100;

    Network(String path, ArrayList<Float> param, ArrayList<String> in, ArrayList<String> teach){
        this.param = param;
        this.in = in;
        this.teach = teach;

        layer_one = new Node[(int)(float) param.get(0)];
        layer_two = new Node[(int)(float) param.get(1)];
        layer_three = new Node[(int)(float) param.get(2)];

        // builds network using given parameters.
        build_network();

        // runs the network.
        float success_rate = run_network();

        System.out.println("epochs completed: " + epoch_count + ", Success rate: " + success_rate*100 + "%");
    }

    // Builds Each layer with requested number of nodes.
    private void build_network(){
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
    private float run_network(){
        int epoch_count = 0;
        int correct = 0;

        // performs so many epochs, 1 epoch is batch learnt.
        Node[][] layers = {layer_one, layer_two, layer_three};
        while (epoch_count < this.epoch_count){
            correct += run_epoch(layers);
            // put back prop here to batch learn.
            epoch_count++;
        }
        System.out.println(correct);
        return (correct/this.epoch_count)/(float) in.size();
    }
    // runs an epoch.
    private int run_epoch(Node[][] layers_in) {
        String[] split;
        int i;
        int j;
        int correct = 0;

        // assigns each value in the current patter to an input node.
        for (i = 0; i < in.size(); i++) {
            split = in.get(i).split("\\s+");
            for (j = 0; j < split.length; j++) {
                layer_one[j].sum = Float.parseFloat(split[j]);
            }
            // runs a feed forward for this pattern to calculate output.
            correct += feed_forward(layers_in, teach.get(i));

            // online learning, not batch.
            back_prop(layers_in, teach.get(i));
        }
        return correct;
    }

    private int feed_forward(Node[][] layers_in, String desired_output){
        Node[][] layers = layers_in;
        int i;
        int j;
        int k;

        //each layer.
        for (i = 0; i < layers.length - 1; i++){
            //each node in layer.
            for (j = 0; j < layers[i].length; j++){
                //each forward-weight in node.
                for (k = 0; k < layers[i][j].weights.length; k++){
                    layers[i+1][k].sum += (layers[i][j].function() * layers[i][j].weights[k]);
                }
                //every weight for this node has been forward fed, set sum to 0 for next epoch.
                layers[i][j].sum = 0;
            }
            // bias node for loop
            for (j = 0; j < layers[i+1].length; j++){
                layers[i+1][j].sum += 1 * layers[i+1][j].bias_weight;
            }
        }

        String output = "";
        String sums = "";
        for (Node node : layer_three){
            if (node.function() > 0.5f) {
                output += "1 ";
            }else{
                output += "0 ";
            }
            System.out.println(output + ", " + node.function());
            node.sum = 0.0f;
        }
        output = output.substring(0, output.length()-1);
        if (output.equals(desired_output)){
            return 1;
        }
        return 0;
    }

    private void back_prop(Node[][] layers_in, String desired_output){
        float learning = param.get(3);
        Node[][] layers = layers_in;
        String[] desired = desired_output.split("//s+");
        int i;
        int j;
        int k;
        int l;

        //each layer.
        for (i = layers.length - 1; i > 0; i--){
            //each node in layer.
            for (j = layers[i].length - 1; j >= 0; j--){
                //each node in previous layer.
                for (k = 0; k < layers[i-1].length; k++){
                    //each weight for current node from previous layer
                    for (l = 0; l < layers[i-1][k].weights.length; l++){
                        layers[i-1][k].weights[l] += (learning * layers[i][j].error_term(Float.parseFloat(desired[0])) * layers[i-1][k].get_active());
                    }
                }
                // change bias node --> node weight:
                layers[i][j].bias_weight += learning * layers[i][j].error_term(Float.parseFloat(desired[0])) * 1;
            }
        }
        return;
    }

}