package nl.hubble.deap;

import java.util.ArrayList;

import nl.hubble.deap.exeptions.ArrayIsFullException;

public class Deap<E extends Comparable<E>> {
    private final InfiniteArrayList<E> deap;
    private int size = 1;

    /**
     * Construct a deap
     */
    public Deap() {
        deap = new InfiniteArrayList<>();
    }

    /**
     * Get the size of the deap
     *
     * @return amount of elements which are in the deap
     */
    public int getSize() {
        return size - 1;
    }

    /**
     * @return true if there a no items in the deap
     */
    public boolean isEmpty() {
        return size == 1;
    }

    /**
     * @return the deap in array form, mainly used for testing purposes
     */
    public ArrayList<E> toArray() {
        return this.deap;
    }

    /**
     * @return the deap in {@link ArrayList#toString(int[])}} form
     * @see #prettyString() to get a more readable string of this deap
     */
    @Override
    public String toString() {
        // O(n) iterations
        return deap.toString();
    }

    /**
     * Make a tree diagram from this deap and return it The tree is made recursively
     * with the {@link #print(int, StringBuilder, String, String)} method
     *
     * @see #toString() if you want to get a representation of this deap in a more
     *      basic way
     */
    public String prettyString() {
        StringBuilder sb = new StringBuilder();
        // O(log n) iterations
        print(1, sb, "", "");
        return sb.toString();
    }

    /**
     * Clear the deap
     * <p>
     * Removes all items that were in the deap And resets the size
     */
    public void clear() {
        // O(n) iterations
        deap.clear();
        size = 1;
    }

    /**
     * Insert an item into the deap
     *
     * @param value to add
     * @throws RuntimeException     this exception is thrown when you try to add a
     *                              value that is used as the 'EMPTY' value
     * @throws ArrayIsFullException Gets thrown when you are trying to add an item
     *                              even though the deap is full
     */
    public void add(E value) {
        if (value == null) {
            throw new RuntimeException("You can't add null to this deap");
        }

        /*
         * If the deap is empty add the value to the third index
         */
        if (++size == 2) {
            this.deap.set(size, value);
            return;
        }

        /*
         * Checks if the item will be added to the min or max heap
         */
        int i;
        if (inMaxHeap(size)) {
            i = minPartner(size);
            // If the value is smaller than its partner swap their positions
            if (value.compareTo(deap.get(i)) < 0) {
                deap.set(size, deap.get(i));
                deap.set(i, value);
                // If the partners are swapped, you enter the min heap. So therefore, the min
                // insert is used
                minInsert(i);
            } else {
                // Max insert the value
                deap.set(size, value);
                maxInsert(size);
            }
        } else {
            // The opposite of the above
            i = maxPartner(size);
            if (value.compareTo(deap.get(i)) > 0) {
                deap.set(size, deap.get(i));
                deap.set(i, value);
                maxInsert(i);
            } else {
                deap.set(size, value);
                minInsert(size);
            }
        }
    }

    /**
     * @return the lowest number in the deap
     */
    public E getLow() {
        int size = this.size - 1;
        if (size > 0) {
            return deap.get(2);
        } else {
            return null;
        }
    }

    /**
     * @return the highest number in the deap
     */
    public E getHigh() {
        int size = this.size - 1;
        if (size < 2) {
            if (size > 0) {
                return deap.get(2);
            } else {
                return null;
            }
        } else {
            return deap.get(3);
        }
    }

    /**
     * Remove the lowest number
     *
     * @return the item you removed
     */
    public E removeLow() {
        if (isEmpty())
            return null;

        int i;
        int j;
        // The lowest value in the deap
        E key = deap.get(2);
        // The last value in the deap
        E value = deap.get(size--);
        // Clear the last value
        deap.set(size + 1, null);

        // move smaller child to i
        // O(n) iterations
        for (i = 2; 2 * i <= size; i = j) {
            j = i * 2;
            if (j + 1 <= size && deap.get(j).compareTo(deap.get(j + 1)) > 0) {
                j++;
            }
            deap.set(i, deap.get(j));
        }

        // try to put x at leaf i
        j = maxPartner(i);

        if (deap.get(j) != null && value.compareTo(deap.get(j)) > 0) {
            // If the value is in max heap you swap i with j
            deap.set(i, deap.get(j));
            deap.set(j, value);
            maxInsert(j);
        } else if (deap.get(i) != null) {
            // If the value is in min heap you swap the value to i
            deap.set(i, value);
            minInsert(i);
        }
        return key;
    }

