//package DeltaNetwork;

import java.util.Random;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;

public class Node {

    public float sum;
    private float active;
    public float[] weights;
    public float[] previous_changes;
    public float bias_weight;
    public float bias_weight_change;
    public float error_term;
    private String type;
    private Random random = new Random();

    Node(int weights, String type){
        int i;
        this.sum = 0.0f;
        this.active = 0.0f;
        this.weights = new float[weights];
        this.previous_changes = new float[weights];
        this.type = type;
        this.error_term = 0.0f;

        float high = 0.3f;
        float low = -0.3f;
        for (i = 0; i < this.weights.length; i++){
            this.weights[i] = random.nextFloat() * (high - low) + low;
            this.previous_changes[i] = 0.0f;
        }
        this.bias_weight = random.nextFloat() * (high - low) + low;
        this.bias_weight_change = 0.0f;
    }


    public float get_active(){ // not needed?
        return this.active;
    }

    public float function(){
        // apply function here
        if (this.type.equals("input")) {
            this.active = this.sum;
            return this.active;
        }
        this.active = 1/(1+((float)Math.exp( -1 * this.sum)));

        return this.active;

    }

    public float error_term(String[] desired, int j, Node[][] layers){
        float error = 0.0f;
        List<Float> semi_errors = new ArrayList<>();
        float semi_error = 0;

        if (this.type.equals("output")){
            float desire = Float.parseFloat(desired[j]);
            error = (desire - this.active) * this.active * (1 - this.active);
            this.error_term = error;
            return error;
        }
        for (int i = 0; i < this.weights.length; i++){
            semi_errors.add(layers[2][i].error_term * this.weights[i]);
        }
        for (float item : semi_errors){
            semi_error += item;
        }
        error = this.active * (1 - this.active) * semi_error;
        this.error_term = error;
        return error;
    }

}