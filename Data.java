import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Data {

    private List<String> in;
    private List<String> teach;
    private List<Integer> shuffler;

    public Data(ArrayList<String> in, ArrayList<String> teach){
        this.in = in;
        this.teach = teach;
        this.shuffler = new ArrayList<Integer>();
        for (int i = 0; i < in.size(); i++){
            shuffler.add(i);
        }
    }

    public void shuffle(){
        Collections.shuffle(shuffler);
    }

    public List<String> get_in(){
        List<String> output = new ArrayList<>();
        for (Integer item : shuffler){
            output.add(in.get(item));
        }

        return output;
    }

    public List<String> get_teach(){
        List<String> output = new ArrayList<>();
        for (Integer item : shuffler){
            output.add(teach.get(item));
        }

        return output;
    }
}