    /**
     * Remove the highest number
     * <p>
     * O(n) and a max- or min-insert so O(n + log n) [not sure though :eyes:]
     *
     * @return the element you removed
     */
    public E removeHigh() {
        if (isEmpty())
            return null;

        int i;
        int j;
        // The highest value in the deap
        E key = deap.get(3);
        // The last value in de the deap
        E value = deap.get(size--);
        // Clear the last value
        deap.set(size + 1, null);

        // move larger child to i
        // O(n) iterations
        for (i = 3; 2 * i <= size; i = j) {
            j = i * 2;
            if (j + 1 <= size && (deap.get(j).compareTo(deap.get(j + 1)) < 0)) {
                j++;
            }
            deap.set(i, deap.get(j));
        }

        // Get the min partner of i
        j = minPartner(i);

        // Biggest at low side
        int biggest = j;
        if (j * 2 <= size) {
            biggest = j * 2;
            if (j * 2 + 1 <= size && deap.get(j * 2).compareTo(deap.get(j * 2 + 1)) < 0) {
                biggest++;
            }
        }

        // If value is smaller than the biggest at the min side
        // Swap them and call the min-insert
        if (deap.get(biggest) != null && value.compareTo(deap.get(biggest)) < 0) {
            deap.set(i, deap.get(biggest));
            deap.set(biggest, value);
            minInsert(biggest);
        } else if (deap.get(i) != null) {
            // Else, max insert the value
            deap.set(i, value);
            maxInsert(i);
        }
        return key;
    }

    /**
     * Insert an item into the max-heap side
     * <p>
     * O(log n) iterations
     *
     * @param pos position of the item
     */
    private void maxInsert(int pos) {
        E value = deap.get(pos);

        int parentPos = parent(pos);
        E parentVal = deap.get(parentPos);
        if (parentVal == null) {
            return;
        }

        // If the value is bigger than its parent swap them and call maxInsert
        // Recalling maxInsert makes it recursive
        if (value.compareTo(parentVal) > 0) {
            swap(parentPos, pos);
            maxInsert(parentPos);
        }
    }

    /**
     * Insert an item into the min-heap side
     * <p>
     * O(log n) iterations
     *
     * @param pos position of the item
     */
    private void minInsert(int pos) {
        E value = deap.get(pos);

        int parentPos = parent(pos);
        E parentVal = deap.get(parentPos);
        if (parentVal == null) {
            return;
        }

        // If the value is smaller than its parent swap them and call minInsert
        // Recalling minInsert makes it recursive
        if (value.compareTo(parentVal) < 0) {
            swap(pos, parentPos);
            minInsert(parentPos);
        }
    }

    /**
     * Swap 2 items with each-other
     *
     * @param pos1 item1
     * @param pos2 item2
     */
    private void swap(int pos1, int pos2) {
        E save = deap.get(pos1);
        deap.set(pos1, deap.get(pos2));
        deap.set(pos2, save);
    }

    /**
     * Get the parent of a node
     *
     * @param pos node position
     * @return parent of this node
     */
    private int parent(int pos) {
        return pos / 2;
    }

    /**
     * Get the sibling of a max-side position
     *
     * @param pos position in the max-side
     * @return sibling node in min-side
     */
    private int minPartner(int pos) {
        return (int) (pos - Math.pow(2, log2(pos) - 1));
    }

    /**
     * Get the sibling of a min-side position
     *
     * @param pos position in the min-side
     * @return sibling node in max-side
     */
    private int maxPartner(int pos) {
        pos = (int) (pos + Math.pow(2, log2(pos) - 1));
        if (pos > size) {
            pos /= 2;
        }
        return pos;
    }

    /**
     * Calculate log2 of n
     *
     * @param n n
     * @return log2(n)
     */
    private int log2(int n) {
        return (int) (Math.log10(n) / Math.log10(2));
    }

    /**
     * Check if position is in the max-heap or in the min-heap
     * <p>
     * eg. if we take 13 as the position we get this:
     * <p>
     * 13 > 3 * (log(13) - 1)^2 - 1 13 > 3 * (3 - 1)^2 - 1 13 > 3 * (2)^2 - 1 13 > 3
     * * 4 - 1 13 > 12 - 1 13 > 11
     * <p>
     * 11 is the middle and 13 is greater than the middle So 13 is in the max heap
     *
     * @param pos position to check
     * @return true if position is in the max-heap
     */
    private boolean inMaxHeap(int pos) {
        return pos > (3 * Math.pow(2, log2(pos) - 1) - 1);
    }

    /**
     * Recursive method for making a fancy tree
     *
     * @see #prettyString()
     */
    private void print(int pos, StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(deap.get(pos) == null ? "ROOT" : deap.get(pos));
        buffer.append('\n');

        int leftChild = pos * 2;
        int rightChild = leftChild + 1;

        if (rightChild <= deap.size()) {
            if (deap.get(leftChild) != null) {
                boolean rightExists = deap.get(rightChild) != null;
                print(leftChild, buffer, childrenPrefix + (rightExists ? "├── " : "└── "),
                        childrenPrefix + (rightExists ? "│   " : "    "));
                if (rightExists) {
                    print(rightChild, buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
                }
            }
        }
    }

    /**
     * Use this method to print out this deap in a tree-form diagram
     *
     * @see #prettyString()
     */
    public void prettyPrint() {
        System.out.println(prettyString());
    }
}
