package game2d.charmechanics;

import java.util.LinkedList;
import java.util.Queue;

/**Prevents a character from getting stuck.*/
public class PreviousStepNode{
    public Queue<Integer> x;
    public Queue<Integer> y;
    public boolean bad_step;
    public PreviousStepNode(){
        x = new LinkedList<Integer>();
        x.add(0);
        x.add(0);

        y = new LinkedList<Integer>();
        y.add(0);
        y.add(0);

        bad_step = false;
    }
}
