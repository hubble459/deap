package nl.hubble.deap;

import java.util.ArrayList;

public class InfiniteArrayList<T> extends ArrayList<T> {
    @Override
    public T set(int index, T element) {
        final int empty = this.size() - index;
        for (int i = 0; i >= empty; i--) {
            this.add(null);
        }
        return super.set(index, element);
    }

    @Override
    public T get(int index) {
        if (index >= this.size()) return null;
        return super.get(index);
    }
}
