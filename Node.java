//package DeltaNetwork;

import java.util.Random;
import java.lang.Math;

public class Node {

    public float sum;
    private float active;
    public float[] weights;
    public float bias_weight;
    private String type;
    private Random random = new Random();

    Node(int weights, String type){
        int i;
        this.sum = 0.0f;
        this.active = 0.0f;
        this.weights = new float[weights];
        this.type = type;

        float high = 0.3f;
        float low = -0.3f;
        for (i = 0; i < this.weights.length; i++){
            this.weights[i] = random.nextFloat() * (high - low) + low;
        }
        this.bias_weight = random.nextFloat() * (high - low) + low;
    }

    private int active(){
        if (this.sum > 0.0f){
            return 1;
        }
        return 0;
    }

    public float get_active(){
        return this.active;
    }

    public float function(){
        // apply function here
        if (type.equals("input")){
            this.active = this.sum;
            return this.active;
        }else if (type.equals("output")){
            //System.out.println(this.sum);
            this.active = 1/(1+((float) Math.exp(-1 * this.sum)));
            return this.active;
        }
        // replace this with sigmoid function for hidden layer nodes.
        this.active = 1/(1+((float) Math.exp(-1 * this.sum)));

        return this.active;

    }

    public float error_term(float desired){
        float error = 0.0f;

        if (this.type.equals("output")){
            error = (desired - this.active) * this.active * (1 - this.active);
            return error;
        }

        //error = this.active * (1 - this.active) * (())
        return error;
    }



}