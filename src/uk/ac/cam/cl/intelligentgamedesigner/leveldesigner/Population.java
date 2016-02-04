package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

public class Population<T> implements Iterable {

    private int size;
    private ArrayList<T> members;
    private Semaphore[] semaphores;

    public Population (int size) {
        this.size = size;
        this.members = new ArrayList<>(size);
        this.semaphores = new Semaphore[size];
    }

    public int getSize () {
        return size;
    }

    private boolean isValidIndex (int index) {
        return (index >= 0 && index < size);
    }

    public T get (int index) {
        if (isValidIndex(index)) {
            T result;
            try {
                semaphores[index].acquire();
                result = members.get(index);
                return result;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } finally {
                semaphores[index].release();
            }
        } else {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    public synchronized void set (int index, T value) {
        if (isValidIndex(index)) {
            try {
                semaphores[index].acquire();
                members.set(index, value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphores[index].release();
            }
        } else {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    @Override
    public Iterator iterator() {
        // TODO: Make population iterable
        return null;
    }
}
