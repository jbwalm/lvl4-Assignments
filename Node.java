//package DeltaNetwork;

import java.util.Random;

public class Node {

    public float sum;
    public float[] weights;
    private Random random = new Random();

    Node(int weights){
        int i;
        this.sum = 0.0f;
        this.weights = new float[weights];
        for (i = 0; i < this.weights.length; i++){
            this.weights[i] = random.nextFloat();
        }
    }

    public int active(){
        if (this.sum > 0.5f){
            return 1;
        }

        return 0;
    }



}
