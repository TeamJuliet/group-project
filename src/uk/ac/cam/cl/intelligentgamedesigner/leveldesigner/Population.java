package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;

public class Population<T> {

    private int size;
    private ArrayList<T> members;

    public Population () {
        this.members = new ArrayList<>();
    }

    public synchronized int size () {
        return size;
    }

    public synchronized T get (int index) {
    	return members.get(index);
    }

    public synchronized void set (int index, T value) {
    	members.set(index, value);
    }
    
    public synchronized void add(T value) {
    	members.add(value);
    }
    
    public synchronized void remove(int index) {
    	members.remove(index);
    }
}
