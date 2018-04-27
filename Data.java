import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

// this class manages the input and teaching data.
public class Data {

    //global variables
    private List<String> in;
    private List<String> teach;
    private List<Integer> shuffler;

    /**
     * constructor for a Data instance.
     * Assigns variables to global variables and creates the array to be shuffled
     * @param in is the input data
     * @param teach is the teaching data
     */
    public Data(ArrayList<String> in, ArrayList<String> teach){
        this.in = in;
        this.teach = teach;
        this.shuffler = new ArrayList<Integer>();
        for (int i = 0; i < in.size(); i++){
            shuffler.add(i);
        }
    }

    // shuffles the index array
    public void shuffle(){
        Collections.shuffle(shuffler);
    }

    /**
     * Uses the shuffled array of indexes to populate the input data.
     * @return
     */
    public List<String> get_in(){
        List<String> output = new ArrayList<>();
        for (Integer item : shuffler){
            output.add(in.get(item));
        }

        return output;
    }

    /**
     * Uses the shuffled array of indexes to populate the teaching data.
     * @return
     */
    public List<String> get_teach(){
        List<String> output = new ArrayList<>();
        for (Integer item : shuffler){
            output.add(teach.get(item));
        }

        return output;
    }

}